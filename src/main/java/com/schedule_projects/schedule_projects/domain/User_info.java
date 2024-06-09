package com.schedule_projects.schedule_projects.domain;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class User_info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", length = 20)
    private int userId;

    @Column(name = "user_name", length = 10, nullable = false)
    private String userName;

    @Column(name = "identifier", unique = true, length = 30, nullable = false)
    private String identifier;

    @Column(name = "password", unique = true, length = 30, nullable = false)
    private String password;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "update_time", insertable = false)
    private LocalDateTime updateTime;

    @Column(name = "connection_status", nullable = false)
    private String connectionStatus = "disconnected";

    // 새로운 사용자 생성 시 생성 시간 설정
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
