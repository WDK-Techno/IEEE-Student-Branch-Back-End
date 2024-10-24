package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.FinanceBalanceDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.TransectionDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.AccountService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TransectionService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/transaction")
public class TransectionController {

    @Autowired
    private TransectionService transectionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;


    @PostMapping("/account")
    public ResponseEntity<CommonResponseDTO> addAccountTransection(HttpServletRequest request, @RequestBody TransectionDTO transectionDTO) {
        CommonResponseDTO<Transaction> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");

        // Check if user has the "FINANCE_ALL" policy
        boolean isFinanceAllolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");

        // Validate the transaction type
        boolean typeValidation = transectionDTO.getType().equals("CREDIT") || transectionDTO.getType().equals("DEBIT");
        if (!typeValidation) {
            commonResponseDTO.setMessage("Invalid type");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }

        // Only allow if the user has the "FINANCE_ALL" policy
        if (isFinanceAllolicyAvailable) {
            try {
                // Fetch the account by ID
                Account account = accountService.getAccountById(transectionDTO.getAccount_id());
                if (account == null) {
                    commonResponseDTO.setMessage("Account not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }

                // Validate amount for CREDIT transactions
                if (transectionDTO.getType().equals("CREDIT") && transectionDTO.getAmount() > account.getAmount()) {
                    commonResponseDTO.setMessage("Invalid amount");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                }

                Wallet toWallet = null;
                if (transectionDTO.getTo_wallet_id() != null) {
                    toWallet = walletService.getWalletById(transectionDTO.getTo_wallet_id());
                }

                // Update the account balance based on transaction type
                Double accountBalance = account.getAmount();
                if (transectionDTO.getType().equals("CREDIT")) {
                    accountBalance -= transectionDTO.getAmount(); // Subtract for CREDIT
                } else if (transectionDTO.getType().equals("DEBIT")) {
                    accountBalance += transectionDTO.getAmount(); // Add for DEBIT
                }

                // Create a transaction for the account
                Transaction accountTransaction = Transaction.builder()
                        .title(transectionDTO.getTitle())
                        .description(transectionDTO.getDescription())
                        .date(LocalDateTime.now())
                        .type(transectionDTO.getType())
                        .amount(transectionDTO.getAmount())
                        .to_wallet(toWallet)
                        .balance(accountBalance)
                        .account(account)
                        .build();

                // Handle the wallet transaction if a wallet is involved
                if (toWallet != null) {
                    Double walletBalance = toWallet.getAmount();
                    if (transectionDTO.getType().equals("CREDIT")) {
                        walletBalance += transectionDTO.getAmount(); // Add to wallet for CREDIT
                    } else if (transectionDTO.getType().equals("DEBIT")) {
                        walletBalance -= transectionDTO.getAmount(); // Subtract from wallet for DEBIT
                    }

                    // Create a transaction for the wallet
                    Transaction walletTransaction = Transaction.builder()
                            .title(transectionDTO.getTitle())
                            .description(transectionDTO.getDescription())
                            .date(LocalDateTime.now())
                            .type(transectionDTO.getType().equals("CREDIT") ? "DEBIT" : "CREDIT")
                            .amount(transectionDTO.getAmount())
                            .wallet(toWallet)
                            .balance(walletBalance)
                            .build();

                    transectionService.saveTransection(walletTransaction);
                    walletService.saveWallet(toWallet); // Update wallet balance
                }

                // Save the account transaction and update account balance
                Transaction savedTransaction = transectionService.saveTransection(accountTransaction);
                account.setAmount(accountBalance); // Update the account balance
                accountService.saveAccount(account); // Persist the account

                commonResponseDTO.setData(savedTransaction);
                commonResponseDTO.setMessage("Successfully added Transaction");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);

            } catch (Exception e) {
                commonResponseDTO.setMessage("Failed to add Transaction");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            // No authority to add transaction
            commonResponseDTO.setMessage("No Authority to Add Transaction");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/wallet")
    public ResponseEntity<CommonResponseDTO> addWalletTransection(HttpServletRequest request, @RequestBody TransectionDTO transectionDTO) {
        CommonResponseDTO<Transaction> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isFinanceAllolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        boolean isFinanceolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE");
        boolean typeValidation = transectionDTO.getType().equals("CREDIT") || transectionDTO.getType().equals("DEBIT");

        if (!typeValidation) {
            commonResponseDTO.setMessage("invalid type");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }

        try {
            Wallet mainWallet = walletService.getWalletById(transectionDTO.getWallet_id());
            if (mainWallet == null) {
                commonResponseDTO.setMessage("wallet is not found");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
            }

            // Check the policies for finance authority
            if (isFinanceAllolicyAvailable || isFinanceolicyAvailable) {
                double mainWalletBalance = mainWallet.getAmount();

                if (transectionDTO.getType().equals("CREDIT")) {
                    // Validate amount for CREDIT
                    if (transectionDTO.getAmount() > mainWalletBalance) {
                        commonResponseDTO.setMessage("invalid amount");
                        return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
                    }
                    mainWalletBalance -= transectionDTO.getAmount(); // Decrease balance for CREDIT
                } else if (transectionDTO.getType().equals("DEBIT")) {
                    mainWalletBalance += transectionDTO.getAmount(); // Increase balance for DEBIT
                }

                // Create main wallet transaction
                Transaction mainWalletTransaction = Transaction.builder()
                        .title(transectionDTO.getTitle())
                        .description(transectionDTO.getDescription())
                        .date(LocalDateTime.now())
                        .type(transectionDTO.getType())
                        .amount(transectionDTO.getAmount())
                        .wallet(mainWallet)
                        .balance(mainWalletBalance)
                        .build();

                // Handle transfer to another wallet if applicable
                if (transectionDTO.getTo_wallet_id() != null) {
                    Wallet toWallet = walletService.getWalletById(transectionDTO.getTo_wallet_id());
                    if (toWallet != null) {
                        double toWalletBalance = toWallet.getAmount();
                        // Update the other wallet balance based on transaction type
                        if (transectionDTO.getType().equals("CREDIT")) {
                            toWalletBalance += transectionDTO.getAmount(); // Increase for transfer to other wallet
                            mainWalletTransaction.setTo_wallet(toWallet);
                            // Create transaction for toWallet
                            Transaction toWalletTransaction = Transaction.builder()
                                    .title(transectionDTO.getTitle())
                                    .description(transectionDTO.getDescription())
                                    .date(LocalDateTime.now())
                                    .type("DEBIT")
                                    .amount(transectionDTO.getAmount())
                                    .wallet(toWallet)
                                    .to_wallet(mainWallet)
                                    .balance(toWalletBalance)
                                    .build();
                            transectionService.saveTransection(toWalletTransaction);
                            toWallet.setAmount(toWalletBalance);
                            walletService.saveWallet(toWallet);
                            // Save toWallet transaction
                        } else if (transectionDTO.getType().equals("DEBIT")) {
                            toWalletBalance -= transectionDTO.getAmount(); // Decrease for transfer to main wallet
                            mainWalletTransaction.setTo_wallet(toWallet);
                            // Create transaction for toWallet
                            Transaction toWalletTransaction = Transaction.builder()
                                    .title(transectionDTO.getTitle())
                                    .description(transectionDTO.getDescription())
                                    .date(LocalDateTime.now())
                                    .type("CREDIT")
                                    .amount(transectionDTO.getAmount())
                                    .wallet(toWallet)
                                    .balance(toWalletBalance)
                                    .build();
                            transectionService.saveTransection(toWalletTransaction);
                            toWallet.setAmount(toWalletBalance);
                            walletService.saveWallet(toWallet);
                            // Save toWallet transaction
                        }
                    }
                }

                Transaction savedTransaction = transectionService.saveTransection(mainWalletTransaction);
                mainWallet.setAmount(mainWalletBalance);
                walletService.saveWallet(mainWallet);
                commonResponseDTO.setData(savedTransaction);
                commonResponseDTO.setMessage("Successfully added Transaction");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);

            } else {
                commonResponseDTO.setMessage("No Authority to Add Transaction");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            commonResponseDTO.setMessage("failed to add Transaction");
            commonResponseDTO.setError(e.getMessage());
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/wallet/{wallet_id}")
    public ResponseEntity<CommonResponseDTO> getWalletTransection(
            HttpServletRequest request,
            @PathVariable int wallet_id,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate

    ) {
        CommonResponseDTO<Page<Transaction>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isFinanceolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE");
        if (isFinanceolicyAvailable) {
            try {
                Wallet mainWallet = walletService.getWalletById(wallet_id);
                if (mainWallet == null) {
                    commonResponseDTO.setMessage("wallet in not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }


                Page<Transaction> allTransection = transectionService.findAllTransactionByWaltet(search, mainWallet, type, startDate, endDate, page);
                commonResponseDTO.setData(allTransection);
                commonResponseDTO.setMessage("Successfully retrived Transection");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);


            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to retrived Transection");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("unauthorized");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }


    @GetMapping("/account/{account_id}")
    public ResponseEntity<CommonResponseDTO> getAccountTransection(
            HttpServletRequest request,
            @PathVariable int account_id,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate

    ) {
        CommonResponseDTO<Page<Transaction>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isFinanceolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        if (isFinanceolicyAvailable) {
            try {
                Account mainAccount = accountService.getAccountById(account_id);
                if (mainAccount == null) {
                    commonResponseDTO.setMessage("account in not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }


                Page<Transaction> allTransection = transectionService.findAllTransactionByAccount(search, mainAccount, type, startDate, endDate, page);
                commonResponseDTO.setData(allTransection);
                commonResponseDTO.setMessage("Successfully retrived Transection");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);


            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to retrived Transection");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("unauthorized");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }


    @GetMapping("/account_balance/{account_id}")
    public ResponseEntity<CommonResponseDTO> getAccountBalance(
            HttpServletRequest request,
            @PathVariable int account_id
    ) {
        CommonResponseDTO<FinanceBalanceDTO> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isFinanceolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE_ALL");
        if (isFinanceolicyAvailable) {
            try {
                Account mainAccount = accountService.getAccountById(account_id);
                if (mainAccount == null) {
                    commonResponseDTO.setMessage("account in not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }

                Double accountBalance =  mainAccount.getAmount();
                Double CreditAmount = transectionService.getTotalCreditByAccountId(account_id);
                Double debitAmount = transectionService.getTotalDebitByAccountId(account_id);

                FinanceBalanceDTO financeBalanceDTO = FinanceBalanceDTO.builder()
                        .credit_balance(CreditAmount)
                        .debit_balance(debitAmount)
                        .main_balance(accountBalance)
                        .build();

                commonResponseDTO.setData(financeBalanceDTO);
                commonResponseDTO.setMessage("Successfully retrived balance");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);


            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to retrived balance");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("unauthorized");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }


    @GetMapping("/wallet_balance/{wallet_id}")
    public ResponseEntity<CommonResponseDTO> getWalletBalance(
            HttpServletRequest request,
            @PathVariable int wallet_id
    ) {
        CommonResponseDTO<FinanceBalanceDTO> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isFinanceolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "FINANCE");
        if (isFinanceolicyAvailable) {
            try {
                Wallet wallet = walletService.getWalletById(wallet_id);
                if (wallet == null) {
                    commonResponseDTO.setMessage("wallet is not found");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }

                Double accountBalance =  wallet.getAmount();
                Double CreditAmount = transectionService.getTotalCreditByWalletId(wallet_id);
                Double debitAmount = transectionService.getTotalDebitBywalletId(wallet_id);

                FinanceBalanceDTO financeBalanceDTO = FinanceBalanceDTO.builder()
                        .credit_balance(CreditAmount)
                        .debit_balance(debitAmount)
                        .main_balance(accountBalance)
                        .build();

                commonResponseDTO.setData(financeBalanceDTO);
                commonResponseDTO.setMessage("Successfully retrived balance");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);


            } catch (Exception e) {

                commonResponseDTO.setMessage("failed to retrived balance");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);

            }
        } else {
            commonResponseDTO.setMessage("unauthorized");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }


    }


}
