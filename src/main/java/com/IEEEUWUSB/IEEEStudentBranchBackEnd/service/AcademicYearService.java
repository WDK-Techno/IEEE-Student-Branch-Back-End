package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.AccedemicYearRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@Transactional
public class AcademicYearService {

    @Autowired
    AccedemicYearRepo academicYearRepo;


    public AcademicYear createAcademicYear(AcademicYear academicYear) {
        return academicYearRepo.save(academicYear);
    }

    public Page<AcademicYear> getAllAcademicYears(Integer page, String academicYear, String status) {
        Pageable pageable = PageRequest.of(page, 15);
        return academicYearRepo.findAllByStatusAndAcademicYear(academicYear,status,pageable);
    }



    public boolean alreadyExistsAcademicYear(AcademicYear academicYear) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("academicYear", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id");
        Example<AcademicYear> example = Example.of(academicYear, matcher);
        try {
            academicYearRepo.findOne(example);
            return true;
        } catch (Exception e) {
            return false;
        }

    }


}
