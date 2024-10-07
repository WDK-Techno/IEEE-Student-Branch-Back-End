package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AssignTaskDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.TaskCreateDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Task;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OUService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TaksService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/task")
public class TaskController {

    @Autowired
    private final TaksService taksService;


    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @Autowired
    private OUService ouService;

    public TaskController(TaksService taksService) {
        this.taksService = taksService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addTask(HttpServletRequest request, @RequestBody TaskCreateDTO task) {
        CommonResponseDTO<Task> commonResponseDTO = new CommonResponseDTO();
        boolean priorityValidation = task.getPriority().equals("HIGH") || task.getPriority().equals("NORMAL") || task.getPriority().equals("LOW");
        boolean typeValidation = task.getType().equals("EXCOM") || task.getType().equals("PROJECT");
        if (typeValidation && priorityValidation) {
            User user = (User) request.getAttribute("user");
            List<UserRoleDetails> userRoleDetailsExcom = userRoleDetailsServices.getuserRoleDetails(user, true, "EXCOM");
            boolean isTaskPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetailsExcom, "EXCOM_TASK");
            if (isTaskPolicyAvailable) {
                try {
                    Task newTask = Task.builder()
                            .start_date(task.getStart_date())
                            .end_date(task.getEnd_date())
                            .ou(ouService.getOUById(task.getOu_id()))
                            .task_name(task.getTask_name())
                            .type(task.getType())
                            .status("TODO")
                            .createdBy(user)
                            .build();
                    Task newtask = taksService.saveTask(newTask);
                    commonResponseDTO.setData(newtask);
                    commonResponseDTO.setMessage("Task Added Successfully");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
                } catch (Exception e) {
                    commonResponseDTO.setMessage("Task Added Failed");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

            } else {
                commonResponseDTO.setMessage("No Authority to Add Task");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage(typeValidation ? "Invalid Task Type" : "Invalid Task Priority");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{ouID}")
    public ResponseEntity<CommonResponseDTO> getTask(HttpServletRequest request, @PathVariable int ouID) {
        CommonResponseDTO<List<Task>> commonResponseDTO = new CommonResponseDTO();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isTaskPolicy = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "EXCOM_TASK");


        try {
            if (isTaskPolicy) {
                List<Task> Tasks = taksService.findAllTasksByOU(ouService.getOUById(ouID));
                commonResponseDTO.setData(Tasks);
                commonResponseDTO.setMessage("Task retrieved Successfully");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                List<Task> Tasks = taksService.findMyTasksByOU(user, ouService.getOUById(ouID));
                commonResponseDTO.setData(Tasks);
                commonResponseDTO.setMessage("Task retrieved Successfully");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("Task retrieved Failed");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/assign")
    public ResponseEntity<CommonResponseDTO> assignUser(HttpServletRequest request,
                                                        @RequestBody AssignTaskDTO assignTaskDTO
    ) {
        CommonResponseDTO<Task> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "EXCOM");
        boolean isTaskPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "EXCOM_TASK_ASSIGN");

        if (isTaskPolicyAvailable) {

            try {
                String message = taksService.assign(assignTaskDTO.getTaskId(), assignTaskDTO.getUsers());
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("No Authority to Assign Task");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }


}
