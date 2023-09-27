package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
public class BaseResponse implements Serializable {
    private Status status;
    private String message;

    public BaseResponse(Status status,String  message) {
        this.status = status;
        this.message = message;
    }
    public BaseResponse(Status status) {
        this.status = status;
    }


}
