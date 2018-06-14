package com.example.projectserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Employer extends Person{

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String tenure;

    @Getter
    @Setter
    private String companyName;

    @ManyToOne
    @JsonIgnore
    @Getter
    @Setter
    private Company company;




}
