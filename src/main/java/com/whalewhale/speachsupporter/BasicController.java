package com.whalewhale.speachsupporter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;

@Controller///클래스에 붙이면
public class BasicController {
    //서버기능 생성 가능
    @GetMapping("/")
//메인페이지 접속
    //@ResponseBody : return 오른쪽에 있는 문자를 그대로 보내줌,
    String hello() {
        return "index.html";//기본 폴더는 static
    }


    @GetMapping("/about")//새로운 페이지 접속
    @ResponseBody
    String about() {

        return "이 사이트는요 주저리 주저리" + "옛다 날자" + ZonedDateTime.now().toString();
    }


    @GetMapping("/date")
    @ResponseBody
    String date() {
        SimpleDateFormat now = new SimpleDateFormat("yyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = now.format(c1.getTime());
        return "Today is : "  + strToday;


    }
}