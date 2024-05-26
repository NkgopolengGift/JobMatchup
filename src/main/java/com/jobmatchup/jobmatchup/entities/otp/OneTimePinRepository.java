package com.jobmatchup.jobmatchup.entities.otp;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OneTimePinRepository extends JpaRepository<OneTimePin, Long> {
    
    Optional<OneTimePin> findByEmailAndOtp(String email, String otp);
}
