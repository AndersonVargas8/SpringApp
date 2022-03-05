package com.app.springapp.service;

import com.app.springapp.Exception.UsernameOrIdNotFound;
import com.app.springapp.dto.ChangePasswordForm;
import com.app.springapp.entity.User;

public interface UserService {
    public Iterable<User> getAllUsers();

    public User createUser(User user) throws Exception; 

    public User getUserById(Long id) throws UsernameOrIdNotFound;

    public User updateUser(User user)throws Exception;

    public void deleteUser(Long id) throws UsernameOrIdNotFound;

    public User changePassword(ChangePasswordForm form) throws Exception;
}
