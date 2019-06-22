package com.leyou.common.vo;

import com.leyou.common.exception.LyException;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private String timestamp;

    public ExceptionResult(LyException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
        this.timestamp = new Date().toLocaleString();
    }
}
