package KuehneNagel.Citylist.service;

import KuehneNagel.Citylist.exception.CityListException;
import org.springframework.web.multipart.MultipartFile;

public interface BulkCityImageService {

    void createBulkCityRecords(MultipartFile file) throws CityListException;
}
