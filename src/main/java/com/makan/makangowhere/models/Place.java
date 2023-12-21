package com.makan.makangowhere.models;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

@Data
@Entity
@Table(name = "place")
@EntityListeners(AuditingEntityListener.class)
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
    @JsonIgnore
    private Meeting meeting;

    protected Place() {
    }

    public Place(String name, String address, String createdBy, Meeting meeting) {
        this.name = name;
        this.createdBy = createdBy;
        this.address = address;
        this.meeting = meeting;
        this.chosen = false;
    }
}
