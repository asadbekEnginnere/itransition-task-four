package com.itransition.itransitiontaskfour.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itransition.itransitiontaskfour.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestUserController {

    private final UserServiceImpl userService;


    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @PostMapping("/delete-all")
    public void deleteAll(@RequestBody List<String> users, HttpServletRequest request){
        users.stream().forEach(System.out::println);

        List<Long> usersId = new ArrayList<>();
        for (String user : users) {
            usersId.add(Long.valueOf(user));
        }

        userService.deleteAll(usersId,request);
    }

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @PostMapping("/block-all")
    public void blockAll(@RequestBody List<String> users, HttpServletRequest request){

        List<Long> usersId = new ArrayList<>();
        for (String user : users) {
            usersId.add(Long.valueOf(user));
        }

        userService.blockAll(usersId,request);
    }

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @PostMapping("/unblock-all")
    public void unBlockAll(@RequestBody List<String> users, HttpServletRequest request){

        List<Long> usersId = new ArrayList<>();
        for (String user : users) {
            usersId.add(Long.valueOf(user));
        }

        userService.unBlockAll(usersId,request);
    }





}
