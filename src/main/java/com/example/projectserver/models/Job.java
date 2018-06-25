package com.example.projectserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String apiId = "Not Available";

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String country;

    @Column(name = "description", length = 1000000)
    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private Date postedDate;

    @Getter
    @Setter
    private int totalApplications = 0;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String keywords;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @Getter
    @Setter
    private Company company;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Getter
    @Setter
    private JobType jobType;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "person_job_table", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    @JsonIgnore
    @Getter
    @Setter
    private List<Person> persons;


    @OneToMany(mappedBy = "job",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<JobQuery> queries;

}
