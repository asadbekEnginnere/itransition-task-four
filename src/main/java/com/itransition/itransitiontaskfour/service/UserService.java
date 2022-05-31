package com.itransition.itransitiontaskfour.service;

import com.itransition.itransitiontaskfour.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {


    Map<String,String> save(User user);

    List<User> getAllUsers();

    Map<String, String> delete(Long id);

    Map<String,String> blockUser(Long id, HttpServletRequest request);

    Map<String, String> unBlockUser(Long id);
}
