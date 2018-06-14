package com.example.projectserver.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
public class Employer extends Person{

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String currentCompany;

    @Getter
    @Setter
    private String tenure;




}
