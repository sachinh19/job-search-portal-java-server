package com.example.projectserver.services;

import com.example.projectserver.models.Moderator;
import com.example.projectserver.models.Role;
import com.example.projectserver.repositories.ModeratorRepository;
import com.example.projectserver.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
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

    @GetMapping("api/moderator/username/{username}")
    public Moderator findJobSeekerByUsername(@PathVariable("username") String username) {
        return moderatorRepository.findModeratorByUsername(username).orElse(null);
    }


    @PostMapping("api/register/moderator")
    public Moderator createModerator(@RequestBody Moderator moderator) {

        moderator.setRoleType("Moderator");
        Role role = roleRepository.findRoleByName("Moderator").orElse(null);
        if(role != null){

            moderator.setRole(role);
        }
        Moderator user = moderatorRepository.save(moderator);
        return user;
    }

    @PutMapping("api/moderator/{username}")
    public Moderator updateModerator(@RequestBody Moderator newModerator,
                                     @PathVariable("username") String username,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        Moderator existingModerator = moderatorRepository.findModeratorByUsername(username).orElse(null);

        if (existingModerator != null) {
            String password = newModerator.getPassword();
            String firstName = newModerator.getFirstName();
            String lastName = newModerator.getLastName();
            String email = newModerator.getEmail();
            String expDescription = newModerator.getExpDescription();
            String aboutMe = newModerator.getAboutMe();

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
            existingModerator.setUpdated(new Date());
            return moderatorRepository.save(existingModerator);
        }

        response.setStatus(204);
        return existingModerator;
    }
}
