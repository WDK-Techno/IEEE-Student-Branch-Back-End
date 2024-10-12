package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.ProjectDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OUService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.ProjectService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TermYearService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("api/v1/project")
public class ProjectController {

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @Autowired
    private ProjectService projectService;

    @Autowired
    public final OUService ouService;

    @Autowired
    private TermYearService termYearService;

    public ProjectController(OUService ouService) {
        this.ouService = ouService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addProject(HttpServletRequest request, @RequestBody ProjectDTO projectDTO) {
        CommonResponseDTO<Project> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isProjectolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");
        if (isProjectolicyAvailable) {
            try {
                TermYear ActiveTermYear = termYearService.findByActiveStatus();

                Project newproject = Project.builder()
                        .projectName(projectDTO.getProject_name())
                        .description(projectDTO.getDescription())
                        .projectLogo(projectDTO.getProject_logo())
                        .status("TODO")
                        .startDate(projectDTO.getStart_date())
                        .endDate(projectDTO.getEnd_date())
                        .createdBy(user)
                        .termyear(ActiveTermYear)
                        .build();
                for (Integer OuId : projectDTO.getOu_id()) {
                    OU ou = ouService.getOUById(OuId);
                    if (ou != null) {
                        newproject.addOU(ou);
                    }
                }
                Project SavedProject = projectService.saveProject(newproject);
                commonResponseDTO.setData(SavedProject);
                commonResponseDTO.setMessage("Successfully added Project");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } catch (Exception e) {

                    commonResponseDTO.setMessage("failed to add Project");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("No Authority to Add Project");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }
}
