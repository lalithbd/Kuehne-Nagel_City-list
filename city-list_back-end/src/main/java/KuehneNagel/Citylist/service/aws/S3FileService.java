package KuehneNagel.Citylist.service.aws;

import KuehneNagel.Citylist.exception.S3Exception;

import java.io.File;

public interface S3FileService {

    byte[] getByteData(String storedFileName) throws S3Exception;

    void replaceAndUpdate(String currentFileName, String newFileName, File file) throws S3Exception;
}
