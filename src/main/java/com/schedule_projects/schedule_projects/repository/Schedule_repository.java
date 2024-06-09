package com.schedule_projects.schedule_projects.repository;

import com.schedule_projects.schedule_projects.domain.Schedule;
import com.schedule_projects.schedule_projects.domain.User_info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Schedule_repository extends JpaRepository<Schedule, Integer> {
    // userId로 스케줄 조회
    List<Schedule> findByUser(User_info user);
    void deleteByTitle(String title);
}

