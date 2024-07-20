package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AuthenticationDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AuthenticationResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.RegisterDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.AuthenticationService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponseDTO> register(@RequestBody RegisterDTO request) {
        CommonResponseDTO<Integer> commonResponseDTO = new CommonResponseDTO<>();
        try{
            String message = service.register(request);
            commonResponseDTO.setMessage(message);
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
        }catch (Exception e){
            User exists = userService.findUserByEmail(request.getEmail());
            if(exists != null){
                commonResponseDTO.setMessage("Email already exists");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.ALREADY_REPORTED);
            }else{
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<CommonResponseDTO> authenticate(@RequestBody AuthenticationDTO request) {
        CommonResponseDTO<AuthenticationResponseDTO> commonResponseDTO = new CommonResponseDTO<>();
        var data = service.authenticate(request);
        commonResponseDTO.setData(data);
        return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

    }


}
