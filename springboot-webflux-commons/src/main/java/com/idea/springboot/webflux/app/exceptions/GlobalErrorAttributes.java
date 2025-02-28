package com.idea.springboot.webflux.app.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        Throwable error = getError(request);

        errorAttributes.put("trace_id", UUID.randomUUID().toString());
        errorAttributes.put("timestamp", new Date());

        if (error instanceof CustomNotFoundException) {
            errorAttributes.put("error", "Not Found");
            errorAttributes.put("message", error.getMessage());
            errorAttributes.put("status", HttpStatus.NOT_FOUND.value());
        } else if (error instanceof WebExchangeBindException) {
            List<String> errors = ((WebExchangeBindException) error).getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            errorAttributes.put("errors", errors);
            errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
        } else if (error instanceof CategoryNotFountByIdException || error instanceof CategoryNotFoundByNameException || error instanceof ProductNotFoundException) {
            errorAttributes.put("message", error.getMessage());
            errorAttributes.put("status", HttpStatus.NOT_FOUND.value());
        } else {
            errorAttributes.put("message", "An unexpected error occurred");
            errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return errorAttributes;
    }
}
