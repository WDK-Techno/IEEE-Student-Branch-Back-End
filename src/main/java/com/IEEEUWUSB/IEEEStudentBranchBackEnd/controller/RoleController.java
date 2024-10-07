package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Role;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OUService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.RoleServices;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("api/v1/role")
public class RoleController {
    @Autowired
    private final RoleServices roleServices;

    @Autowired
    public final OUService ouService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @Autowired
    private UserService userService;

    public RoleController(RoleServices roleServices, OUService ouService) {
        this.roleServices = roleServices;
        this.ouService = ouService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addRole(HttpServletRequest request, @RequestBody Role role) {
        CommonResponseDTO<Role> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
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
        } else {
            commonResponseDTO.setMessage("No Authority to Add Role");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getRole(HttpServletRequest request, @RequestParam(required = false) String search,
                                                     @RequestParam(required = false) String type,
                                                     @RequestParam(defaultValue = "0") int page) {
        CommonResponseDTO<Page<Role>> commonResponseDTO = new CommonResponseDTO<>();

        try {
            Page<Role> data = roleServices.getAllRole(page, search, type);
            commonResponseDTO.setData(data);
            commonResponseDTO.setMessage("Successfully retrieved Roles");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("No Authority to Get Roles");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }

    }


    @PutMapping
    public ResponseEntity<CommonResponseDTO> updateRole(HttpServletRequest request, @RequestBody Role role) {
        CommonResponseDTO<Role> commonResponseDTO = new CommonResponseDTO<>();

        if (Objects.nonNull(role.getRoleID()) && role.getRoleID() != 0) {
            User user = (User) request.getAttribute("user");
            List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
            boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
            if (isOtherPolicyAvailable) {
                try {
                    String message = roleServices.updateRole(role);
                    commonResponseDTO.setData(role);
                    commonResponseDTO.setMessage(message);
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
                } catch (Exception e) {
                    commonResponseDTO.setMessage("Role Edited failed");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }
            } else {
                commonResponseDTO.setMessage("No Authority to update Roles");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
            }


        } else {
            commonResponseDTO.setMessage("Role id not found");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/deleteRole/{roleID}")
    public ResponseEntity<CommonResponseDTO> deletePolicy(HttpServletRequest request, @PathVariable int roleID) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                String message = roleServices.deleteRole(roleID);
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("Failed to Delete Role");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("No Authority to Delete Role");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/{roleID}/assign/{userId}/{ouId}")
    public ResponseEntity<CommonResponseDTO> assignRole(HttpServletRequest request, @PathVariable int roleID, @PathVariable int userId, @PathVariable int ouId) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetailsMain = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        List<UserRoleDetails> userRoleDetailsExom = userRoleDetailsServices.getuserRoleDetails(user, true, "EXCOM");
        boolean isExcomAssignAvailablemMain = userRoleDetailsServices.isPolicyAvailable(userRoleDetailsMain, "EXCOM_ASSIGN");
        boolean isExcomAssignAvailableExcom = userRoleDetailsServices.isPolicyAvailable(userRoleDetailsMain, "EXCOM_ASSIGN");
        if (isExcomAssignAvailableExcom || isExcomAssignAvailablemMain) {
            OU ou = ouService.getOUById(ouId);
            User subuser = userService.getUserId(userId);
            Role role = roleServices.getRoleById(roleID);
            UserRoleDetails subuserRoleDetails = userRoleDetailsServices.findByUserAndIsActiveAndType(subuser, true, "EXCOM");
            List<UserRoleDetails> otherUserRoleDetails = userRoleDetailsServices.getuserRoleDetailsExomByUserRole(role,true,"EXCOM");
            try {
                if (subuserRoleDetails != null) {
                    if (role == subuserRoleDetails.getRole()) {
                        commonResponseDTO.setMessage("Already Assigned Role");
                        commonResponseDTO.setError(null);
                        return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                    }
                    subuserRoleDetails.setEnd_date(LocalDateTime.now());
                    subuserRoleDetails.setIsActive(false);
                    userRoleDetailsServices.createUserRoleDetails(subuserRoleDetails);
                }

                if (otherUserRoleDetails != null && !otherUserRoleDetails.isEmpty()) {
                    otherUserRoleDetails.forEach(userRoleDetails -> {
                        userRoleDetails.setIsActive(false);
                        userRoleDetails.setEnd_date(LocalDateTime.now());
                        userRoleDetailsServices.createUserRoleDetails(userRoleDetails);
                    });
                }

                if (ou == null) {
                    commonResponseDTO.setMessage("OU not found");
                    commonResponseDTO.setError(null);
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

                if (subuser == null) {
                    commonResponseDTO.setMessage("User not found");
                    commonResponseDTO.setError(null);
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

                if (role == null) {
                    commonResponseDTO.setMessage("Role not found");
                    commonResponseDTO.setError(null);
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }


                var NewuserRoleDetails = UserRoleDetails.builder()
                        .user(subuser)
                        .role(role)
                        .isActive(true)
                        .type(role.getType())
                        .ou(ou)
                        .start_date(LocalDateTime.now()).build();

                userRoleDetailsServices.createUserRoleDetails(NewuserRoleDetails);
                commonResponseDTO.setMessage("Successfully Assign Role");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("Failed to Assign Role");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("No Authority to Assign Role");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }
    }
}
