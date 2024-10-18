package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Task;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Integer> {

    @Query("SELECT task FROM Task task " +
            "WHERE (:taskname IS NULL OR task.task_name LIKE CONCAT(:taskname, '%')) " +
            "AND (:status IS NULL OR task.status LIKE CONCAT(:status, '%')) " +
            "AND (:priority IS NULL OR task.priority LIKE CONCAT(:priority, '%')) " +
            "AND (:ou IS NULL OR task.ou = :ou) " +
            "AND ((:user IS NULL OR :user MEMBER OF task.users) " +
            "OR (:createdby IS NULL OR task.createdBy = :createdby)) " +
            "ORDER BY task.taskId DESC")
    Page<Task> findByOuAndUsers(String priority,String taskname,OU ou, String status, User user, User createdby, Pageable pageable);



    @Query("SELECT task FROM Task task " +
            "WHERE (:taskname IS NULL OR task.task_name LIKE CONCAT(:taskname, '%')) " +
            "AND (:status IS NULL OR task.status LIKE CONCAT(:status, '%')) " +
            "AND (:priority IS NULL OR task.priority LIKE CONCAT(:priority, '%')) " +
            "AND (:project IS NULL OR task.project = :project) " +
            "AND ((:user IS NULL OR :user MEMBER OF task.users) " +
            "OR (:createdby IS NULL OR task.createdBy = :createdby)) " +
            "ORDER BY task.taskId DESC")
    Page<Task> findByProjectAndUsers(String priority, String taskname,Project project, String status, User user, User createdby, Pageable pageable);


    Optional<List<Task>> findByParentTask(Task task);

    long countByProjectAndStatus(
           Project project, String status
    );

    @Query("SELECT COUNT(task) FROM Task task " +
            "WHERE task.status = :status " +
            "AND task.project = :project " +
            "AND (:user MEMBER OF task.users OR task.createdBy = :user)")
    long countProjectTaskWithUser(Project project, User user, String status);

    long countByOuAndStatus(OU ou, String status);


}
