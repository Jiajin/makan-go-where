package com.makan.makangowhere.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
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

    @OneToMany(mappedBy = "meeting", fetch = FetchType.EAGER)
    private List<Place> places;

    @CreatedDate // Add not null?
    @Column(nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedDate;

    protected Meeting() {
    }

    public Meeting(String name, String createdBy, MeetingStatus status) {
        this.name = name;
        this.createdBy = createdBy;
        this.status = status;
    }

    public enum MeetingStatus {
        ACTIVE, FINAL
    }
}
