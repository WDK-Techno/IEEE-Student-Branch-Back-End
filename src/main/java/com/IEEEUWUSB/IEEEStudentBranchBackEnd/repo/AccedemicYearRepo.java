package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AccedemicYearRepo extends JpaRepository<AcademicYear,Integer> {

    @Query("SELECT ay FROM AcademicYear ay " +
            "WHERE (:academicYear IS NULL OR ay.academicYear LIKE COALESCE(:academicYear, '') || '%') " +
            "AND (:status IS NULL OR ay.status LIKE COALESCE(:status, '') || '%')"+
            "ORDER BY ay.acedemicId DESC")
    Page<AcademicYear> findAllByStatusAndAcademicYear(String status, String academicYear, Pageable pageable);
}
