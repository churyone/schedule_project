package com.schedule_projects.schedule_projects.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", length = 255)
    private int eventId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User_info user;

    @Column(name = "schedule_title", length = 255, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "location")
    private String location;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "update_time", insertable = false)
    private LocalDateTime updateTime;

    @Column(name = "color", nullable = false)
    private String color;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    // 사용자 정보 업데이트 시 갱신 시간 설정
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}

