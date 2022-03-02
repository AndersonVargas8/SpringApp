package com.app.springapp.service;


import java.util.HashSet;
import java.util.Set;

import com.app.springapp.entity.Role;
import com.app.springapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements  UserDetailsService{

    @Autowired
    UserRepository repUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.app.springapp.entity.User appUser = repUser.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username inválido"));

        Set<GrantedAuthority> grantList = new HashSet<GrantedAuthority>();

        for(Role role: appUser.getRoles()){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescription());
            grantList.add(grantedAuthority);
        }

        UserDetails user = (UserDetails) new User(username, appUser.getPassword(),grantList);
        return user;
    }
    
}
