package com.linkpeer.admin.repository;

import com.linkpeer.admin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    
    @Query("SELECT u FROM User u WHERE lower(u.name) LIKE lower(concat('%', :keyword, '%')) OR lower(u.email) LIKE lower(concat('%', :keyword, '%')) OR lower(u.department) LIKE lower(concat('%', :keyword, '%')) OR lower(u.college) LIKE lower(concat('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);
    
    @Query("SELECT u FROM User u WHERE (u.facultyVerificationImage IS NOT NULL OR u.facultyVerificationStatus = 'pending') AND (u.facultyVerified = false OR u.facultyVerified IS NULL)")
    List<User> findPendingFaculty();
    
    long countByUserType(String userType);
    
    @Query("SELECT count(u) FROM User u WHERE (u.userType = 'faculty' OR u.role = 'faculty') AND (u.facultyVerified = true OR u.isVerified = true)")
    long countVerifiedFaculty();
    
    @Query("SELECT u FROM User u ORDER BY u.rankingScore DESC LIMIT 10")
    List<User> findTopUsers();
    
    @Query("SELECT count(u) FROM User u WHERE CAST(u.createdAt AS date) = CURRENT_DATE")
    long countNewUsersToday();
}
