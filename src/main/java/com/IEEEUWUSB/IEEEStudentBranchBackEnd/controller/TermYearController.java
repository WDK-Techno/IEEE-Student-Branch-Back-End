package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommentDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TermYearService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/termyear")
public class TermYearController {
    @Autowired
    private TermYearService termYearService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @PostMapping("")
    public ResponseEntity<CommonResponseDTO> createTermYear(HttpServletRequest request,
                                                           @RequestBody TermYear termyear
    ) {
        termyear.setStatus("DEACTIVE");
        CommonResponseDTO<TermYear> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");

        if (isOtherPolicyAvailable) {
            try {
                TermYear newTermYear = termYearService.saveTermYear(termyear);
                commonResponseDTO.setData(newTermYear);
                commonResponseDTO.setMessage("Successfully added TermYear");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } catch (Exception e) {

                    commonResponseDTO.setMessage("failed to add TermYear");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            commonResponseDTO.setMessage("No Authority to Add Term Year");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }

    }


    @GetMapping("")
    public ResponseEntity<CommonResponseDTO> getAllTermYear(HttpServletRequest request
    ) {
        CommonResponseDTO<List<TermYear>> commonResponseDTO = new CommonResponseDTO<>();
            try {
                List<TermYear> TermYears = termYearService.getAllTermYear();
                commonResponseDTO.setData(TermYears);
                commonResponseDTO.setMessage("Successfully term year retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("failed to Retrieve TermYear");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

    }


    @PutMapping("")
    public ResponseEntity<CommonResponseDTO> editTermYear(HttpServletRequest request,
                                                            @RequestBody TermYear termyear
    ) {
        CommonResponseDTO<TermYear> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                TermYear activeTermYear = termYearService.findByActiveStatus();

                if (activeTermYear != null && termyear.getStatus().equals("ACTIVE")) {
                    commonResponseDTO.setError("Already Active Term Year");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

                TermYear newTermYear = termYearService.saveTermYear(termyear);
                commonResponseDTO.setData(newTermYear);
                commonResponseDTO.setMessage("Successfully added Policy");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("failed to Edit TermYear");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        }else {
            commonResponseDTO.setMessage("No Authority to Add Term Year");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }
}
