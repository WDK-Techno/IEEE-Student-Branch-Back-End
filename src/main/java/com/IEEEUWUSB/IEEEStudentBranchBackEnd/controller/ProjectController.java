package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/project")
public class ProjectController {

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    public final OUService ouService;

    @Autowired
    private final RoleServices roleServices;

    @Autowired
    private TermYearService termYearService;

    public ProjectController(OUService ouService, RoleServices roleServices) {
        this.ouService = ouService;
        this.roleServices = roleServices;
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
    public ResponseEntity<CommonResponseDTO> editProject(HttpServletRequest request, @RequestBody ProjectDTO projectDTO, @PathVariable int project_id) {
        CommonResponseDTO<Project> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isProjectolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");
        boolean statusValidation = projectDTO.getStatus().equals("TODO") || projectDTO.getStatus().equals("PROGRESS") || projectDTO.getStatus().equals("COMPLETE");
        if (isProjectolicyAvailable && statusValidation) {
            try {
                Project project = projectService.getProjectById(project_id);

                if (project == null) {
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
            TermYear termyear = (termYearId != null) ? termYearService.findByid(termYearId) : termYearService.findByActiveStatus();

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
                long todo = projectService.countProjectsByUser("TODO", user, user);
                long progress = projectService.countProjectsByUser("PROGRESS", user, user);
                long complete = projectService.countProjectsByUser( "COMPLETE", user, user);
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


    @GetMapping("/{project_id}")
    public ResponseEntity<CommonResponseDTO> getProjectbyId(HttpServletRequest request, @PathVariable int project_id) {
        CommonResponseDTO<ProjectUserPermissionDTO> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");

        try {
            Project project = projectService.getProjectById(project_id);
            if (project == null) {
                commonResponseDTO.setMessage("Project not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
            List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetailsByProject(user, true, "PROJECT", project_id);
            List<UserRoleDetails> OthersuserRoleDetails = userRoleDetailsServices.getAlluserRoleDetailsByProject(true, "PROJECT", project_id);
            ProjectUserPermissionDTO data= new ProjectUserPermissionDTO(project,userRoleDetails,OthersuserRoleDetails);
            commonResponseDTO.setData(data);
            commonResponseDTO.setMessage("Project retrieved successfully");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to get Project");
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
                if (project == null) {
                    commonResponseDTO.setMessage("Project not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }
                projectService.deleteProject(project);
                commonResponseDTO.setData(null);
                commonResponseDTO.setMessage("Successfully deleted Project");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
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


    @PostMapping(value = "/{roleID}/assign/{userId}/{projectId}")
    public ResponseEntity<CommonResponseDTO> assignRole(HttpServletRequest request, @PathVariable int roleID, @PathVariable int userId, @PathVariable int projectId) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetailsMain = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        List<UserRoleDetails> userRoleDetailsProject = userRoleDetailsServices.getuserRoleDetailsByProject(user, true, "PROJECT", projectId);
        boolean isExcomAssignAvailablemMain = userRoleDetailsServices.isPolicyAvailable(userRoleDetailsMain, "PROJECT");
        boolean isExcomAssignAvailableProject = userRoleDetailsServices.isPolicyAvailable(userRoleDetailsProject, "PROJECT_ASSIGN");
        if (isExcomAssignAvailableProject || isExcomAssignAvailablemMain) {
            try {
                Project project = projectService.getProjectById(projectId);
                TermYear activeTermYear = termYearService.findByActiveStatus();
                User subuser = userService.getUserId(userId);
                Role role = roleServices.getRoleById(roleID);

                if (project == null) {
                    commonResponseDTO.setMessage("Project not found");
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

                if (activeTermYear == null) {
                    commonResponseDTO.setMessage("There is no active term year");
                    commonResponseDTO.setError(null);
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

                UserRoleDetails subuserRoleDetailsProject = userRoleDetailsServices.getuserRoleDetailsByProjectSingleObject(subuser, true, "PROJECT", projectId);
                List<UserRoleDetails> otherUserRoleDetailsInsameProject = userRoleDetailsServices.getuserroledetailsbyroleandproject(role,true, project);
                if (subuserRoleDetailsProject != null) {
                    if (role == subuserRoleDetailsProject.getRole()) {
                        commonResponseDTO.setMessage("Already Assigned Role");
                        commonResponseDTO.setError(null);
                        return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                    }
                    subuserRoleDetailsProject.setEnd_date(LocalDateTime.now());
                    subuserRoleDetailsProject.setIsActive(false);
                    project.removeuser(subuser);
                    projectService.saveProject(project);
                    userRoleDetailsServices.createUserRoleDetails(subuserRoleDetailsProject);
                }

                if (otherUserRoleDetailsInsameProject != null && !otherUserRoleDetailsInsameProject.isEmpty()) {
                    otherUserRoleDetailsInsameProject.forEach(userRoleDetails -> {
                        project.removeuser(userRoleDetails.getUser());
                        userRoleDetails.setIsActive(false);
                        userRoleDetails.setEnd_date(LocalDateTime.now());
                        userRoleDetailsServices.createUserRoleDetails(userRoleDetails);
                    });
                }

                var NewuserRoleDetails = UserRoleDetails.builder()
                        .user(subuser)
                        .role(role)
                        .isActive(true)
                        .type(role.getType())
                        .termyear(activeTermYear)
                        .project(project)
                        .start_date(LocalDateTime.now()).build();
                project.adduser(subuser);
                projectService.saveProject(project);
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

    @PutMapping("duration/{project_id}")
    public ResponseEntity<CommonResponseDTO> editProjectDuration(HttpServletRequest request, @RequestBody ProjectDurationDTO projectDurationDTO, @PathVariable int project_id) {
        CommonResponseDTO<Project> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isProjectolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");
        if (isProjectolicyAvailable) {
            try {
                Project project = projectService.getProjectById(project_id);

                if (project == null) {
                    commonResponseDTO.setMessage("Project not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

                project.setEndDate(projectDurationDTO.getEnd_date());
                project.setStartDate(projectDurationDTO.getStart_date());
                Project SavedProject = projectService.saveProject(project);
                commonResponseDTO.setData(SavedProject);
                commonResponseDTO.setMessage("Successfully edit Project");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
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

}
