package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}
