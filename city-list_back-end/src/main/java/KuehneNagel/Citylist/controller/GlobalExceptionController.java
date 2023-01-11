package lk.adl.iot.sima.controller;

import lk.adl.iot.sima.exception.BmsException;
import lk.adl.iot.sima.exception.BmsExceptionCode;
import lk.adl.iot.sima.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected boolean isDebugEnable = logger.isDebugEnabled();

    @ExceptionHandler(value = BmsException.class)
    public ResponseEntity<ErrorResponse> handleBmsException(BmsException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getExceptionCode().name(), e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getHttpStatusCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.info("Unexpected exception occurs : ", e);
        ErrorResponse errorResponse = new ErrorResponse(BmsExceptionCode.SVR00x.name(), "Unexpected operation failure, please try again");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();

        List<String> errorMsgList = new ArrayList<>();
        if (!errorList.isEmpty()){
            errorList.forEach(error->{
                errorMsgList.add(error.getDefaultMessage());
            });
        }
        ErrorResponse errorResponse = new ErrorResponse(BmsExceptionCode.SVR00x.name(), String.join(", ", errorMsgList));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
