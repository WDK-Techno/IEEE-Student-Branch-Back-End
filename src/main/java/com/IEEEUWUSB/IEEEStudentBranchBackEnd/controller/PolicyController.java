package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AssignPolicyDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Role;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.AcademicYearService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("api/v1/policy")
public class PolicyController {

    @Autowired
    private final PolicyService policyService;


    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addPolicy(@RequestBody Policy policy) {
        CommonResponseDTO<Policy> commonResponseDTO = new CommonResponseDTO<>();
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

    }


    @PostMapping("/assign")
    public ResponseEntity<CommonResponseDTO> assignPolicy(
            @RequestBody  AssignPolicyDTO assignPolicyDTO
    ) {
        CommonResponseDTO<Role> commonResponseDTO = new CommonResponseDTO<>();
        String message = policyService.assignPolicy(assignPolicyDTO.getRoleId(), assignPolicyDTO.getPolicies());
        commonResponseDTO.setMessage(message);
        return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getPolicy(@RequestParam(required = false) String search,
                                                             @RequestParam(required = false) String type,
                                                             @RequestParam(defaultValue = "0") int page) {
        CommonResponseDTO<Page<Policy>> commonResponseDTO = new CommonResponseDTO<>();
        Page<Policy> data = policyService.getAllPolicy(page,search,type);
        commonResponseDTO.setData(data);
        commonResponseDTO.setMessage("Successfully retrieved Policies");
        return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<CommonResponseDTO> updatePolicy(@RequestBody Policy policy) {
        CommonResponseDTO<Policy> commonResponseDTO = new CommonResponseDTO<>();

        if (Objects.nonNull(policy.getPolicyID()) && policy.getPolicyID() != 0) {

            try{
                String message = policyService.updatePolicy(policy);
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            }catch (Exception e) {
                commonResponseDTO.setMessage("Policy Edited failed");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("Policy id not found");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
        }

    }

}
