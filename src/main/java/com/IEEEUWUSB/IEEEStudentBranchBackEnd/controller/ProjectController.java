package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.ProjectDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.StatusCountDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OUService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.ProjectService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TermYearService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


    @PutMapping("/{project_id}")
    public ResponseEntity<CommonResponseDTO> editProject(HttpServletRequest request, @RequestBody ProjectDTO projectDTO,@PathVariable int project_id) {
        CommonResponseDTO<Project> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isProjectolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");
        boolean statusValidation = projectDTO.getStatus().equals("TODO") || projectDTO.getStatus().equals("PROGRESS") || projectDTO.getStatus().equals("COMPLETE");
        if (isProjectolicyAvailable && statusValidation) {
            try {
                Project project = projectService.getProjectById(project_id);

                if(project == null){
                    commonResponseDTO.setMessage("Project not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

                Set<OU> existingOus = new HashSet<>(project.getOus());

                Set<Integer> ProjectIdSet = new HashSet<>(Arrays.asList(projectDTO.getOu_id()));

                for (OU Ou : existingOus) {
                    if (!ProjectIdSet.contains(Ou.getOuID())) {
                        project.removeOU(Ou);
                    }
                }

                for (Integer OuId : projectDTO.getOu_id()) {
                    OU ou = ouService.getOUById(OuId);
                    if (ou != null && !project.getOus().contains(ou)) {
                        project.addOU(ou);
                    }
                }

                project.setProjectLogo(projectDTO.getProject_logo());
                project.setProjectName(projectDTO.getProject_name());
                project.setDescription(projectDTO.getDescription());
                project.setEndDate(projectDTO.getEnd_date());
                project.setStartDate(projectDTO.getStart_date());
                project.setStatus(projectDTO.getStatus());


                Project SavedProject = projectService.saveProject(project);
                commonResponseDTO.setData(SavedProject);
                commonResponseDTO.setMessage("Successfully edit Project");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to edit Project");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage(!isProjectolicyAvailable ? "No Authority to Add Project" : "Invalid status type");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getProject(HttpServletRequest request,
                                                        @RequestParam(required = false) Integer ouid,
                                                        @RequestParam(required = false) Integer termYearId,
                                                        @RequestParam(required = false) String search,
                                                        @RequestParam(required = false) String status,
                                                        @RequestParam(required = false, defaultValue = "0") Integer page) {
        CommonResponseDTO<Page<Project>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isProjectPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");

        try {
            OU ou = (ouid != null) ? ouService.getOUById(ouid) : null;
            TermYear termyear = (termYearId != null) ? termYearService.findByid(termYearId) : null;

            Page<Project> data;
            if (isProjectPolicyAvailable) {
                data = projectService.getAllProject(page, search, status, ou, termyear);
            } else {
                data = projectService.getAllProjectByuser(page, search, status, user);
            }

            commonResponseDTO.setData(data);
            commonResponseDTO.setMessage("Successfully Project Retrieved");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get Project");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/count")
    public ResponseEntity<CommonResponseDTO> getProjectCount(HttpServletRequest request,
                                                        @RequestParam(required = false) Integer ouid,
                                                        @RequestParam(required = false) Integer termYearId,
                                                        @RequestParam(required = false) String search) {
        CommonResponseDTO<StatusCountDTO> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isProjectPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");

        try {
            OU ou = (ouid != null) ? ouService.getOUById(ouid) : null;
            TermYear termyear = (termYearId != null) ? termYearService.findByid(termYearId) : null;

            StatusCountDTO data;
            if (isProjectPolicyAvailable) {
                long todo = projectService.countProjectsByCriteria(search, "TODO", ou, termyear);
                long progress = projectService.countProjectsByCriteria(search, "PROGRESS", ou, termyear);
                long complete = projectService.countProjectsByCriteria(search, "COMPLETE", ou, termyear);
                data = new StatusCountDTO(todo, progress, complete);

            } else {
                long todo = projectService.countProjectsByUser(search, "TODO", user, user);
                long progress = projectService.countProjectsByUser(search, "PROGRESS", user, user);
                long complete = projectService.countProjectsByUser(search, "COMPLETE", user, user);
                data = new StatusCountDTO(todo, progress, complete);
            }

            commonResponseDTO.setData(data);
            commonResponseDTO.setMessage("Successfully Project counts Retrieved");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get Project counts");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{project_id}")
    public ResponseEntity<CommonResponseDTO> deleteProject(HttpServletRequest request,
                                                           @PathVariable int project_id) {
        CommonResponseDTO<String> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isProjectPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");

        if (isProjectPolicyAvailable) {
            try {

                Project project = projectService.getProjectById(project_id);
                if(project == null){
                    commonResponseDTO.setMessage("Project not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }
                projectService.deleteProject(project);
                commonResponseDTO.setData(null);
                commonResponseDTO.setMessage("Successfully deleted Project");
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
