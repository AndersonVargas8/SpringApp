package com.app.springapp.service;

import java.util.Optional;

import javax.validation.Valid;

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


    
    private boolean checkUserNameAvailable(User user) throws Exception{
        Optional<User> userFound = repUser.findByUsername(user.getUsername());

        if(userFound.isPresent()){
            throw new Exception("Nombre de usuario no disponible");
        }
        return true;
    }

    private boolean checkPasswordValid(User user) throws Exception{
        if(!user.getPassword().equals(user.getConfirmPassword())){
            throw new Exception("Las contraseñas no coinciden");
        }
        return true;
    }



    @Override
    public User createUser(User user) throws Exception{
        if(checkUserNameAvailable(user) && checkPasswordValid(user))
            user = repUser.save(user);
        
        return user;
    }
}
