package KuehneNagel.Citylist.controller;

import KuehneNagel.Citylist.controller.dto.ErrorResponse;
import KuehneNagel.Citylist.exception.CityListException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected boolean isDebugEnable = logger.isDebugEnabled();

    @ExceptionHandler(value = CityListException.class)
    public ResponseEntity<ErrorResponse> handleBmsException(CityListException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatusCode().toString(), e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getHttpStatusCode());
    }
}
