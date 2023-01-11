package KuehneNagel.Citylist.service.implementation;

import KuehneNagel.Citylist.controller.pojo.CityRecordRequest;
import KuehneNagel.Citylist.exception.CityListException;
import KuehneNagel.Citylist.service.BulkCityImageService;
import KuehneNagel.Citylist.service.CityImageService;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class BulkCityImageServiceImpl implements BulkCityImageService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CityImageService cityImageService;

    @Override
    public void createBulkCityRecords(MultipartFile file) throws CityListException {
        if (file == null) {
            logger.info("Null file object for add record");
            throw new CityListException(HttpStatus.BAD_REQUEST, "No file object for add record");
        }
        byte[] data;
        try {
            data = file.getBytes();
        } catch (IOException e) {
            logger.info(e.getMessage());
            throw new CityListException(HttpStatus.BAD_REQUEST, "File content reading failed");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
        List<CityRecordRequest> cityRecordRequests = readData(inputStreamReader);
        cityRecordRequests.remove(0);
        cityImageService.createAll(cityRecordRequests);
    }

    private List<CityRecordRequest> readData(InputStreamReader inputStreamReader) throws CityListException {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.enable(CsvParser.Feature.TRIM_SPACES);
        ObjectReader reader;
        CsvSchema schema = csvMapper.schemaFor(CityRecordRequest.class).withColumnSeparator(',');
        reader = csvMapper.readerFor(CityRecordRequest.class).with(schema);
        try {
            return reader.<CityRecordRequest>readValues(inputStreamReader).readAll();
        } catch (IOException e) {
            logger.info(e.getMessage());
            throw new CityListException(HttpStatus.BAD_REQUEST, "File content reading failed");
        }
    }
}
