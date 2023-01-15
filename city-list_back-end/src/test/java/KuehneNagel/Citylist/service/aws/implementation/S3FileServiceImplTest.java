package KuehneNagel.Citylist.service.aws.implementation;

import KuehneNagel.Citylist.exception.S3Exception;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class S3FileServiceImplTest {

    @InjectMocks
    private S3FileServiceImpl s3FileService;

    @Mock
    private AmazonS3 amazonS3;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void getByteData() throws S3Exception, IOException {
        S3Object s3Object = new S3Object();
        when(amazonS3.getObject("name", "bucket")).thenReturn(s3Object);
        byte[] data = s3FileService.getByteData("name");
        assertArrayEquals(data, IOUtils.toByteArray(s3Object.getObjectContent()));
    }

    @Test
    public void replaceAndUpdate() throws S3Exception {
        when(amazonS3.putObject(new PutObjectRequest("bucket", "name", new File("file")))).thenReturn(new PutObjectResult());
        doNothing().when(amazonS3).deleteObject("bucket", "name");
        s3FileService.replaceAndUpdate("name", "name", new File("file"));
    }
}