package KuehneNagel.Citylist.service.aws.implementation;

import KuehneNagel.Citylist.exception.S3Exception;
import KuehneNagel.Citylist.service.aws.S3FileService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class S3FileServiceImpl implements S3FileService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    @Autowired
    private AmazonS3 s3client;

    @Override
    public byte[] getByteData(String storedFileName) throws S3Exception {
        S3Object s3Object;
        try {
            s3Object = s3client.getObject(s3BucketName, storedFileName);
        } catch (AmazonServiceException e) {
            logger.error("Data Fetching error for key: {}", storedFileName);
            throw new S3Exception("File data fetching error");
        }
        try {
            return IOUtils.toByteArray(s3Object.getObjectContent());
        } catch (IOException e) {
            logger.error("Data conversion error for key: {}", storedFileName);
            throw new S3Exception("File data converting error");
        }
    }

    @Override
    public void replaceAndUpdate(String currentFileName, String newFileName, File file) throws S3Exception {
        if (currentFileName != null) {
            delete(currentFileName);
        }
        uploadToS3(file, newFileName);
    }

    private void uploadToS3(File file, String name) throws S3Exception {
        try {
            s3client.putObject(new PutObjectRequest(s3BucketName, name, file));
        } catch (AmazonServiceException e) {
            logger.error("File upload error for key: {}", name);
            throw new S3Exception("Fail to upload");
        }
    }

    private void delete(String name) throws S3Exception {
        try {
            s3client.deleteObject(s3BucketName, name);
        } catch (AmazonServiceException e) {
            logger.error("Data deletion error for key: {}", name);
            throw new S3Exception("Fail to delete");

        }
    }
}
