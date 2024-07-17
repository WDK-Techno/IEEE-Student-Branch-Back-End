package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Role;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.PolicyService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.RoleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("api/v1/role")
public class RoleController {
    @Autowired
    private final RoleServices roleServices;


    public RoleController(RoleServices roleServices) {
        this.roleServices = roleServices;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addRole(@RequestBody Role role) {
        CommonResponseDTO<Role> commonResponseDTO = new CommonResponseDTO<>();
        try {
            Role newRole = roleServices.CreateRole(role);
            commonResponseDTO.setData(newRole);
            commonResponseDTO.setMessage("Successfully Role Added");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            boolean exist = roleServices.alreadyExistsRole(role);
            if (exist) {
                commonResponseDTO.setMessage("Role already exists");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CONFLICT);
            } else {
                commonResponseDTO.setMessage("failed to add Role");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        }

    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getPolicy(@RequestParam(required = false) String search,
                                                       @RequestParam(required = false) String type,
                                                       @RequestParam(defaultValue = "0") int page) {
        CommonResponseDTO<Page<Role>> commonResponseDTO = new CommonResponseDTO<>();
        Page<Role> data = roleServices.getAllRole(page,search,type);
        commonResponseDTO.setData(data);
        commonResponseDTO.setMessage("Successfully retrieved Roles");
        return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<CommonResponseDTO> updateRole(@RequestBody Role role) {
        CommonResponseDTO<Role> commonResponseDTO = new CommonResponseDTO<>();

        if (Objects.nonNull(role.getRoleID()) && role.getRoleID() != 0) {

            try{
                String message = roleServices.updateRole(role);
                commonResponseDTO.setData(role);
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            }catch (Exception e) {
                commonResponseDTO.setMessage("Role Edited failed");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("Role id not found");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
        }

    }
}
