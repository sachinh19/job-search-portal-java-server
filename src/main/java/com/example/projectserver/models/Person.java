package com.example.projectserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    @NotEmpty(message = "Please provide your username")
    private String username;

    @Getter
    @Setter
    @NotEmpty(message = "Please provide your password")
    private String password;


    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String email;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    @JsonFormat(pattern="MM/dd/yyyy", timezone = "EST")
    private Date created = new Date();

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    @JsonFormat(pattern="MM/dd/yyyy", timezone = "EST")
    private Date updated = new Date();

    @Getter
    @Setter
    private String roleType;

    @Column(name = "expDescription", length = 10000)
    @Getter
    @Setter
    private String expDescription;

    @Column(name = "aboutMe", length = 10000)
    @Getter
    @Setter
    private String aboutMe;


    @ManyToOne
    @JsonIgnore
    @Getter
    @Setter
    private Role role;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "following", joinColumns = {
            @JoinColumn(name = "follower", referencedColumnName = "person_id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "following", referencedColumnName = "person_id", nullable = false)})
    @JsonIgnore
    @Getter
    @Setter
    private List<Person> following;


    @ManyToMany(mappedBy = "following")
    @JsonIgnore
    @Getter
    @Setter
    private List<Person> followedBy;

    @ManyToMany(mappedBy = "persons")
    @JsonIgnore
    @Getter
    @Setter
    private List<Job> jobs;

    @OneToMany(mappedBy = "postedBy",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<JobQuery> queries;


    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<Project> projects;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<Award> awards;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<Skill> skills;
}
