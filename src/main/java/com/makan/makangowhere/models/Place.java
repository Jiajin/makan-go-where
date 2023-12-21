package com.makan.makangowhere.models;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@Entity
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean chosen;

    // @CreatedBy
    private String createdBy;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedDate;

    @ManyToOne
    @JoinColumn(name = "meeting_id")

    private Meeting meeting;

    protected Place() {
    }

    public Place(String name, String address, String createdBy) {
        this.name = name;
        this.createdBy = createdBy;
        this.address = address;
    }
}
