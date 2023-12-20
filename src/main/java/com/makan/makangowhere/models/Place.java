package com.makan.makangowhere.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String address;

    private Boolean chosen;

    @CreatedBy
    private String createdBy;

    @CreatedDate // Add not null?
    private Instant createdDate;

    @LastModifiedDate
    private Instant modifiedDate;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
}
