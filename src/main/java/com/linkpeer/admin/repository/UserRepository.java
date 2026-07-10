package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
    
    @Query("SELECT u FROM User u WHERE lower(u.name) LIKE lower(concat('%', :keyword, '%')) OR lower(u.email) LIKE lower(concat('%', :keyword, '%')) OR lower(u.department) LIKE lower(concat('%', :keyword, '%')) OR lower(u.college) LIKE lower(concat('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);
    
    @Query("SELECT u FROM User u WHERE u.facultyProof IS NOT NULL AND (u.isVerified = false OR u.isVerified IS NULL)")
    List<User> findPendingFaculty();
    
    long countByUserType(String userType);
    
    @Query("SELECT count(u) FROM User u WHERE u.userType = 'faculty' AND u.isVerified = true")
    long countVerifiedFaculty();
    
    @Query("SELECT u FROM User u ORDER BY u.rankingScore DESC LIMIT 10")
    List<User> findTopUsers();
    
    @Query("SELECT count(u) FROM User u WHERE CAST(u.createdAt AS date) = CURRENT_DATE")
    long countNewUsersToday();
}
