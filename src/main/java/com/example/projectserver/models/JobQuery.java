package com.example.projectserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class JobQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_id")
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String post;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    @Setter
    @JsonFormat(pattern="MM/dd/yyyy", timezone = "EST")
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
    @Getter
    @Setter
    private Person postedBy;

}
