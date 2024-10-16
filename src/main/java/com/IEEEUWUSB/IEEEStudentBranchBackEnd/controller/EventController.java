package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Events;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.EventService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.ProjectService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @PostMapping("/{project_id}")
    public ResponseEntity<CommonResponseDTO> addEvent(HttpServletRequest request, @RequestBody Events event, @PathVariable int project_id) {
        CommonResponseDTO<Events> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        boolean IsValidStatusType = event.getStatus().equals("ACTIVE") || event.getStatus().equals("INACTIVE");
        if (!IsValidStatusType) {
            commonResponseDTO.setMessage("Invalid status");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
        try {
            Project project = projectService.getProjectById(project_id);
            if (project == null) {
                commonResponseDTO.setMessage("Project not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }
            List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
            boolean isProjectolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");
            List<UserRoleDetails> userRoleDetailsProject = userRoleDetailsServices.getuserRoleDetailsByProject(user, true, "PROJECT", project_id);
            boolean isEventAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetailsProject, "PROJECT_EVENT");
            if (isEventAvailable || isProjectolicyAvailable) {
                event.setProject(project);
                Events savedEvent = eventService.saveEvent(event);
                commonResponseDTO.setData(savedEvent);
                commonResponseDTO.setMessage("Successfully added Event");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } else {
                commonResponseDTO.setMessage("No Authority to create event");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("failed to add Event");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }


    }


    @PutMapping("/{project_id}/{event_id}")
    public ResponseEntity<CommonResponseDTO> editEvent(HttpServletRequest request, @RequestBody Events event, @PathVariable int event_id, @PathVariable int project_id) {
        CommonResponseDTO<Events> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        boolean IsValidStatusType = event.getStatus().equals("ACTIVE") || event.getStatus().equals("INACTIVE");
        if (!IsValidStatusType) {
            commonResponseDTO.setMessage("Invalid status");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
        try {
            Project project = projectService.getProjectById(project_id);
            if (project == null) {
                commonResponseDTO.setMessage("Project not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }
            List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
            boolean isProjectolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");
            List<UserRoleDetails> userRoleDetailsProject = userRoleDetailsServices.getuserRoleDetailsByProject(user, true, "PROJECT", project_id);
            boolean isEventAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetailsProject, "PROJECT_EVENT");
            if (isEventAvailable || isProjectolicyAvailable) {
                Events getevent = eventService.getEventById(event_id);
                if (getevent == null) {
                    commonResponseDTO.setMessage("Event not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }

                getevent.setEventName(event.getEventName());
                getevent.setEventLink(event.getEventLink());
                getevent.setStatus(event.getStatus());
                getevent.setDate(event.getDate());
                getevent.setVenue(event.getVenue());
                getevent.setImage(event.getImage());
                getevent.setDecsription(event.getDecsription());
                Events savedEvent = eventService.saveEvent(getevent);
                commonResponseDTO.setData(savedEvent);
                commonResponseDTO.setMessage("Successfully edit the Event");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } else {
                commonResponseDTO.setMessage("No Authority to create event");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("failed to edit Event");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/{project_id}")
    public ResponseEntity<CommonResponseDTO> getEventByProject(
            HttpServletRequest request,
            @PathVariable int project_id,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        CommonResponseDTO<Page<Events>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        try {
            Project project = projectService.getProjectById(project_id);
            if (project == null) {
                commonResponseDTO.setMessage("Project not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }
            Page<Events> savedEvent = eventService.getAllEventByProject(page, project);
            commonResponseDTO.setData(savedEvent);
            commonResponseDTO.setMessage("Successfully  retrived Event");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            commonResponseDTO.setMessage("failed to add Event");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }


    }


    @GetMapping("")
    public ResponseEntity<CommonResponseDTO> getEvents(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        CommonResponseDTO<Page<Events>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        try {
            Page<Events> savedEvent = eventService.getAllEvent(page);
            commonResponseDTO.setData(savedEvent);
            commonResponseDTO.setMessage("Successfully  retrived Event");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            commonResponseDTO.setMessage("failed to retrived Event");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }


    }

    @DeleteMapping("/{project_id}/{event_id}")
    public ResponseEntity<CommonResponseDTO> deleteEvent(HttpServletRequest request,  @PathVariable int event_id, @PathVariable int project_id) {
        CommonResponseDTO<Events> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        try {
            Project project = projectService.getProjectById(project_id);
            if (project == null) {
                commonResponseDTO.setMessage("Project not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }
            List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
            boolean isProjectolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "PROJECT");
            List<UserRoleDetails> userRoleDetailsProject = userRoleDetailsServices.getuserRoleDetailsByProject(user, true, "PROJECT", project_id);
            boolean isEventAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetailsProject, "PROJECT_EVENT");
            if (isEventAvailable || isProjectolicyAvailable) {
                Events getevent = eventService.getEventById(event_id);
                if (getevent == null) {
                    commonResponseDTO.setMessage("Event not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }

                eventService.deleteEvent(event_id);
                commonResponseDTO.setMessage("Successfully delete the Event");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } else {
                commonResponseDTO.setMessage("No Authority to create event");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("failed to delete Event");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }


    }


}
