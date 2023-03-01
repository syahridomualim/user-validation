package com.example.uservalidation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/*
* author: Mualim
* this is custom response
* */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private Date date;
    private String status;
    private int code;
    private T data;
}