package com.schedule_projects.schedule_projects.repository;

import com.schedule_projects.schedule_projects.domain.User_info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface User_info_repository extends JpaRepository<User_info, String> {
    // identifier와 password로 사용자 조회
    Optional<User_info> findByIdentifierAndPassword(String identifier, String password);

    Optional<User_info> findByUserId(int userId);

    Optional<User_info> findByIdentifier(String identifier);
    User_info findByUserName(String userName);

    List<User_info> findByConnectionStatus(String connected);
}
