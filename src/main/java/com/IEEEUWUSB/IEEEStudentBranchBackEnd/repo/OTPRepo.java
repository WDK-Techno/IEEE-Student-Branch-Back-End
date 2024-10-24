package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OTP;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepo  extends JpaRepository<OTP, Integer> {
    Optional<OTP> findByuser(User user);
}
