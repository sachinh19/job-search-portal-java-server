package com.example.projectserver.services;

import com.example.projectserver.models.Role;
import com.example.projectserver.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    RoleService(RoleRepository roleRepository) {this.roleRepository = roleRepository;}

    @GetMapping("api/role")
    public List<Role> findAllRoles(){
        return roleRepository.findAll();
    }

    @GetMapping("api/role/{roleId}")
    public Role findRoleById(@PathVariable("roleId") int roleId){

        return roleRepository.findById(roleId).orElse(null);
    }

    @DeleteMapping("api/role/{roleId}")
    public void deleteRoleById(@PathVariable("roleId") int roleId){

        Role role = roleRepository.findById(roleId).orElse(null);

        if(role!= null){
            roleRepository.delete(role);
        }
    }

}