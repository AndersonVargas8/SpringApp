package com.app.springapp.service;

import com.app.springapp.entity.User;
import com.app.springapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository repUser;
    
    @Override
    public Iterable<User> getAllUsers(){
        return repUser.findAll();
    }
}
