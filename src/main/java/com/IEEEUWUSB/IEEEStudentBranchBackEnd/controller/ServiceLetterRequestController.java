package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.ServiceLetterReqDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.ServiceLetterRequest;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.ServiceLetterRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("api/v1/service")
public class ServiceLetterRequestController {
    @Autowired
    private ServiceLetterRequestService serviceLetterRequestService;

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addRequest(HttpServletRequest request, @RequestBody ServiceLetterReqDTO service) {
        CommonResponseDTO<ServiceLetterRequest> commonResponseDTO = new CommonResponseDTO<>();
        try {
            User user = (User) request.getAttribute("user");
            ServiceLetterRequest newRequest = ServiceLetterRequest.builder()

                    .remarks(service.getRemarks())
                    .email(service.getEmail())
                    .status("TODO")
                    .type(service.getType())
                    .request_date(LocalDate.now())
                    .due_date(service.getDue_date())
                    .user(user)
                    .build();
            commonResponseDTO.setData(serviceLetterRequestService.saveServiceLetterRequest(newRequest));
            commonResponseDTO.setMessage("Service Letter Request added successfully");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            commonResponseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }


}
