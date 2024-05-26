package com.jobmatchup.jobmatchup.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobmatchup.jobmatchup.entities.otp.OneTimePin;
import com.jobmatchup.jobmatchup.entities.otp.OneTimePinRepository;

@Service
public class OneTimePinService {

    @Autowired
    private OneTimePinRepository otpRepository;
    @Autowired
    private EmailService emailService;

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void saveOtp(String email, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        OneTimePin otpEntity = new OneTimePin();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(expiryTime);
        otpRepository.save(otpEntity);
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<OneTimePin> otpEntityOptional = otpRepository.findByEmailAndOtp(email, otp);
        if (otpEntityOptional.isPresent()) {
            OneTimePin otpEntity = otpEntityOptional.get();
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (currentDateTime.isBefore(otpEntity.getExpiryTime())) {
                // OTP is valid
                return true;
            } 
            else {
                // OTP has expired
                return false;
            }
        } 
        // OTP has expired
        return false;

    }

    public void sendOtpEmail(String email, String otp) {
        String subject = "OTP for Password Reset";
        String body = "Your OTP for password reset is: " + otp + ". It expires in 5 minutes.";
        emailService.sendEmail(email, subject, body);
    }
}
