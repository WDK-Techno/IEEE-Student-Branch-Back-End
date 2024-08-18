package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Task;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.TaskRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class TaksService {

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    public Task saveTask(Task task) {
        return taskRepo.save(task);
    }

    public Task findTaskById(int id) {
        return taskRepo.findById(id).get();
    }

    public List<Task> findAllTasksByOU(OU ou) {
        Optional<List<Task>> optionalRole = taskRepo.findByOu(ou);
        return optionalRole.orElse(null);
    }

    public List<Task> findMyTasksByOU(User user, OU ou) {
        Optional<List<Task>> optionalRole = taskRepo.findByUsersAndOu(user, ou);
        return optionalRole.orElse(null);
    }

    public List<Task> getAllTask() {
        return taskRepo.findAll();
    }

    public String assign(Integer taskId, Integer[] usersIds) {
        try {
            Task task = findTaskById(taskId);
            Set<User> existingUsers = new HashSet<>(task.getUsers());
            Set<Integer> userIdSet = new HashSet<>(Arrays.asList(usersIds));
            for (User user : existingUsers) {
                if (!userIdSet.contains(user.getUserID())) {
                    task.removeuser(user);
                }
            }

            for (Integer userId : usersIds) {
                User user = userRepo.findById(userId).get();
                if (user != null && !task.getUsers().contains(user)) {
                    task.adduser(user);
                }
            }
            var savedtask = taskRepo.save(task);
            return "Assigned user successfully";
        } catch (Exception e) {
            return e.getMessage();
        }

    }


}
