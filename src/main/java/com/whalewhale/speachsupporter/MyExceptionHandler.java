package com.whalewhale.speachsupporter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handler1(){
        return ResponseEntity.status(400).body("유저타입에러");
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handler(){
//        return ResponseEntity.status(400).body("유저에러");
//    }


}
//rest api는 try/catch 또는 @ExceptionHandler
// ResponseEntity로 에러코드 전송 가능
