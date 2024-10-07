package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AssignPolicyDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.PolicyService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("api/v1/policy")
public class PolicyController {

    @Autowired
    private final PolicyService policyService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addPolicy(HttpServletRequest request, @RequestBody Policy policy) {
        CommonResponseDTO<Policy> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                Policy newPolicy = policyService.CreatePolicy(policy);
                commonResponseDTO.setData(newPolicy);
                commonResponseDTO.setMessage("Successfully added Policy");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } catch (Exception e) {
                boolean exist = policyService.alreadyExistsPolicyCode(policy);
                if (exist) {
                    commonResponseDTO.setMessage("Policy already exists");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.CONFLICT);
                } else {
                    commonResponseDTO.setMessage("failed to add Policy");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

            }
        } else {
            commonResponseDTO.setMessage("No Authority to Add Policy");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }


    @PostMapping("/assign")
    public ResponseEntity<CommonResponseDTO> assignPolicy(HttpServletRequest request,
                                                          @RequestBody AssignPolicyDTO assignPolicyDTO
    ) {
        CommonResponseDTO<Role> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                String message = policyService.assignPolicy(assignPolicyDTO.getRoleId(), assignPolicyDTO.getPolicies());
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            commonResponseDTO.setMessage("No Authority to Assign Policy");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getPolicy(HttpServletRequest request, @RequestParam(required = false) String search,
                                                       @RequestParam(required = false) String type,
                                                       @RequestParam(defaultValue = "0") int page) {
        CommonResponseDTO<Page<Policy>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            Page<Policy> data = policyService.getAllPolicy(page, search, type);
            commonResponseDTO.setData(data);
            commonResponseDTO.setMessage("Successfully retrieved Policies");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } else {
            commonResponseDTO.setMessage("No Authority to Get Policy");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }


    @PutMapping
    public ResponseEntity<CommonResponseDTO> updatePolicy(HttpServletRequest request, @RequestBody Policy policy) {
        CommonResponseDTO<Policy> commonResponseDTO = new CommonResponseDTO<>();

        if (Objects.nonNull(policy.getPolicyID()) && policy.getPolicyID() != 0) {
            User user = (User) request.getAttribute("user");
            List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
            boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
            if (isOtherPolicyAvailable) {

                try {
                    String message = policyService.updatePolicy(policy);
                    commonResponseDTO.setMessage(message);
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
                } catch (Exception e) {
                    commonResponseDTO.setMessage("Policy Edited failed");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }
            } else {
                commonResponseDTO.setMessage("No Authority to Edit Policy");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
            }

        } else {
            commonResponseDTO.setMessage("Policy id not found");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/deletePolicy/{policyID}")
    public ResponseEntity<CommonResponseDTO> deletePolicy(HttpServletRequest request, @PathVariable int policyID) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                String message = policyService.deletePolicy(policyID);
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("Failed to Delete Policy");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("No Authority to Delete Policy");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }
    }

}
