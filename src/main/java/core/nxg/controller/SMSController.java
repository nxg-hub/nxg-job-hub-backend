//package core.nxg.controller;
//
//import core.nxg.dto.SmsDTO;
//import core.nxg.entity.OTP;
//import core.nxg.service.OTPService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//public class SMSController {
//
//    @Autowired
//    private OTPService otpService;
//
//    @PostMapping("/generate")
//    public ResponseEntity<String> generateOTP(@RequestBody SmsDTO request) {
//        OTP otpEntity = otpService.generateOTP(request.getEmail(), request.getPhoneNumber());
//        return new ResponseEntity<>("OTP generated and sent to " + request.getPhoneNumber(), HttpStatus.OK);
//    }
//
//    @PostMapping("/validate")
//    public ResponseEntity<String> validateOTP(@RequestParam String email, @RequestParam String otp) {
//        boolean isValid = otpService.validateOTP(email, otp);
//        if (isValid) {
//            return new ResponseEntity<>("OTP is valid.", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Invalid OTP.", HttpStatus.BAD_REQUEST);
//        }
//    }
//}
