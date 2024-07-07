package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.OtpCheckDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OTP;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.EmailService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OTPService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("api/v1/auth")
public class OTPController {

    @Autowired
    private final OTPService otpService;

    @Autowired
    private final EmailService emailService;
    @Autowired
    private UserService userService;

    public OTPController(OTPService otpService, EmailService emailService) {
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("otp/check")
    public ResponseEntity<CommonResponseDTO> addPolicy(@RequestBody OtpCheckDTO otp) {
        CommonResponseDTO<String> commonResponseDTO = new CommonResponseDTO<>();
       User user =  userService.findUserByEmail(otp.getEmail());
       OTP dbOtp = otpService.findOtpByuser(user);
        if (dbOtp != null && dbOtp.getOtpCode().equals(otp.getOtp())) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (dbOtp.getExprieDate().isAfter(currentDateTime)) {
                commonResponseDTO.setMessage("OTP verified");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                commonResponseDTO.setMessage("OTP has expired");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            commonResponseDTO.setMessage("Invalid OTP");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }

    }
}
