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

    /**
     * @param file Multipart request of csv for n=bulk city creation at initially
     * @return  Http status code 200 for successful upload
     * @throws CityListException
     */
    //TODO Need to implement method for check status for each file about file status
    @ApiOperation(value = "Create bulk city records by csv")
    @PostMapping
    public ResponseEntity bulkCityAdd(@RequestParam("file") MultipartFile file) throws CityListException {
        bulkCityImageService.createBulkCityRecords(file);

        return new ResponseEntity<>( HttpStatus.OK);
    }
}
