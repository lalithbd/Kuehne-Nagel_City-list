package KuehneNagel.Citylist.service.implementation;

import KuehneNagel.Citylist.controller.dto.ImageResponse;
import KuehneNagel.Citylist.controller.pojo.CityRecordRequest;
import KuehneNagel.Citylist.exception.CityListException;
import KuehneNagel.Citylist.exception.S3Exception;
import KuehneNagel.Citylist.model.City;
import KuehneNagel.Citylist.repository.CityRepository;
import KuehneNagel.Citylist.service.aws.implementation.S3FileServiceImpl;
import KuehneNagel.Citylist.service.external.implementation.HttpServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CityImageServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private S3FileServiceImpl s3FileService;

    @Mock
    private HttpServiceImpl httpService;

    @InjectMocks
    private CityImageServiceImpl cityImageService;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadImagesTest() {
        City city1 = new City(1L, "city1", "city1File");
        City city2 = new City(2L, "city2", "city2File");
        Page<City> cities = new PageImpl<>(Arrays.asList(city1, city2), getPageable(), 2);
        when(cityRepository.findAll(PageRequest.of(0, 2))).thenReturn(cities);
        Page<ImageResponse> imageResponses = cityImageService.loadImages(false, null, 2, 0);
        assertEquals(2, imageResponses.getContent().size());
    }

    @Test
    public void loadImagesTest2() {
        Page<City> cities = new PageImpl<>(new ArrayList<>(), getPageable(), 0);
        doReturn(cities).when(cityRepository).findAll(PageRequest.of(0, 2));
        Page<ImageResponse> imageResponses = cityImageService.loadImages(false, null, 2, 0);
        assertEquals(0, imageResponses.getContent().size());
    }

    private Pageable getPageable() {
        return new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 2;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
    }

    @Test
    public void getByteDataTest() throws S3Exception, CityListException {
        Optional<City> city = Optional.of(new City(1L, "name", "city1File"));
        doReturn(city).when(cityRepository).findById(1L);
        byte[] data = "name".getBytes();
        S3FileServiceImpl s3FileServiceImpl = Mockito.spy(s3FileService);
        when(s3FileServiceImpl.getByteData("city1File")).thenReturn(data);
        byte[] byteData = cityImageService.getByteData(1L);
        assertArrayEquals(byteData, null);
    }

    @Test(expected = CityListException.class)
    public void getByteDataExceptionTest() throws CityListException {
        doReturn(Optional.empty()).when(cityRepository).findById(1L);
        cityImageService.getByteData(1L);
    }

    @Test(expected = CityListException.class)
    public void updateCityFileTest() throws CityListException {
        cityImageService.updateCity(1L, null, "name");
    }

    @Test(expected = CityListException.class)
    public void updateCity1NameTest() throws CityListException {
        cityImageService.updateCity(1L, getMultipartFile(), null);
    }

    @Test(expected = CityListException.class)
    public void updateCityNullTest() throws CityListException {
        Optional<City> city = Optional.ofNullable(null);
        doReturn(city).when(cityRepository).findById(1L);
        cityImageService.updateCity(1L, getMultipartFile(), "name");
    }

    @Test
    public void updateCitySaveTest() throws CityListException, S3Exception {
        Optional<City> city = Optional.of(new City(1L, "name", "city1File"));
        City cityCreated = new City(1L, "name", "city1File");
        doReturn(city).when(cityRepository).findById(1L);
        doNothing().when(s3FileService).replaceAndUpdate("city1File", "new Name", new File("new Name"));
        doReturn(city).when(cityRepository).save(cityCreated);
        cityImageService.updateCity(1L, getMultipartFile(), "name");
    }

    private MultipartFile getMultipartFile() {
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
    }

    @Test
    public void createAll() throws S3Exception {
        byte[] data = "name".getBytes();
        doReturn(data).when(httpService).getDataFromUrl(anyString());
        List<CityRecordRequest> cityRecordRequests = new ArrayList<>();
        CityRecordRequest cityRecordRequest1 = new CityRecordRequest("1", "city1", "link1");
        CityRecordRequest cityRecordRequest2 = new CityRecordRequest("2", "city2", "link2");
        cityRecordRequests.add(cityRecordRequest1);
        cityRecordRequests.add(cityRecordRequest2);
        cityImageService.createAll(cityRecordRequests);

        doReturn(null).when(httpService).getDataFromUrl(anyString());
        cityImageService.createAll(cityRecordRequests);

        doThrow(S3Exception.class).when(s3FileService).replaceAndUpdate("city1File", "new Name", new File("new Name"));
        cityImageService.createAll(cityRecordRequests);
    }

}