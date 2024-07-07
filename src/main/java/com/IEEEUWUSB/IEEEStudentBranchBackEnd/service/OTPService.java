package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;



import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OTP;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.OTPRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class OTPService {

    @Autowired
    private OTPRepo otpRepo;

    private EmailService emailService;

    public OTP createOtp(OTP otp){
        return otpRepo.save(otp);
    }

    public OTP findOtpByuser(User user){
        Optional<OTP> otpOptional = otpRepo.findByuser(user);
        return otpOptional.orElse(null);
    }

    public Integer generateOTP(){
        Random rand = new Random();
        return rand.nextInt(100000,999999);
    }
}
