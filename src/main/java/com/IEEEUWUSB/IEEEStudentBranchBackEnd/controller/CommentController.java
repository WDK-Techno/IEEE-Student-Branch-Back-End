package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.AssignPolicyDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommentDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.CommentService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.ProjectService;
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

    @Autowired
    private ProjectService projectService;

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


        }else if(commentDTO.getType().equals("PROJECT")){
            Project project = projectService.getProjectById(commentDTO.getProject_id());
            if(project == null){
                commonResponseDTO.setMessage("Project not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
            Comment newcomment = Comment.builder()
                    .comment(commentDTO.getComment())
                    .user(user)
                    .project(project)
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
            commonResponseDTO.setMessage("Invalid Comment type");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/{taskID}")
    public ResponseEntity<CommonResponseDTO<List<Comment>>> getCommentsByTask(
            HttpServletRequest request, @PathVariable int taskID) {
        CommonResponseDTO<List<Comment>> commonResponseDTO = new CommonResponseDTO<>();
        try {
            List<Comment> comments = commentService.getCommentsByTask(taskID);
                commonResponseDTO.setData(comments);
                commonResponseDTO.setMessage("Comments retrieved successfully");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to retrieve comments");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/project/{project_id}")
    public ResponseEntity<CommonResponseDTO<List<Comment>>> getCommentsByProject(
            HttpServletRequest request, @PathVariable int project_id) {
        CommonResponseDTO<List<Comment>> commonResponseDTO = new CommonResponseDTO<>();
        try {
            List<Comment> comments = commentService.getCommentsByProject(project_id);
            if (comments != null && !comments.isEmpty()) {
                commonResponseDTO.setData(comments);
                commonResponseDTO.setMessage("Comments retrieved successfully");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                commonResponseDTO.setMessage("No comments found for the specified Project");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to retrieve comments");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{commentID}")
    public ResponseEntity<CommonResponseDTO<Void>> deleteComment(HttpServletRequest request, @PathVariable int commentID) {
        CommonResponseDTO<Void> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");

        try {
            Comment comment = commentService.getCommentById(commentID);

            if (comment == null) {
                commonResponseDTO.setMessage("Comment not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }

            // Check if the user has permission to delete the comment
            if (!comment.getUser().equals(user)) {
                commonResponseDTO.setMessage("No Authority to Delete Comment");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
            }

            // Perform delete operation
            commentService.deleteComment(commentID);
            commonResponseDTO.setMessage("Comment deleted successfully");

            return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            commonResponseDTO.setMessage("Failed to delete comment");
            commonResponseDTO.setError(e.getMessage());

            return new ResponseEntity<>(commonResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
