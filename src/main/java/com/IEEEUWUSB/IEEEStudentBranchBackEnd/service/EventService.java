package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.EventRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.ProjectRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EventService {
    @Autowired
    EventRepo eventRepo;

    public Events saveEvent(Events event) {
        return eventRepo.save(event);
    }

    public Page<Events> getAllEvent(Integer page) {
        Pageable pageable = PageRequest.of(page, 15);
        return eventRepo.findAllBy(pageable);
    }

    public Page<Events> getAllEventByProject(Integer page,Project project) {
        Pageable pageable = PageRequest.of(page, 15);
        return eventRepo.findByProject(project, pageable);
    }

    public String deleteEvent(int eventId) {
        if (eventRepo.existsById(eventId)){
            eventRepo.deleteById(eventId);
            return "Event deleted successfully";
        }else {
            return "Event Not Found";
        }
    }

    public Events getEventById(int event_id) {
        return eventRepo.findById(event_id).orElse(null);
    }
}
