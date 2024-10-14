package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Task;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.TermYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.TermYearRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TermYearService {

    @Autowired
    TermYearRepo termYearRepo;

    public TermYear saveTermYear(TermYear termYear) {
        return termYearRepo.save(termYear);
    }

    public List<TermYear> getAllTermYear() {
        return termYearRepo.findAll();
    }

    public TermYear findByActiveStatus() {
        Optional<TermYear> optionalRole = termYearRepo.findByStatus("ACTIVE");
        return optionalRole.orElse(null);
    }

    public TermYear findByid(Integer id) {
        Optional<TermYear> optionalRole = termYearRepo.findById(id);
        return optionalRole.orElse(null);
    }
}
