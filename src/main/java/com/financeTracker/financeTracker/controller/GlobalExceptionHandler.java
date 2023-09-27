package com.financeTracker.financeTracker.controller;

import com.financeTracker.financeTracker.data.dtos.BaseResponse;
import com.financeTracker.financeTracker.data.enums.Status;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends Controller {
    @ExceptionHandler(value = {RuntimeException.class})
    public BaseResponse handleRunTimeException(RuntimeException runtimeException){
        return responseWithUpdatedHttpStatus(new BaseResponse(Status.FORBIDDEN,runtimeException.getLocalizedMessage()));
    }
}
