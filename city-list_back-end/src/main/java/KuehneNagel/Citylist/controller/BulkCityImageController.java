package KuehneNagel.Citylist.controller;

import KuehneNagel.Citylist.exception.CityListException;
import KuehneNagel.Citylist.service.BulkCityImageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bulk-images")
public class BulkCityImageController {

    @Autowired
    private BulkCityImageService bulkCityImageService;

    @ApiOperation(value = "Create bulk city records by csv")
    @PostMapping(value = "/bulk")
    public ResponseEntity bulkCityAdd(@RequestParam("file") MultipartFile file) throws CityListException {
        bulkCityImageService.createBulkCityRecords(file);

        return new ResponseEntity<>( HttpStatus.OK);
    }
}
