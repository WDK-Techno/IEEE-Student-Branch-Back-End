package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.ProjectDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.AccountService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addAccount(HttpServletRequest request, @RequestBody Account account) {
        CommonResponseDTO<Account> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isAccountMainPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        if (isAccountMainPolicyAvailable) {
            try {

                Account newAccount = Account.builder()
                        .account_number(account.getAccount_number())
                        .bank_name(account.getBank_name())
                        .branch(account.getBranch())
                        .description(account.getDescription())
                        .build();
                Account savedAccount = accountService.saveAccount(newAccount);
                commonResponseDTO.setData(savedAccount);
                commonResponseDTO.setMessage("Successfully added Account");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to add Account");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("No Authority to Add Account");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }


    @PutMapping("/{account_id}")
    public ResponseEntity<CommonResponseDTO> editAccount(HttpServletRequest request, @RequestBody Account account,@PathVariable int account_id) {
        CommonResponseDTO<Account> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isAccountMainPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        if (isAccountMainPolicyAvailable) {
            try {

                Account existingAccount = accountService.getAccountById(account_id);

                if(existingAccount==null){
                    commonResponseDTO.setMessage("Account does not exist");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }

                existingAccount.setAccount_number(account.getAccount_number());
                existingAccount.setBank_name(account.getBank_name());
                existingAccount.setBranch(account.getBranch());
                existingAccount.setDescription(account.getDescription());
                Account savedAccount = accountService.saveAccount(existingAccount);
                commonResponseDTO.setData(savedAccount);
                commonResponseDTO.setMessage("Successfully edit Account");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to edit Account");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("No Authority to Edit Account");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO> getAccount(HttpServletRequest request) {
        CommonResponseDTO<List<Account>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isAccountMainPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        if (isAccountMainPolicyAvailable) {
            try {

                List<Account> accounts =  accountService.getAllAccount();
                commonResponseDTO.setData(accounts);
                commonResponseDTO.setMessage("Successfully retrieved Account");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to retrieved Account");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("No Authority to retrieved Account");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }
}
