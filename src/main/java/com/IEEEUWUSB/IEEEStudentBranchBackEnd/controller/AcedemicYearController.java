package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.AcademicYearService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@CrossOrigin
@RestController
@RequestMapping("api/v1/academic")
public class AcedemicYearController {

    @Autowired
    private final AcademicYearService academicYearService;
    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    public AcedemicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addAcademicYear(HttpServletRequest request, @RequestBody AcademicYear academicYear) {
        CommonResponseDTO<AcademicYear> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        UserRoleDetails userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                AcademicYear newAcademicYear = academicYearService.createAcademicYear(academicYear);
                commonResponseDTO.setData(newAcademicYear);
                commonResponseDTO.setMessage("Successfully added academic year");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } catch (Exception e) {
                boolean exist = academicYearService.alreadyExistsAcademicYear(academicYear);
                if (exist) {
                    commonResponseDTO.setMessage("academic year already exists");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.CONFLICT);
                } else {
                    commonResponseDTO.setMessage("failed to add academic year");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

            }
        } else {
            commonResponseDTO.setMessage("No Authority to Add Academic Year");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getAcademicYear(@RequestParam(required = false) String status,
                                                             @RequestParam(required = false) String academicYear,
                                                             @RequestParam(defaultValue = "0") int page) {
        CommonResponseDTO<Page<AcademicYear>> commonResponseDTO = new CommonResponseDTO<>();
        Page<AcademicYear> data = academicYearService.getAllAcademicYears(page, status, academicYear);
        commonResponseDTO.setData(data);
        commonResponseDTO.setMessage("Successfully retrieved academic year");
        return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<CommonResponseDTO> updateAcademicYear(HttpServletRequest request, @RequestBody AcademicYear academicYear) {
        CommonResponseDTO<AcademicYear> commonResponseDTO = new CommonResponseDTO<>();

        if (Objects.nonNull(academicYear.getAcedemicId()) && academicYear.getAcedemicId() != 0) {
            User user = (User) request.getAttribute("user");
            UserRoleDetails userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
            boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
            if (isOtherPolicyAvailable) {
                try {
                    String message = academicYearService.updateAcademicYear(academicYear);
                    commonResponseDTO.setMessage(message);
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
                } catch (Exception e) {
                    commonResponseDTO.setMessage("Academic Year Edited failed");
                    commonResponseDTO.setError(e.getMessage());
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }
            } else {
                commonResponseDTO.setMessage("No Authority to Edit Academic Year");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
            }

        } else {
            commonResponseDTO.setMessage("Academic id not found");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/deleteAcademicYear/{academicID}")
    public ResponseEntity<CommonResponseDTO> deletePolicy(HttpServletRequest request, @PathVariable int academicID) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        UserRoleDetails userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                String message = academicYearService.deleteAcademicYear(academicID);
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("Failed to Delete Academic Year");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("No Authority to Delete Academic Year");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }
    }
}
