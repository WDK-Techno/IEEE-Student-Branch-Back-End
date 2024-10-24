package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OUService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.ProjectService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private OUService ouService;

    @GetMapping("/ou/all")
    public ResponseEntity<CommonResponseDTO> getWallets(HttpServletRequest request) {
        CommonResponseDTO<List<Wallet>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isFinanceAllpolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        if (isFinanceAllpolicyAvailable) {
            try {
                List<Wallet> excomWallets = walletService.getAllExcomWallet("EXCOM");
                commonResponseDTO.setData(excomWallets);
                commonResponseDTO.setMessage("Successfully retrieved excom wallets");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to retrieved excom wallets");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("No Authority to get all wallets");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/main")
    public ResponseEntity<CommonResponseDTO> getMainWallet(HttpServletRequest request) {
        CommonResponseDTO<List<Wallet>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isFinanceAllpolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        if (isFinanceAllpolicyAvailable) {
            try {
                List<Wallet> excomWallets = walletService.getAllExcomWallet("MAIN");
                commonResponseDTO.setData(excomWallets);
                commonResponseDTO.setMessage("Successfully retrieved excom wallets");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to retrieved excom wallets");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("No Authority to get all wallets");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }

    @GetMapping("")
    public ResponseEntity<CommonResponseDTO> getMyWallet(HttpServletRequest request) {
        CommonResponseDTO<Wallet> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        UserRoleDetails userRoleDetail = userRoleDetailsServices.findByUserAndIsActiveAndType(user, true, "EXCOM");
        boolean isFinanceAllpolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        try {
            if (isFinanceAllpolicyAvailable) {
                Wallet excomWallets = walletService.getSBWallet("MAIN");
                commonResponseDTO.setData(excomWallets);
                commonResponseDTO.setMessage("Successfully retrieved excom wallets");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } else {
                OU usersOU = userRoleDetail.getOu();
                if (usersOU == null) {
                    commonResponseDTO.setMessage("No Authority to get all wallets");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
                }
                Wallet excomWallets = walletService.getWalletByOU(usersOU);
                commonResponseDTO.setData(excomWallets);
                commonResponseDTO.setMessage("Successfully retrieved excom wallets");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("failed to retrieved excom wallets");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

        }


    }


    @GetMapping("/project/{project_id}")
    public ResponseEntity<CommonResponseDTO> getWalletByProject(HttpServletRequest request, @PathVariable int project_id) {
        CommonResponseDTO<Wallet> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
            try {
                Project project = projectService.getProjectById(project_id);
                if (project == null) {
                    commonResponseDTO.setMessage("Project not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }

                Wallet wallet = walletService.getWalletByProoject(project);
                commonResponseDTO.setData(wallet);
                commonResponseDTO.setMessage("Successfully retrieved project wallets");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("failed to retrieved project wallets");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
    }


    @GetMapping("ou/project/{ou_id}")
    public ResponseEntity<CommonResponseDTO> getProjectWalletByExom(HttpServletRequest request, @PathVariable int ou_id) {
        CommonResponseDTO<List<Wallet>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isFinanceAllpolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        try {

            List<Wallet> projectWallets = new ArrayList<>();

            if(isFinanceAllpolicyAvailable){
                List<Project> projects = projectService.getAllProject();
                for (Project project : projects) {
                    Wallet newWallet = walletService.getWalletByProoject(project);
                    projectWallets.add(newWallet);
                }
                commonResponseDTO.setData(projectWallets);
                commonResponseDTO.setMessage("Successfully retrieved project wallets");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            }else{
                OU ou = ouService.getOUById(ou_id);
                if (ou == null) {
                    commonResponseDTO.setMessage("Ou not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }
                List<Project> projects = projectService.getAllProjectByExomList(ou);
                for (Project project : projects) {
                    Wallet newWallet = walletService.getWalletByProoject(project);
                    projectWallets.add(newWallet);
                }
                commonResponseDTO.setData(projectWallets);
                commonResponseDTO.setMessage("Successfully retrieved project wallets");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            }


        } catch (Exception e) {
            commonResponseDTO.setMessage("failed to retrieved project wallets");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

        }
    }

}
