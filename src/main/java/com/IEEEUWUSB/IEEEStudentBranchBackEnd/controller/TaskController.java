package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AssignTaskDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.TaskCreateDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Task;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OUService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TaksService;
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
    private final TaksService taskService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @Autowired
    private OUService ouService;

    public TaskController(TaksService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addTask(HttpServletRequest request, @RequestBody TaskCreateDTO task) {
        CommonResponseDTO<Task> commonResponseDTO = new CommonResponseDTO<>();
        boolean priorityValidation = task.getPriority().equals("HIGH") || task.getPriority().equals("MEDIUM") || task.getPriority().equals("LOW");
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

                    Task newTaskSaved = taskService.saveTask(newTask);
                    commonResponseDTO.setData(newTaskSaved);
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
        CommonResponseDTO<List<Task>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isTaskPolicy = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "EXCOM_TASK");

        try {
            List<Task> tasks;
            if (isTaskPolicy) {
                tasks = taskService.findAllTasksByOU(ouService.getOUById(ouID));
            } else {
                tasks = taskService.findMyTasksByOU(user, ouService.getOUById(ouID));
            }
            commonResponseDTO.setData(tasks);
            commonResponseDTO.setMessage("Task retrieved Successfully");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Task retrieval Failed");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<CommonResponseDTO> assignUser(HttpServletRequest request, @RequestBody AssignTaskDTO assignTaskDTO) {
        CommonResponseDTO<Task> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "EXCOM");
        boolean isTaskPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "EXCOM_TASK_ASSIGN");

        if (isTaskPolicyAvailable) {
            try {
                String message = taskService.assign(assignTaskDTO.getTaskId(), assignTaskDTO.getUsers());
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

    @PutMapping("/status/{taskId}")
    public ResponseEntity<CommonResponseDTO> setStatus(HttpServletRequest request, @PathVariable int taskId, @RequestParam(required = true) String status) {
        CommonResponseDTO<Task> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");

        // Validate the status
        boolean statusValidation = status.equals("TODO") || status.equals("PROGRESS") || status.equals("COMPLETE");

        if (!statusValidation) {
            commonResponseDTO.setMessage("Invalid Task Status");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }

        try {
            // Get the task object by the ID
            Task task = taskService.getTaskById(taskId);
            task.setStatus(status);

            // Save the task
            Task savedTask = taskService.saveTask(task);
            commonResponseDTO.setData(savedTask);
            commonResponseDTO.setMessage("Task status updated successfully");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Task status update failed");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }
}
