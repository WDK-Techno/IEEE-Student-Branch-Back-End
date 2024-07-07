package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.PolicyRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyService {

    @Autowired
    PolicyRepo policyRepo;

    public Policy CreatePolicy(Policy policy) {
        return policyRepo.save(policy);
    }

    public Page<Policy> getAllPolicy(Integer page, String search, String type) {
        Pageable pageable = PageRequest.of(page, 15);
        return policyRepo.findAllPolicies(search, type, pageable);
    }



    public boolean alreadyExistsPolicyCode(Policy policy) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("policyCode", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id");
        Example<Policy> example = Example.of(policy, matcher);
        try {
            policyRepo.findOne(example);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Policy getPolicyById(int policyId) {
        return policyRepo.findById(policyId).get();
    }

    public String updatePolicy(Policy policy) {
        try{
            getPolicyById(policy.getPolicyID());
            try{
                policyRepo.save(policy);
                return "Academic year updated successfully";
            }catch (Exception e){
                return "Academic Year Edited failed";
            }
        }catch (Exception e){
            return "Academic Year Not Found";
        }
    }

}
