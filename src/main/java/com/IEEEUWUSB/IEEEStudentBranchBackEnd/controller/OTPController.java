package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.EmailService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OTPService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/auth")
public class OTPController {

    @Autowired
    private final OTPService otpService;

    @Autowired
    private final EmailService emailService;

    public OTPController(OTPService otpService, EmailService emailService) {
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("otp/create-accout")
    public Void createAccount() {
        emailService.sendMail( "aasadh2000@gmail.com","hi","gh");
        return null;
    }
}
