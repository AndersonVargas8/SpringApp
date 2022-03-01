package com.app.springapp.repository;

import com.app.springapp.entity.Role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long>{
    
}
