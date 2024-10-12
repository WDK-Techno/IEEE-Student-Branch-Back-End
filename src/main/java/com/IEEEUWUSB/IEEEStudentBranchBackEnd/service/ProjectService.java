package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.ProjectRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.TaskRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {
    @Autowired
    ProjectRepo projectRepository;

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProjectById(Integer id) {
        Optional<Project> optionalRole = projectRepository.findById(id);
        return optionalRole.orElse(null);
    }

    public void DeleteProject(Project project){
        projectRepository.delete(project);
    }

    public Page<Project> getAllProject(Integer page, String name, String status, OU ou, TermYear termYear) {
        Pageable pageable = PageRequest.of(page, 15);
        return projectRepository.findByProjectNameOrStatusOrOusContainingOrTermyear(name, status, ou,termYear,pageable);
    }

    public Page<Project> getAllProjectByuser(Integer page, String name, String status, OU ou, TermYear termYear,User user) {
        Pageable pageable = PageRequest.of(page, 15);
        return projectRepository.findByProjectNameOrStatusOrOusContainingOrTermyearOrUsersContainingOrCreatedBy(name, status, ou,termYear,user,user,pageable);
    }

}
