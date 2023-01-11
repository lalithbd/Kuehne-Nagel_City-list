package KuehneNagel.Citylist.service;

import KuehneNagel.Citylist.controller.dto.ImageResponse;
import KuehneNagel.Citylist.controller.pojo.CityRecordRequest;
import KuehneNagel.Citylist.exception.CityListException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CityImageService {

    Page<ImageResponse> loadImages(boolean isSearch, String searchValue, int pageSize, int pageNumber);

    void updateCity(Long id, MultipartFile file, String cityName) throws CityListException;

    void createAll(List<CityRecordRequest> cityRecordRequests);
}
