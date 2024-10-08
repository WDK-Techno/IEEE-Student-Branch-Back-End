package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.CommentRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.OURepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepo commentRepo;

    @Autowired
    TaskRepo taskRepo;

    public Comment createComment(Comment comment) {
        return commentRepo.save(comment);
    }

    public List<Comment> getCommentsByTask(int taskID) {
        Task task = taskRepo.findById(taskID).get();
        Optional<List<Comment>> optionalRole = commentRepo.findByTask(task);
        return optionalRole.orElse(null);
    }
    public Comment getCommentById(int commentID) {
        return commentRepo.findById(commentID).orElse(null);
    }

    public String deleteComment(int commentID) {
        if (commentRepo.existsById(commentID)){
            commentRepo.deleteById(commentID);
            return "Comment deleted successfully";
        }else {
            return "Comment Not Found";
        }
    }
}


