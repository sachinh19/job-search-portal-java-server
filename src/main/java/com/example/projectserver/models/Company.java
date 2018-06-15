package com.example.projectserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String apiId = "Not Available";

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String state;

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<Employer> employers;

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<Job> jobs;
}
