package com.example.projectserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @ManyToOne
    @JsonIgnore
    @Getter
    @Setter
    private Person person;
}
