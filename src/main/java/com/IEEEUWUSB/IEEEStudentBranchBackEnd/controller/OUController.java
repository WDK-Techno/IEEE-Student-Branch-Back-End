package com.IEEEUWUSB.IEEEStudentBranchBackEnd.controller;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto.CommonResponseDTO;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.OUService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.TermYearService;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.UserRoleDetailsServices;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/ou")
public class OUController {
    @Autowired
    public final OUService ouService;

    @Autowired
    public WalletService walletService;

    @Autowired
    private UserRoleDetailsServices userRoleDetailsServices;
    @Autowired
    private TermYearService termYearService;

    public OUController(OUService ouService) {
        this.ouService = ouService;
    }

    @PostMapping
    public ResponseEntity<CommonResponseDTO> addOu(HttpServletRequest request, @RequestBody OU ou) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {

                OU newOu = ouService.createOU(ou);
                Wallet newWallet = Wallet.builder()
                        .type("EXCOM")
                        .amount(0.0)
                        .ou(newOu)
                        .build();
                walletService.saveWallet(newWallet);
                commonResponseDTO.setData(newOu);
                commonResponseDTO.setMessage("Successfully OU Added");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.CREATED);
            } catch (Exception e) {
                commonResponseDTO.setMessage("Failed to add OU");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            commonResponseDTO.setMessage(isOtherPolicyAvailable + "No Authority to Add OU");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }

    }

    @PutMapping
    public ResponseEntity<CommonResponseDTO> updateOu(HttpServletRequest request, @RequestBody OU ou) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                String message = ouService.updateOU(ou);
                commonResponseDTO.setData(ou);
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("Failed to Edit OU");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            commonResponseDTO.setMessage("No Authority to Edit OU");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping(value = "/getOus")
    public ResponseEntity<CommonResponseDTO> getAllOus(HttpServletRequest request) {
        CommonResponseDTO<List<OU>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isAllPOlicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "EXCOM_ALL");

        if (isAllPOlicyAvailable) {
            try {
                List<OU> data = ouService.getAllOUs();
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully retrieved Ous");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

            } catch (Exception e) {
                commonResponseDTO.setError(e.getMessage());
                commonResponseDTO.setMessage("Failed to retrieve Ous");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {

                List<OU> ouList = ouService.getAllOUsByUser(user);
                commonResponseDTO.setData(ouList);

                commonResponseDTO.setMessage("Successfully retrieved Ous sep");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

            } catch (Exception e) {
                commonResponseDTO.setError(e.getMessage());
                commonResponseDTO.setMessage("Failed to retrieve Ous");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping(value = "/{ouID}")
    public ResponseEntity<CommonResponseDTO> getouByid(HttpServletRequest request, @PathVariable int ouID) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isAllPOlicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "EXCOM");

        if (isAllPOlicyAvailable) {
            try {
                OU data = ouService.getOUById(ouID);
                if (data == null){
                    commonResponseDTO.setMessage("Failed to retrieve Ous");
                    return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                }
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully retrieved Ous");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

            } catch (Exception e) {
                commonResponseDTO.setError(e.getMessage());
                commonResponseDTO.setMessage("Failed to retrieve Ous");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            commonResponseDTO.setMessage("Failed to retrieve Ous");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getExcom")
    public ResponseEntity<CommonResponseDTO<Page<UserRoleDetails>>> getExcom(HttpServletRequest request, @RequestParam(required = false) String search,
                                                                             @RequestParam(required = false) Integer ouid,
                                                                             @RequestParam(required = false) Integer termyearId,
                                                                             @RequestParam(defaultValue = "0") int page) {
        CommonResponseDTO<Page<UserRoleDetails>> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isAllPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "EXCOM_ALL");

        if (isAllPolicyAvailable) {
            try {
                OU ou = null;
                if(ouid!=null){
                    ou = ouService.getOUById(ouid);
                    if(ou == null){
                        commonResponseDTO.setMessage("OU not found");
                        return new ResponseEntity<>(commonResponseDTO, HttpStatus.NOT_FOUND);
                    }
                }

                if(termyearId==null){
                    TermYear activeTermYear = termYearService.findByActiveStatus();
                    termyearId = activeTermYear.getTermyearId();
                }

                Page<UserRoleDetails> data = userRoleDetailsServices.getAllExcomUserDetails(page, search, ouid, termyearId);
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully retrieved EXCOM Members");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

            } catch (Exception e) {
                commonResponseDTO.setError(e.getMessage());
                commonResponseDTO.setMessage("Failed to retrieve EXCOM Members");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                UserRoleDetails userRoleDetailsExcom = userRoleDetailsServices.findByUserAndIsActiveAndType(user, true, "EXCOM");
                OU ou = (OU) userRoleDetailsExcom.getOu();
                TermYear myTermYear = userRoleDetailsExcom.getTermyear();
                Page<UserRoleDetails> data = userRoleDetailsServices.getAllExcomUserDetails(page, search, ou.getOuID(),myTermYear.getTermyearId() );
                commonResponseDTO.setData(data);
                commonResponseDTO.setMessage("Successfully retrieved respective Excom Members");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);

            } catch (Exception e) {
                commonResponseDTO.setError(e.getMessage());
                commonResponseDTO.setMessage("Failed to retrieve respective Excom Members");
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @DeleteMapping(value = "/deleteOU/{ouID}")
    public ResponseEntity<CommonResponseDTO> deleteOu(HttpServletRequest request, @PathVariable int ouID) {
        CommonResponseDTO<OU> commonResponseDTO = new CommonResponseDTO<>();
        User user = (User) request.getAttribute("user");
        List<UserRoleDetails> userRoleDetails = userRoleDetailsServices.getuserRoleDetails(user, true, "MAIN");
        boolean isOtherPolicyAvailable = userRoleDetailsServices.isPolicyAvailable(userRoleDetails, "OTHER");
        if (isOtherPolicyAvailable) {
            try {
                String message = ouService.deleteOU(ouID);
                commonResponseDTO.setMessage(message);
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.OK);
            } catch (Exception e) {
                commonResponseDTO.setMessage("Failed to Delete OU");
                commonResponseDTO.setError(e.getMessage());
                return new ResponseEntity<>(commonResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            commonResponseDTO.setMessage("No Authority to Delete OU");
            return new ResponseEntity<>(commonResponseDTO, HttpStatus.UNAUTHORIZED);
        }
    }

}
