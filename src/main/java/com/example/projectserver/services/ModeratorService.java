package com.example.projectserver.services;

import com.example.projectserver.models.Moderator;
import com.example.projectserver.models.Role;
import com.example.projectserver.repositories.ModeratorRepository;
import com.example.projectserver.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class ModeratorService {

    private ModeratorRepository moderatorRepository;
    private RoleRepository roleRepository;

    @Autowired
    public ModeratorService(ModeratorRepository moderatorRepository,RoleRepository roleRepository){
        this.moderatorRepository = moderatorRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("api/moderator")
    public List<Moderator> findAllModerators() {
        return moderatorRepository.findAll();
    }

    @GetMapping("api/moderator/{moderatorid}")
    public Moderator findModeratorById(@PathVariable("moderatorid") int moderatorid) {

        return moderatorRepository.findById(moderatorid).orElse(null);
    }

    @PostMapping("api/register/moderator")
    public Moderator createModerator(@RequestBody Moderator moderator) {

        moderator.setRoleType("Moderator");
        Role role = roleRepository.findRoleByName("Moderator").orElse(null);
        if(role != null){

            moderator.setRole(role);
        }
        return moderatorRepository.save(moderator);
    }

    @PutMapping("api/moderator/{moderatorid}")
    public Moderator updateModerator(@PathVariable("moderatorid") int moderatorid,
                             @RequestBody Moderator newModerator,
                             HttpServletResponse response) {

        Moderator existingModerator = moderatorRepository.findById(moderatorid).orElse(null);

        if (existingModerator != null) {
            String username = newModerator.getUsername();
            String password = newModerator.getPassword();
            String firstName = newModerator.getFirstName();
            String lastName = newModerator.getLastName();
            String email = newModerator.getEmail();
            String expDescription = newModerator.getExpDescription();
            String aboutMe = newModerator.getAboutMe();

            if (username != null) {
                existingModerator.setUsername(username);
            }

            if (password != null) {
                existingModerator.setPassword(password);
            }
            if (firstName != null) {
                existingModerator.setFirstName(firstName);
            }
            if (lastName != null) {
                existingModerator.setLastName(lastName);
            }
            if (email != null) {
                existingModerator.setEmail(email);
            }
            if(expDescription != null){
                existingModerator.setExpDescription(expDescription);
            }
            if(aboutMe != null){
                existingModerator.setAboutMe(aboutMe);
            }

            return moderatorRepository.save(existingModerator);
        }

        response.setStatus(204);
        return existingModerator;
    }
}
