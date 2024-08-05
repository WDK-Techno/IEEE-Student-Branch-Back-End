package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.config.JwtUtils;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AuthenticationDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AuthenticationResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.RegisterDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo repository;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final AcademicYearService academicYearService;
    private final UserRoleDetailsServices userRoleDetailsServices;
    private final RoleServices roleServices;


    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private View error;

    public String register(RegisterDTO request) throws Exception {

        AcademicYear academicYear = academicYearService.getAcademicYearById(request.getAcademicId());
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .contactNo(request.getContactNo())
                .createdDate(LocalDateTime.now())
                .status("NOT_VERIFIED")
                .academicYear(academicYear)
                .build();

        var memberRole = roleServices.getRoleByName("Member");
        if(memberRole != null){
            var savedUser = repository.save(user);
            var userRoleDetails = UserRoleDetails.builder()
                    .user(savedUser)
                    .role(memberRole)
                    .isActive(true)
                    .type(memberRole.getType())
                    .start_date(LocalDateTime.now()).build();
            userRoleDetailsServices.createUserRoleDetails(userRoleDetails);
            var otpCode = otpService.generateOTP();
            LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(5);
            var otp = OTP.builder()
                    .otpCode(otpCode)
                    .exprieDate(expiryDateTime)
                    .user(savedUser).build();

            otpService.createOtp(otp);
            emailService.sendMail(savedUser.getEmail(),"OTP Verification","This is your OTP "+otpCode);
            return "OTP Sent";
        }else{
            throw new Exception("Member Role Not Found");
        }

    }


    public AuthenticationResponseDTO authenticate(AuthenticationDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken;

        if(user.getStatus().equals("VERIFIED")){
            jwtToken = jwtUtils.generateTokenFromUsername(user);
        }else{
            jwtToken = "";
            var otpCode = otpService.generateOTP();
            LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(5);
            var otp = OTP.builder()
                    .otpCode(otpCode)
                    .exprieDate(expiryDateTime)
                    .user(user).build();
            otpService.createOtp(otp);
            emailService.sendMail(user.getEmail(),"OTP Verification","This is your OTP "+otpCode);

        }

        UserRoleDetails userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user,true,"MAIN");
       return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .userRoleDetails(userRoleDetails)
                .build();

    }





}
