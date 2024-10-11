package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.ChangePasswordDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.UserDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.UserRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserId(int id) {
        Optional<User> optionalUser = userRepo.findById(id);
        return optionalUser.orElse(null);
    }

    public String changePassword(ChangePasswordDTO request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Check whether the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
//            throw new IllegalStateException("Wrong password");
            return VarList.RSP_WRONG_PWD;
        }

        // Check if new passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
//            throw new IllegalStateException("Passwords do not match");
            return VarList.RSP_PWD_NOT_MATCH;
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Save new password
        userRepo.save(user);
        return VarList.RSP_SUCCESS;
    }


//   public String updateUser(UserDTO userDTO) {
//        if (userRepo.existsById(userDTO.getUserID())) {
//            userRepo.save(modelMapper.map(userDTO, User.class));
//            return VarList.RSP_SUCCESS;
//        } else {
//            return VarList.RSP_NO_DATA_FOUND;
//        }
//    }

    public List<UserDTO> getAllUser() {
        List<User> userList = userRepo.findAll();
        return modelMapper.map(userList, new TypeToken<ArrayList<UserDTO>>() {
        }.getType());
    }

    public UserDTO searchUser(int userID) {
        if (userRepo.existsById(userID)) {
            User user = userRepo.findById(userID).orElse(null);
            return modelMapper.map(user, UserDTO.class);
        } else {
            return null;
        }
    }

    public String deleteUser(int userID) {
        if (userRepo.existsById(userID)) {
            userRepo.deleteById(userID);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }


    public User findUserByEmail(String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        return userOptional.orElse(null);
    }


    public boolean alreadyExistsUser(User user) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id");
        Example<User> example = Example.of(user, matcher);
        try {
            userRepo.findOne(example);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public User saveUser(User user) {
       return userRepo.save(user);
    }

    public Page<UserDTO> getAllusers(Integer page, String search) {
        Pageable pageable = PageRequest.of(page, 15);
        Page<User> userPage = userRepo.finduser(search, pageable);
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(UserDTO::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(userDTOs, pageable, userPage.getTotalElements());
    }

}
