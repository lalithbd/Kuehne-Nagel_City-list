package KuehneNagel.Citylist.exception;

import org.springframework.http.HttpStatus;

public class CityListException extends Exception {

    private final HttpStatus httpStatusCode;

    public CityListException(HttpStatus httpStatusCode, String description) {
        super(description);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

}
