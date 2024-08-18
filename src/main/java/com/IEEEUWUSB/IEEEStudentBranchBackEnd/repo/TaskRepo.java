package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Task;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskRepo extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t JOIN t.users u WHERE u IN :user AND t.ou = :ou")
    Optional<List<Task>> findByUsersAndOu(User user, OU ou);

    Optional<List<Task>> findByOu(OU ou);
    Optional<List<Task>> findByProject(Project project);
    Optional<List<Task>> findByParentTask(Task task);
//    Optional<List<Task>> searchByUser(Task task);

}
