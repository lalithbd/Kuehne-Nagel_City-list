package KuehneNagel.Citylist.controller;

import KuehneNagel.Citylist.controller.dto.ImageResponse;
import KuehneNagel.Citylist.service.CityImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller contains api for city image view and management
 */
@RestController
@RequestMapping("/images")
@Api(value = "APIs for Image management", tags = {"Images"})
public class CityImageController {

    @Autowired
    private CityImageService imageService;

    /**
     * @param isSearch indicate that search request
     * @param searchValue if search search value should be presented
     * @param pageSize indicate the page size for response data
     * @param pageNumber indicate the page number for response data
     * @return image information with page details
     */
    @ApiOperation(value = "Api for load image list with data", notes = "featured with search by name with pagination")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ImageResponse>> getCityImages(@RequestParam(value = "is-search", required = false) boolean isSearch,
                                                             @RequestParam(value = "search-value", required = false) String searchValue,
                                                             @RequestParam(value = "page-size", defaultValue = "10", required = false) int pageSize,
                                                             @RequestParam(value = "page-number", defaultValue = "0", required = false) int pageNumber) {
        Page<ImageResponse> imageResponses = imageService.loadImages(isSearch, searchValue, pageSize, pageNumber);
        return new ResponseEntity<>(imageResponses, HttpStatus.OK);
    }

    /**
     * @param file image file for update
     * @param id original city reference id from the api response
     * @param cityName city name for replacement
     * @return Http code 202 for success
     */
    @ApiOperation(value = "Api for load image list with data", notes = "featured with search by name with pagination")
    @PutMapping(value = "/update")
    public ResponseEntity updateCityData(@RequestParam ("file") MultipartFile file,
                                         @RequestParam("id") Long id,
                                         @RequestParam("city-name") String cityName) {
        imageService.updateCity(id, file, cityName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
