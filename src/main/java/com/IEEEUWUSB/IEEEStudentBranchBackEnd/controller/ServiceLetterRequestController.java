package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.BestVolunteerDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.ServiceLetterReqDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.StatusCountDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.ServiceLetterRequestService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TaksService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private TaksService taksService;

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addRequest(HttpServletRequest request, @RequestBody ServiceLetterReqDTO service) {
        CommonResponseDTO<ServiceLetterRequest> commonResponseDTO = new CommonResponseDTO<>();
        if (service.getType_excom() == null) {
            service.setType_excom(false);
        }
        if (service.getType_project() == null) {
            service.setType_project(false);
        }
        if (service.getType_other() == null) {
            service.setType_other(false);
        }

        try {
            User user = (User) request.getAttribute("user");
            ServiceLetterRequest newRequest = ServiceLetterRequest.builder()

                    .remarks(service.getRemarks())
                    .email(service.getEmail())
                    .status("TODO")
                    .type_excom(service.getType_excom())
                    .type_project(service.getType_project())
                    .type_other(service.getType_other())
                    .request_date(LocalDateTime.now())
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

    @DeleteMapping("/{request_id}")
    public ResponseEntity<CommonResponseDTO> deleteServieLetterRequest(HttpServletRequest request, @PathVariable int request_id) {
        CommonResponseDTO<ServiceLetterRequest> commonResponseDTO = new CommonResponseDTO<>();
        try {
            User user = (User) request.getAttribute("user");

            ServiceLetterRequest serviceLetterRequest = serviceLetterRequestService.getServiceLetterRequestById(request_id);

            if (serviceLetterRequest == null) {
                commonResponseDTO.setMessage("Service Letter Request not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            } else if (!serviceLetterRequest.getUser().equals(user)) {
                commonResponseDTO.setMessage("You have no permission to delete service letter requests");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            } else if (serviceLetterRequest.getStatus().equals("REVIEWED")) {
                commonResponseDTO.setMessage("Cannot Delete Since Service Letter Request has been reviewed");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

            serviceLetterRequestService.deleteServiceLetterRequestById(request_id);
            commonResponseDTO.setMessage("Service Letter request deleted successfully");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);


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

    @GetMapping("activities/{user_id}")
    public ResponseEntity<CommonResponseDTO> getRolesDetailsByUserWithUserID(HttpServletRequest request, @PathVariable Integer user_id,
                                                                             @RequestParam(required = true) String type,
                                                                             @RequestParam(required = false, defaultValue = "0") Integer page) {
        return getRolesDetailsByUser(request, user_id, type, page);

    }

    @GetMapping("activities")
    public ResponseEntity<CommonResponseDTO> getRolesDetailsByUserWithOutUserID(HttpServletRequest request,
                                                                                @RequestParam(required = true) String type,
                                                                                @RequestParam(required = false, defaultValue = "0") Integer page) {
        return getRolesDetailsByUser(request, null, type, page);

    }

    private ResponseEntity<CommonResponseDTO> getRolesDetailsByUser(HttpServletRequest request, Integer user_id,
                                                                    String type,
                                                                    Integer page) {

        CommonResponseDTO<Page<UserRoleDetails>> commonResponseDTO = new CommonResponseDTO<>();
        User thisuser = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(thisuser, true, "MAIN");
        boolean isServicePolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "SERVICE");
        User targetUser;
        if (user_id != null) {
            targetUser = userService.getUserId(user_id);
        } else {
            targetUser = null;
        }

        try {
            if (isServicePolicyAvailable && targetUser != null && type != null) {

                Page<UserRoleDetails> data = userRoleDetailsServices.getUserRoleDetailsByUserandType(page, targetUser, type);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully User Role Details Retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else if (targetUser == null && type != null) {
                Page<UserRoleDetails> data = userRoleDetailsServices.getUserRoleDetailsByUserandType(page, thisuser, type);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully User Role Details Retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                commonResponseDTO.setMessage("You have no permission to get other users details");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get User Role details");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("activities/tasks/{user_id}")
    public ResponseEntity<CommonResponseDTO> getRolesDetailsByUserWithUserID(HttpServletRequest request, @PathVariable Integer user_id,
                                                                             @RequestParam(required = false) String search,
                                                                             @RequestParam(required = false) String priority,
                                                                             @RequestParam(required = false) String status,
                                                                             @RequestParam(required = false, defaultValue = "0") Integer page) {
        return getTasksByUser(request, user_id, search, priority, status, page);

    }

    @GetMapping("activities/tasks")
    public ResponseEntity<CommonResponseDTO> getRolesDetailsByUserWithOutUserID(HttpServletRequest request,
                                                                                @RequestParam(required = false) String search,
                                                                                @RequestParam(required = false) String priority,
                                                                                @RequestParam(required = false) String status,
                                                                                @RequestParam(required = false, defaultValue = "0") Integer page) {
        return getTasksByUser(request, null, search, priority, status, page);

    }

    private ResponseEntity<CommonResponseDTO> getTasksByUser(HttpServletRequest request, Integer user_id,
                                                             String search,
                                                             String priority,
                                                             String status,
                                                             Integer page) {

        CommonResponseDTO<Page<Task>> commonResponseDTO = new CommonResponseDTO<>();
        User thisuser = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(thisuser, true, "MAIN");
        boolean isServicePolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "SERVICE");

        User targetUser;
        if (user_id != null) {
            targetUser = userService.getUserId(user_id);
        } else {
            targetUser = null;
        }

        try {
            if (isServicePolicyAvailable && targetUser != null) {

                Page<Task> data = taksService.findAllTaskByUser(page, search, priority, status, targetUser);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully Target User's Tasks Retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else if (targetUser == null) {
                Page<Task> data = taksService.findAllTaskByUser(page, search, priority, status, thisuser);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully this User's Tasks Retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                commonResponseDTO.setMessage("You have no permission to get other users tasks");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get tasks details");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("activities/tasks/count/{user_id}")
    public ResponseEntity<CommonResponseDTO> getAllTaskCountByUserWithUserID(HttpServletRequest request, @PathVariable Integer user_id,
                                                                             @RequestParam(required = false) String priority) {
        return getAllTaskCountByUser(request, user_id, priority);

    }

    @GetMapping("activities/tasks/count")
    public ResponseEntity<CommonResponseDTO> getAllTaskCountByUserWithOutUserID(HttpServletRequest request,
                                                                                @RequestParam(required = false) String priority) {
        return getAllTaskCountByUser(request, null, priority);

    }

    private ResponseEntity<CommonResponseDTO> getAllTaskCountByUser(HttpServletRequest request, Integer user_id,
                                                                    String priority) {

        CommonResponseDTO<StatusCountDTO> commonResponseDTO = new CommonResponseDTO<>();
        User thisuser = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(thisuser, true, "MAIN");
        boolean isServicePolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "SERVICE");

        User targetUser;
        if (user_id != null) {
            targetUser = userService.getUserId(user_id);
        } else {
            targetUser = null;
        }

        try {
            if (isServicePolicyAvailable && targetUser != null) {
                StatusCountDTO data = null;
                long todo = taksService.countAllTasksByUser(targetUser, "TODO", priority);
                long progress = taksService.countAllTasksByUser(targetUser, "PROGRESS", priority);
                long complete = taksService.countAllTasksByUser(targetUser, "COMPLETE", priority);
                data = new StatusCountDTO(todo, progress, complete);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully Target User's Tasks count Retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else if (targetUser == null) {
                StatusCountDTO data = null;
                long todo = taksService.countAllTasksByUser(thisuser, "TODO", priority);
                long progress = taksService.countAllTasksByUser(thisuser, "PROGRESS", priority);
                long complete = taksService.countAllTasksByUser(thisuser, "COMPLETE", priority);
                data = new StatusCountDTO(todo, progress, complete);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully Your Tasks count Retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                commonResponseDTO.setMessage("You have no permission to get other users task count");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get tasks Count");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("volunteer/best")
    public ResponseEntity<CommonResponseDTO> getUsersOrderingByTheirCompletedTaskCount(HttpServletRequest request,
                                                                                       @RequestParam(required = false, defaultValue = "0") Integer page) {

        CommonResponseDTO<Page<BestVolunteerDTO>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isServiceVolunteerPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "SERVICE_VOLUNTEER");
        try {
            if (isServiceVolunteerPolicyAvailable) {

                Page<BestVolunteerDTO> data = taksService.bestVolunteersByCompletedTaskCount(page);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully Ordered Users from Completed Task Count Retrieved");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                commonResponseDTO.setMessage("You have no permission to get Ordered Users from Completed Task Count");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get Ordered Users from Completed Task Count");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }


}
