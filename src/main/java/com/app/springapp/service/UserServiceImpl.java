package com.app.springapp.service;

import java.util.Optional;

import com.app.springapp.dto.ChangePasswordForm;
import com.app.springapp.entity.User;
import com.app.springapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repUser;

    @Override
    public Iterable<User> getAllUsers() {
        return repUser.findAll();
    }

    private boolean checkUserNameAvailable(User user) throws Exception {
        Optional<User> userFound = repUser.findByUsername(user.getUsername());

        if (userFound.isPresent()) {
            throw new Exception("Nombre de usuario no disponible");
        }
        return true;
    }

    private boolean checkPasswordValid(User user) throws Exception {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new Exception("Las contraseñas no coinciden");
        }
        return true;
    }

    @Override
    public User createUser(User user) throws Exception {
        if (checkUserNameAvailable(user) && checkPasswordValid(user))
            user = repUser.save(user);

        return user;
    }

    @Override
    public User getUserById(Long id) throws Exception {
        User user = repUser.findById(id).orElseThrow(() -> new Exception("El usuario no existe"));
        return user;
    }

    @Override
    public User updateUser(User fromUser) throws Exception {
        User toUser = getUserById(fromUser.getId());
        mapUser(fromUser, toUser);
        User user = repUser.save(toUser);
        return user;
    }

    protected void mapUser(User from, User to) {
        to.setUsername(from.getUsername());
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setRoles(from.getRoles());
        to.setConfirmPassword(to.getPassword());
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        User user = getUserById(id);
        
        repUser.delete(user);
    }

    @Override
    public User changePassword(ChangePasswordForm form) throws Exception{
		User storedUser = getUserById(form.getId());  
		if(!form.getCurrentPassword().equals(storedUser.getPassword())) {
			throw new Exception("Contraseña actual incorrecta.");
		}
		
		if (form.getCurrentPassword().equals(form.getNewPassword())) {
			throw new Exception("La nueva contraseña debe ser diferente a la actual contraseña");
		}
		
		if(!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("Las contraseñas no coinciden");
		}
		
		storedUser.setPassword(form.getNewPassword());
        storedUser.setConfirmPassword(form.getConfirmPassword());
		return repUser.save(storedUser);
	}
}
