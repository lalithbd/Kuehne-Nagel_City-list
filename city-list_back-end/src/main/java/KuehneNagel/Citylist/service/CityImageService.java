package KuehneNagel.Citylist.service;

import KuehneNagel.Citylist.controller.dto.ImageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CityImageService {

    Page<ImageResponse> loadImages(boolean isSearch, String searchValue, int pageSize, int pageNumber);

    void updateCity(Long id, MultipartFile file, String cityName);
}
