package com.makan.makangowhere.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "meeting")
@EntityListeners(AuditingEntityListener.class)
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    // @CreatedBy
    private String createdBy;

    @Enumerated
    private MeetingStatus status;

    @OneToMany(mappedBy = "meeting")
    private List<Place> places;

    @CreatedDate // Add not null?
    @Column(nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedDate;

    protected Meeting() {
    }

    public Meeting(String name, String createdBy) {
        this.name = name;
        this.createdBy = createdBy;
        this.status = MeetingStatus.ACTIVE;
    }

    public enum MeetingStatus {
        ACTIVE, INACTIVE
    }
}
