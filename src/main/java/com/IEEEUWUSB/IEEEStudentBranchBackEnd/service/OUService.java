package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.UserDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.OURepo;
import jakarta.transaction.Transactional;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OUService {
    @Autowired
    OURepo ouRepo;

    public OU createOU(OU ou) {
        return ouRepo.save(ou);
    }

    public String updateOU(OU ou) {
        if (ouRepo.existsById(ou.getOuID())) {
            ouRepo.save(ou);
            return "OU successfully updated";

        } else {
            return "OU Not Found";
        }
    }

    public List<OU> getAllOUs() {
        List<OU> ouList = ouRepo.findAll();
        return ouList;
    }

    public OU getOUByID(int ouID) {
        if (ouRepo.existsById(ouID)) {
            OU ou = ouRepo.findById(ouID).orElse(null);
            return ou;
        } else {
            return null;
        }
    }
    public String deleteOU(int ouID) {
        if (ouRepo.existsById(ouID)) {
            ouRepo.deleteById(ouID);
            return "OU successfully deleted";
        }else {
            return "OU Not Found";
        }
    }
}
