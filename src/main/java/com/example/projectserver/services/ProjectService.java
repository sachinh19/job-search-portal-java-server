package com.example.projectserver.services;


import com.example.projectserver.models.Person;
import com.example.projectserver.models.Project;
import com.example.projectserver.repositories.PersonRepository;
import com.example.projectserver.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class ProjectService {

    private ProjectRepository projectRepository;
    private PersonRepository personRepository;

    @Autowired
    ProjectService(ProjectRepository projectRepository, PersonRepository personRepository) {
        this.projectRepository = projectRepository;
        this.personRepository = personRepository;}

    @GetMapping("api/project/{projectId}")
    public Project findProjectById(@PathVariable("projectId") int projectId){
        return projectRepository.findById(projectId).orElse(null);
    }

    @DeleteMapping("api/project/{projectId}")
    public void deleteProject(@PathVariable("projectId") int projectId){
        Project project = projectRepository.findById(projectId).orElse(null);

        if(project!= null){

            projectRepository.delete(project);
        }
    }

    @PostMapping("api/person/{personId}/project")
    public Project createProjectForPerson(@RequestBody Project newProject, @PathVariable("personId") int personId){

        Person person = personRepository.findById(personId).orElse(null);

        if(person != null){
            newProject.setPerson(person);
            return projectRepository.save(newProject);
        }

        return null;
    }

    @PutMapping("api/project/{projectId}")
    public Project updateProject(@PathVariable("projectId") int projectId, @RequestBody Project newProject) {

        Project existingProject = projectRepository.findById(projectId).orElse(null);

        if(existingProject != null){

            String name = newProject.getName();
            String description = newProject.getDescription();
            String link = newProject.getLink();

            if(name != null){
                existingProject.setName(name);
            }
            if(description != null){
                existingProject.setDescription(description);
            }
            if(link != null){
                existingProject.setLink(link);
            }
            return projectRepository.save(existingProject);
        }

        return null;
    }

}
