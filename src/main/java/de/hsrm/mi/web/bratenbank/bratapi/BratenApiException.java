package de.hsrm.mi.web.bratenbank.bratapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BratenApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
}
