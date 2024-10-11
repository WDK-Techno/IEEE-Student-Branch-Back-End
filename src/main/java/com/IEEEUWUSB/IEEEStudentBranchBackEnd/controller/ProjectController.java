package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/project")
public class ProjectController {

//    @PostMapping
//    public ResponseEntity<CommonResponseDTO> addPolicy(HttpServletRequest request, @RequestBody Policy policy) {
//        CommonResponseDTO<Policy> commonResponseDTO = new CommonResponseDTO<>();
//        User user = (User) request.getAttribute("user");
//        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
//        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
//        if (isOtherPolicyAvailable) {
//            try {
//                Policy newPolicy = policyService.CreatePolicy(policy);
//                commonResponseDTO.setData(newPolicy);
//                commonResponseDTO.setMessage("Successfully added Policy");
//                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
//            } catch (Exception e) {
//                boolean exist = policyService.alreadyExistsPolicyCode(policy);
//                if (exist) {
//                    commonResponseDTO.setMessage("Policy already exists");
//                    commonResponseDTO.setError(e.getMessage());
//                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.CONFLICT);
//                } else {
//                    commonResponseDTO.setMessage("failed to add Policy");
//                    commonResponseDTO.setError(e.getMessage());
//                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
//                }
//
//            }
//        } else {
//            commonResponseDTO.setMessage("No Authority to Add Policy");
//            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
//        }
//
//
//    }
}
