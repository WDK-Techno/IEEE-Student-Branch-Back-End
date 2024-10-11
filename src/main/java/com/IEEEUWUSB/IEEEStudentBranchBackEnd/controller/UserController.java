package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.util.VarList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;


    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO request, Principal connectedUser) {
        try {
            String res = userService.changePassword(request, connectedUser);
            if (res.equals("00")) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else if (res.equals("11")) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Wrong password");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            } else if (res.equals("12")) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Passwords do not match");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Error");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

//    @PostMapping(value = "/saveUser")
//    public ResponseEntity saveUser(@RequestBody UserDTO userDTO) {
//        try {
//            String res = userService.saveUser(userDTO);
//            if (res.equals("00")) {
//                responseDTO.setCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Success");
//                responseDTO.setContent(userDTO);
//                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
//
//            } else if (res.equals("06")) {
//                responseDTO.setCode(VarList.RSP_DUPLICATED);
//                responseDTO.setMessage("User Already Registered");
//                responseDTO.setContent(userDTO);
//                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
//            } else {
//                responseDTO.setCode(VarList.RSP_FAIL);
//                responseDTO.setMessage("Error");
//                responseDTO.setContent(null);
//                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception ex) {
//            responseDTO.setCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(ex.getMessage());
//            responseDTO.setContent(null);
//            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PutMapping(value = "/updateUser")
//    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
//        try {
//            String res = userService.updateUser(userDTO);
//            if (res.equals("00")) {
//                responseDTO.setCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Success");
//                responseDTO.setContent(userDTO);
//                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
//            } else if (res.equals("01")) {
//                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
//                responseDTO.setMessage("Not A Registered User");
//                responseDTO.setContent(userDTO);
//                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//            } else {
//                responseDTO.setCode(VarList.RSP_FAIL);
//                responseDTO.setMessage("Error");
//                responseDTO.setContent(null);
//                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//            }
//
//        } catch (Exception ex) {
//            responseDTO.setCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(ex.getMessage());
//            responseDTO.setContent(null);
//            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

//    @GetMapping(value = "/getAllUser")
//    public ResponseEntity getAllUser() {
//        try {
//            List<UserDTO> userDTOList = userService.getAllUser();
//            responseDTO.setCode(VarList.RSP_SUCCESS);
//            responseDTO.setMessage("Success");
//            responseDTO.setContent(userDTOList);
//            return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
//
//        } catch (Exception ex) {
//            responseDTO.setCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(ex.getMessage());
//            responseDTO.setContent(null);
//            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @GetMapping(value = "/searchUser/{userID}")
//    public ResponseEntity searchUser(@PathVariable int userID) {
//        try {
//            UserDTO userDTO = userService.searchUser(userID);
//            if (userDTO != null) {
//                responseDTO.setCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Success");
//                responseDTO.setContent(userDTO);
//                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
//            } else {
//                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
//                responseDTO.setMessage("No User Available for this UserID ");
//                responseDTO.setContent(null);
//                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception ex) {
//            responseDTO.setCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(ex.getMessage());
//            responseDTO.setContent(null);
//            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @DeleteMapping(value = "/deleteUser/{userID}")
//    public ResponseEntity deleteUser(@PathVariable int userID) {
//        try {
//            String res = userService.deleteUser(userID);
//            if (res.equals("00")) {
//                responseDTO.setCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Success");
//                responseDTO.setContent(null);
//                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
//            } else {
//                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
//                responseDTO.setMessage("No User Available for this UserID ");
//                responseDTO.setContent(null);
//                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception ex) {
//            responseDTO.setCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(ex.getMessage());
//            responseDTO.setContent(null);
//            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }


    @GetMapping(value = "/currentuser")
    public ResponseEntity<CommonResponseDTO<List<UserRoleDetails>>> getCurrentUser(HttpServletRequest request) {
        CommonResponseDTO<List<UserRoleDetails>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        if (user == null) {
            commonResponseDTO.setError("User not found");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }

        try {
            List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetailsExom(user, true, "MAIN", "EXCOM");
            if (userRoleDetails != null) {

                commonResponseDTO.setData(userRoleDetails);
                commonResponseDTO.setMessage("User roles received");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                commonResponseDTO.setError("No user roles found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping
    public ResponseEntity<CommonResponseDTO> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page) {
        CommonResponseDTO<Page<User>> commonResponseDTO = new CommonResponseDTO<>();
        try {
            Page<User> data = userService.getAllusers(page, search);
            commonResponseDTO.setData(data);
            commonResponseDTO.setMessage("Successfully retrieved Policies");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/updateUser")
    public ResponseEntity<CommonResponseDTO> updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        CommonResponseDTO<User> commonResponseDTO = new CommonResponseDTO<>();
        try {

            User user = (User) request.getAttribute("user");
            if (user == null) {
                commonResponseDTO.setMessage("User not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }

            user.setEmail(userDTO.getEmail());
            user.setIeee_email(userDTO.getIeee_email());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setIeee_membership_number(userDTO.getIeee_membership_number());
            user.setNameWithInitial(userDTO.getNameWithInitial());
            user.setContactNo(userDTO.getContactNo());
            user.setBio(userDTO.getBio());
            user.setProfilePic(userDTO.getProfilePic());
            user.setFbURL(userDTO.getFbURL());
            user.setLinkedInURL(userDTO.getLinkedInURL());
            user.setLocation(userDTO.getLocation());

            User updatedUser = userService.saveUser(user);
            commonResponseDTO.setData(updatedUser);
            commonResponseDTO.setMessage("User updated successfully");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to update user");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}