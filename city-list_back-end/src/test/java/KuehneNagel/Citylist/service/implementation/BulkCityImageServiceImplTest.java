package KuehneNagel.Citylist.service.implementation;

import KuehneNagel.Citylist.controller.pojo.CityRecordRequest;
import KuehneNagel.Citylist.exception.CityListException;
import com.amazonaws.util.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BulkCityImageServiceImplTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @InjectMocks
    private BulkCityImageServiceImpl bulkCityImageService;

    @Mock
    private CityImageServiceImpl cityImageService;

    @Test(expected = CityListException.class)
    public void createBulkCityRecords() throws CityListException, IOException {
        List<CityRecordRequest> cityRecordRequests = new ArrayList<>();
        CityRecordRequest cityRecordRequest1 = new CityRecordRequest("1", "city1", "link1");
        CityRecordRequest cityRecordRequest2 = new CityRecordRequest("2", "city2", "link2");
        cityRecordRequests.add(cityRecordRequest1);
        cityRecordRequests.add(cityRecordRequest2);
        bulkCityImageService.createBulkCityRecords(getSampleFile());
        doNothing().when(cityImageService).createAll(cityRecordRequests);

        bulkCityImageService.createBulkCityRecords(null);
    }

    private MultipartFile getSampleFile() throws IOException {
        File file = new File("src/test/resources/sample.csv");
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
    }
}