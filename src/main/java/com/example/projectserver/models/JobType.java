package com.example.projectserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class JobType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobType_id")
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "jobSeekers_jobType_table", joinColumns = @JoinColumn(name = "jobType_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    @JsonIgnore
    @Getter
    @Setter
    private List<JobSeeker> jobSeekers;

    @OneToMany(mappedBy = "jobType" ,cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<Job> jobs;

}
