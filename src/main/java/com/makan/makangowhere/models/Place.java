package com.makan.makangowhere.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "place")
@IdClass(PlaceId.class)
@EntityListeners(AuditingEntityListener.class)
public class Place {
    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // private String id;

    // @CreatedBy
    @Id
    private String createdBy;

    @Id
    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonIgnore
    private Meeting meeting;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean chosen;

    @CreatedDate
    // @Column(nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedDate;

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
