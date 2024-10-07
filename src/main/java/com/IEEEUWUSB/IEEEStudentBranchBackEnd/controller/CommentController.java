package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AssignPolicyDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommentDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.CommentService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TaksService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/comment")
public class CommentController {

    @Autowired
    private final TaksService taksService;

    @Autowired
    private final CommentService commentService;

    public CommentController(TaksService taksService, CommentService commentService) {
        this.taksService = taksService;
        this.commentService = commentService;
    }


    @PostMapping("")
    public ResponseEntity<CommonResponseDTO> createComment(HttpServletRequest request,
                                                          @RequestBody CommentDTO commentDTO
    ) {
        CommonResponseDTO<Comment> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        if(commentDTO.getType().equals("TASK")){
            Task task = taksService.findTaskById(commentDTO.getTask_id());
            Comment newcomment = Comment.builder()
                    .comment(commentDTO.getComment())
                    .user(user)
                    .task(task)
                    .created_at(LocalDateTime.now())
                    .build();

            try {
                var savedComment = commentService.createComment(newcomment);
                commonResponseDTO.setData(savedComment);
                commonResponseDTO.setMessage("Comment added successfully");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }


        }else{
            commonResponseDTO.setMessage("Type TASK Erorr");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }
}
