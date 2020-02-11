package com.phonebook.service;

import com.phonebook.model.Role;
import com.phonebook.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

     public Role findRole(Long id){
         return roleRepository.findOne(id);
     }
}