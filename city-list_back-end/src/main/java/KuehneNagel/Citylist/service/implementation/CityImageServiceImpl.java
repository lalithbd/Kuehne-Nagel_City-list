package KuehneNagel.Citylist.service.implementation;

import KuehneNagel.Citylist.controller.dto.ImageResponse;
import KuehneNagel.Citylist.controller.pojo.CityRecordRequest;
import KuehneNagel.Citylist.exception.CityListException;
import KuehneNagel.Citylist.exception.S3Exception;
import KuehneNagel.Citylist.model.City;
import KuehneNagel.Citylist.repository.CityRepository;
import KuehneNagel.Citylist.searchSpecification.CitySpecification;
import KuehneNagel.Citylist.service.CityImageService;
import KuehneNagel.Citylist.service.aws.S3FileService;
import KuehneNagel.Citylist.service.external.HttpService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CityImageServiceImpl implements CityImageService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private S3FileService s3FileService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HttpService httpService;

    Random random = new Random();
    private final int low = 10;
    private final int high = 10000;

    @Override
    public Page<ImageResponse> loadImages(boolean isSearch, String searchValue, int pageSize, int pageNumber) {
        Page<City> cities;
        if (!isSearch || searchValue == null) {
            cities = cityRepository.findAll(PageRequest.of(pageNumber, pageSize));
        } else {
            CitySpecification specification = new CitySpecification(searchValue);
            cities = cityRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        }
        List<City> cityContent = cities.getContent();
        if (cityContent.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), cities.getPageable(), cities.getTotalElements());
        }
        List<ImageResponse> imageResponses = getImageData(cityContent);
        return new PageImpl<>(imageResponses, cities.getPageable(), cities.getTotalElements());

    }


    @Override
    public byte[] getByteData(Long id) throws CityListException {
        City city = findOne(id);
        if (city == null) {
            logger.warn("City record doesn't exists by id : {}", id);
            throw new CityListException(HttpStatus.BAD_REQUEST, "City record doesn't exists");
        }
        byte[] data = new byte[0];
        try {
            data =  s3FileService.getByteData(city.getStoredFileName());
        } catch (S3Exception e) {
            logger.error("Byte data fetching error for city id : {}", id);
        }
        return data;
    }

    private List<ImageResponse> getImageData(List<City> cityContent) {
        List<ImageResponse> imageResponses = new ArrayList<>();
        cityContent.forEach(city -> {
            ImageResponse imageResponse = modelMapper.map(city, ImageResponse.class);
            imageResponses.add(imageResponse);
        });
        return imageResponses;
    }

    @Override
    public void updateCity(Long id, MultipartFile multipartFile, String cityName) throws CityListException {
        if (multipartFile == null) {
            logger.warn("File to be updated cannot be null");
            throw new CityListException(HttpStatus.BAD_REQUEST, "Image should be presented");
        }
        if (cityName == null) {
            logger.warn("Null city name presented to update");
            throw new CityListException(HttpStatus.BAD_REQUEST, "City name cannot be null");
        }
        City city = findOne(id);
        if (city == null) {
            logger.warn("City record doesn't exists by id : {}", id);
            throw new CityListException(HttpStatus.BAD_REQUEST, "City record doesn't exists");
        }
        String currentFileName = city.getStoredFileName();
        String newFileName = getRandomFileName(cityName);
        city.setStoredFileName(newFileName);
        city.setName(cityName);
        File file = getFileFromMultipartFile(newFileName, multipartFile);
        try {
            s3FileService.replaceAndUpdate(currentFileName, newFileName, file);
        } catch (S3Exception e) {
            logger.warn("City image upload failed for id : {}", id);
            throw new CityListException(HttpStatus.BAD_REQUEST, "City update Failed");
        }
        cityRepository.save(city);
    }

    private File getFileFromMultipartFile(String fileName, MultipartFile multipartFile) throws CityListException {
        try {
            File file = File.createTempFile(fileName, ".tmp");
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new CityListException(HttpStatus.BAD_REQUEST, "File data converting error");
        }
    }

    private String getRandomFileName(String name) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        int randomValue = random.nextInt(high - low) + low;
        return name + timestamp.getTime() + randomValue;
    }

    private City findOne(Long id) {
        Optional<City> cityOptional = cityRepository.findById(id);
        return cityOptional.orElse(null);
    }

    @Override
    @Async
    public void createAll(List<CityRecordRequest> cityRecordRequests) {
        cityRecordRequests.forEach(cityRecordRequest -> {
            byte[] data;
            data = httpService.getDataFromUrl(cityRecordRequest.getLink());
            if (data == null) {
                City city = new City(cityRecordRequest.getName());
                cityRepository.save(city);
                return;
            }
            String fileName = getRandomFileName(cityRecordRequest.getName());
            File file = new File(fileName);
            try {
                Files.write(Paths.get(fileName), data);
            } catch (IOException e) {
                logger.warn("City image upload failed for id : {}", e.getMessage());
                City city = new City(cityRecordRequest.getName());
                cityRepository.save(city);
                return;
            }
            try {
                s3FileService.replaceAndUpdate(null, fileName, file);
            } catch (S3Exception e) {
                logger.warn("City image upload failed for id : {}", e.getMessage());
                if (file.delete()) {
                    logger.warn("Local file deletion Failed : {}", fileName);
                }
                return;
            }
            if (file.delete()) {
                logger.warn("Local file deletion Failed : {}", fileName);
            }
            City city = new City(cityRecordRequest.getName(), fileName);
            cityRepository.save(city);
        });
    }
}
