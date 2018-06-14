package com.example.projectserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_id")
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String post;

    @Getter
    @Setter
    private Date created;

    @Getter
    @Setter
    private boolean status = false;

    @ManyToOne
    @JsonIgnore
    @Getter
    @Setter
    private Job job;

    @ManyToOne
    @JsonIgnore
    @Getter
    @Setter
    private Person postedBy;

}