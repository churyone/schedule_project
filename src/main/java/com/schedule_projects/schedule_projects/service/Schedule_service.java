package com.schedule_projects.schedule_projects.service;

import com.schedule_projects.schedule_projects.domain.Schedule;
import com.schedule_projects.schedule_projects.domain.User_info;
import com.schedule_projects.schedule_projects.repository.Schedule_repository;
import com.schedule_projects.schedule_projects.repository.User_info_repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class Schedule_service {
    @Autowired
    private Schedule_repository schedule_repository;
    @Autowired
    private User_info_repository user_info_repository;

    // 내 스케줄 조회 서비스 구현
    public List<Schedule> findSchedulesByUsername(String username) {
        User_info user = user_info_repository.findByUserName(username);
        if (user != null) {
            return schedule_repository.findByUser(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(Schedule_service.class);

    public boolean saveSchedule(User_info user, Schedule schedule) {
        logger.info("Saving schedule for user: " + user.getUserId());
        Schedule newschedule = new Schedule();
        // 새로운 일정 저장
        newschedule.setUser(user);
        newschedule.setTitle(schedule.getTitle());
        newschedule.setStartTime(schedule.getStartTime());
        newschedule.setEndTime(schedule.getEndTime());
        newschedule.setColor(schedule.getColor());
        logger.info("New schedule: "+"user" + newschedule.getUser()+"startTime"+newschedule.getStartTime()+"endTime"+newschedule.getEndTime()+"color"+newschedule.getColor());
        schedule_repository.save(newschedule);
        logger.info("Schedule saved successfully: "+schedule.getStartTime()+schedule.getEndTime());

        return true;
    }
    @Transactional
    public void deleteScheduleByTitle(String title) {
        schedule_repository.deleteByTitle(title);
    }
}

