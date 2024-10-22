package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.ServiceLetterReqDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.ServiceLetterRequest;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.ServiceLetterRequestService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/service")
public class ServiceLetterRequestController {
    @Autowired
    private ServiceLetterRequestService serviceLetterRequestService;
    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

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

    @PutMapping("/{request_id}")
    public ResponseEntity<CommonResponseDTO> updateStatus(HttpServletRequest request, @PathVariable int request_id) {
        CommonResponseDTO<ServiceLetterRequest> commonResponseDTO = new CommonResponseDTO<>();
        try {
            User user = (User) request.getAttribute("user");
            List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
            boolean isServicePolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "SERVICE");

            ServiceLetterRequest serviceLetterRequest = serviceLetterRequestService.getServiceLetterRequestById(request_id);
            if (isServicePolicyAvailable) {
                if (serviceLetterRequest == null) {
                    commonResponseDTO.setMessage("Service Letter Request not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                } else {
                    serviceLetterRequest.setStatus("REVIEWED");
                    serviceLetterRequest.setReviewed_by(user);
                    serviceLetterRequest.setReviewed_date(LocalDateTime.now());
                    ServiceLetterRequest updatedServiceRequest = serviceLetterRequestService.saveServiceLetterRequest(serviceLetterRequest);
                    commonResponseDTO.setData(updatedServiceRequest);
                    commonResponseDTO.setMessage("Service Letter status update successfully");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
                }
            } else {
                commonResponseDTO.setMessage("You have no permission to Review letter requests");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            commonResponseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getAllServieLetterRequests(HttpServletRequest request,
                                                                        @RequestParam(required = false) String search,
                                                                        @RequestParam(required = false) String status,
                                                                        @RequestParam(required = false, defaultValue = "0") Integer page) {

        CommonResponseDTO<Page<ServiceLetterRequest>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isServicePolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "SERVICE");
        try {
            if (isServicePolicyAvailable) {

                Page<ServiceLetterRequest> data = serviceLetterRequestService.getAllServiceLetterRequests(page, search, status);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully All Service letter requests Retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                commonResponseDTO.setMessage("You have no permission to get All Service letter requests");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get All Service letter requests");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/my")
    public ResponseEntity<CommonResponseDTO> getAllServieLetterRequests(HttpServletRequest request,
                                                                        @RequestParam(required = false, defaultValue = "0") Integer page) {

        CommonResponseDTO<Page<ServiceLetterRequest>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        try {
            Page<ServiceLetterRequest> data = serviceLetterRequestService.getSreviceLetterRequestByUser(page, user);
            commonResponseDTO.setData(data);
            commonResponseDTO.setMessage("Successfully My Service letter requests Retrieved");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get My Service letter requests");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }


}
