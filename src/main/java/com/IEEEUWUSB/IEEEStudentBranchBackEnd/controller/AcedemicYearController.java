package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.AcademicYearService;
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


    public AcedemicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addAcademicYear(@RequestBody AcademicYear academicYear) {
        CommonResponseDTO<AcademicYear> commonResponseDTO = new CommonResponseDTO<>();
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
    public ResponseEntity<CommonResponseDTO> updateAcademicYear(@RequestBody AcademicYear academicYear) {
        CommonResponseDTO<AcademicYear> commonResponseDTO = new CommonResponseDTO<>();

        if (Objects.nonNull(academicYear.getAcedemicId()) && academicYear.getAcedemicId() != 0) {

            try{
                String message = academicYearService.updateAcademicYear(academicYear);
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            }catch (Exception e) {
                commonResponseDTO.setMessage("Academic Year Edited failed");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("Acedemic id not found");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
        }

    }
}
