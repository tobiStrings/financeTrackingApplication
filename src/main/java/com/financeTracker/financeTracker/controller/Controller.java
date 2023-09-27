package com.financeTracker.financeTracker.controller;

import com.financeTracker.financeTracker.data.dtos.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class Controller {
    @Autowired
    private HttpServletResponse response;

    <T extends BaseResponse> T responseWithUpdatedHttpStatus(T responseDTO) {
        switch (responseDTO.getStatus()) {
            case SUCCESS -> response.setStatus(HttpStatus.OK.value());
            case NO_CONTENT -> response.setStatus(HttpStatus.NO_CONTENT.value());
            case CREATED -> response.setStatus(HttpStatus.CREATED.value());
            case NOT_FOUND -> response.setStatus(HttpStatus.NOT_FOUND.value());
            case FORBIDDEN -> response.setStatus(HttpStatus.FORBIDDEN.value());
            case BAD_REQUEST -> response.setStatus(HttpStatus.BAD_REQUEST.value());
            case UNAUTHORIZED -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
            default -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return responseDTO;
    }
}
