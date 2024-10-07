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
        return academicYearRepo.findAllByStatusAndAcademicYear(academicYear, status, pageable);
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

    public AcademicYear getAcademicYearById(int academicYearId) {
        return academicYearRepo.findById(academicYearId).get();
    }

    public String updateAcademicYear(AcademicYear academicYear) {
        try {
            AcademicYear acedemic = getAcademicYearById(academicYear.getAcedemicId());
            try {
                academicYearRepo.save(academicYear);
                return "Academic year updated successfully";
            } catch (Exception e) {
                return "Academic Year Edited failed";
            }
        } catch (Exception e) {
            return "Academic Year Not Found";
        }
    }


    public String deleteAcademicYear(int academicID) {
        if (academicYearRepo.existsById(academicID)) {
            academicYearRepo.deleteById(academicID);
            return "Academic year deleted successfully";
        } else {
            return "Academic Year Not Found";
        }
    }
}
