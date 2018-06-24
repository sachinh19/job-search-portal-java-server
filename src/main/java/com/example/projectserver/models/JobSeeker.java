package com.example.projectserver.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class JobSeeker extends Person   {

    @Getter
    @Setter
    private String interestedPosition;

    @Getter
    @Setter
    private String totalExp;


    @ManyToMany(mappedBy = "jobSeekers")
    @Getter
    @Setter
    private List<JobType> jobTypes;
}
