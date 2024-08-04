package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Role;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("api/v1/ou")
public class OUController {
    @Autowired
    public final OUService ouService;

    public OUController(OUService ouService) {
        this.ouService = ouService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addOu(@RequestBody OU ou) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();

        try {
            OU newOu = ouService.createOU(ou);
            commonResponseDTO.setData(newOu);
            commonResponseDTO.setMessage("Successfully OU Added");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to add OU");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<CommonResponseDTO> updateOu(@RequestBody OU ou) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        try {
            String message = ouService.updateOU(ou);
            commonResponseDTO.setData(ou);
            commonResponseDTO.setMessage(message);
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to Edit OU");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getAllOus")
    public ResponseEntity<CommonResponseDTO> getAllOus(){
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        try {
            List<OU> data = ouService.getAllOUs();
            commonResponseDTO.setData((OU) data);
            commonResponseDTO.setMessage("Successfully retrieved Ous");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

        }catch (Exception e){
            commonResponseDTO.setError(e.getMessage());
            commonResponseDTO.setMessage("Failed to retrieve Ous");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping(value = "/getOU/{userID}")
//    public ResponseEntity searchUser(@PathVariable int userID)

}
