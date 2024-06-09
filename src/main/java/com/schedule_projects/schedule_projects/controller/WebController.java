package com.schedule_projects.schedule_projects.controller;

import com.schedule_projects.schedule_projects.domain.Schedule;
import com.schedule_projects.schedule_projects.domain.User_info;
import com.schedule_projects.schedule_projects.repository.User_info_repository;
import com.schedule_projects.schedule_projects.service.Schedule_service;
import com.schedule_projects.schedule_projects.service.User_info_service;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class WebController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(WebController.class);
    @Autowired
    private User_info_service user_info_service;

    @Autowired
    private Schedule_service schedule_service;

    private static final Logger logger = Logger.getLogger(WebController.class.getName());
    @Autowired
    private User_info_repository user_info_repository;

    @GetMapping("/login+")
    public String login1Form() {
        return "login+"; // login+.html 반환
    }

    @GetMapping("/index_1")
    public String index1Form() {
        return "index_1"; // index_1.html 반환
    }

    @GetMapping("/newuser")
    public String newUserForm() {
        return "newuser"; // newuser.html 반환
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> user) {
        logger.info("Register attempt: identifier=" + user.get("identifier") + ", userName=" + user.get("userName"));
        String identifier = user.get("identifier");
        String password = user.get("password");
        String userName = user.get("userName");

        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = user_info_service.registerUser(identifier, password, userName);

            if (success) {
                logger.info("Registration successful for user: " + identifier);
                response.put("success", true);
                response.put("message", "회원가입이 성공적으로 완료되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                logger.info("Registration failed for user: " + identifier);
                response.put("success", false);
                response.put("message", "사용자가 이미 존재합니다.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        } catch (Exception e) {
            logger.info("Registration failed: " + e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/request_login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        User_info user= user_info_service.findByUserName(username);
        if (user != null && user.getPassword().equals(password)) {
            user.setConnectionStatus("connected");
            user_info_repository.save(user);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "로그인 성공"));
        } else {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "로그인 실패"));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        User_info user = user_info_service.findByUserName(username);
        if (user != null) {
            user.setConnectionStatus("disconnected");
            user_info_repository.save(user);
            return ResponseEntity.ok().body("Logout successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }
    @GetMapping("/schedules")
    public ResponseEntity<List<Map<String, Object>>> getUserSchedules(@RequestParam String username) {
        List<Schedule> schedules = schedule_service.findSchedulesByUsername(username);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Schedule schedule : schedules) {
            Map<String, Object> scheduleData = new HashMap<>();
            scheduleData.put("title", schedule.getTitle());
            scheduleData.put("startTime", schedule.getStartTime());
            scheduleData.put("endTime", schedule.getEndTime());
            scheduleData.put("color", schedule.getColor());
            response.add(scheduleData);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/saveSchedule")
    public ResponseEntity<Map<String,Object>> saveSchedule(@RequestParam String username, @RequestBody Schedule schedule) {
        logger.info("Received schedule: " + schedule.getColor()+schedule.getStartTime()+schedule.getEndTime()); // 로그로 schedule 객체 출력
        User_info user = user_info_service.findByUserName(username);
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            boolean result = schedule_service.saveSchedule(user, schedule);
            if(result) {
                response.put("success", true);
                response.put("eventId", schedule.getEventId());
                return ResponseEntity.ok(response); // HTTP 200 OK와 response 반환 - response에 success 여부와 eventId 반환
            }
        }
        response.put("success", false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 사용자 찾지 못했을 경우 HTTP 404와 false 반환
    }

    @GetMapping("/connected_users")
    public ResponseEntity<List<String>> getConnectedUsers() {
        List<User_info> connectedUsers = user_info_service.findByConnectionStatus("connected");
        List<String> usernames = connectedUsers.stream().map(User_info::getUserName).collect(Collectors.toList());
        return ResponseEntity.ok(usernames);
    }
    // 친구의 일정을 불러오는 엔드포인트 추가
    @GetMapping("/friend_schedule")
    public ResponseEntity<List<Map<String, Object>>> getFriendSchedule(@RequestParam String username) {
        List<Schedule> schedules = schedule_service.findSchedulesByUsername(username);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Schedule schedule : schedules) {
            Map<String, Object> scheduleData = new HashMap<>();
            scheduleData.put("title", schedule.getTitle());
            scheduleData.put("startTime", schedule.getStartTime());
            scheduleData.put("endTime", schedule.getEndTime());
            scheduleData.put("color", schedule.getColor());
            response.add(scheduleData);
        }

        return ResponseEntity.ok(response);
    }
    @PostMapping("/deleteSchedule")
    public ResponseEntity<Map<String, Object>> deleteSchedule(@RequestParam String title) {
        Map<String, Object> response = new HashMap<>();
        try {
            schedule_service.deleteScheduleByTitle(title);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

