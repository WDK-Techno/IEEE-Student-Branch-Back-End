package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment,Integer> {

    Optional<List<Comment>> findByTask(Task task);

}
