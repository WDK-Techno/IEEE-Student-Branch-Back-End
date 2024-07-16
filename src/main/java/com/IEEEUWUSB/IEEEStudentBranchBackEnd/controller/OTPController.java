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
    public ResponseEntity<CommonResponseDTO> checkPolicy(@RequestBody OtpCheckDTO otp) {
        CommonResponseDTO<String> commonResponseDTO = new CommonResponseDTO<>();
        User user = userService.findUserByEmail(otp.getEmail());
        OTP dbOtp = otpService.findOtpByuser(user);
        if (dbOtp != null && dbOtp.getOtpCode().equals(otp.getOtp())) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (dbOtp.getExprieDate().isAfter(currentDateTime)) {
                otpService.deleteOTP(dbOtp);
                user.setStatus("VERIFIED");
                userService.saveUser(user);
                otpService.deleteOTP(dbOtp);
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

    @PostMapping("otp/send")
    public ResponseEntity<CommonResponseDTO> sentOTP(@RequestBody OtpCheckDTO otp) {
        CommonResponseDTO<String> commonResponseDTO = new CommonResponseDTO<>();
        try {
            User user = userService.findUserByEmail(otp.getEmail());
            try {
                var otpCode = otpService.generateOTP();
                LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(5);
                var existOTP = otpService.findOtpByuser(user);
                if (existOTP != null) {
                    existOTP.setOtpCode(otpCode);
                    existOTP.setExprieDate(expiryDateTime);
                    otpService.createOtp(existOTP);
                } else {
                    var newotp = OTP.builder()
                            .otpCode(otpCode)
                            .exprieDate(expiryDateTime)
                            .user(user).build();
                    otpService.createOtp(newotp);
                }

                emailService.sendMail(user.getEmail(), "OTP Verification", "This is your OTP " + otpCode);
                commonResponseDTO.setMessage("OTP Sent");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("Email not found");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
        }

    }
}
