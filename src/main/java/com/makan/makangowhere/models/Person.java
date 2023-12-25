package com.makan.makangowhere.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "person")
@EntityListeners(AuditingEntityListener.class)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String meetingCsvList;

    @Transient // To populate on retrieval
    private List<Meeting> meetingList;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedDate;

    protected Person() {
    }

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public List<String> getMeetingCsvList() {
        return meetingCsvList != null ? Arrays.asList(meetingCsvList.split(",")) : null;
    }

    public void setMeetingCsvList(List<String> meetings) {
        this.meetingCsvList = (meetings == null || meetings.size() == 0) ? null : String.join(",", meetings);
    }

}
