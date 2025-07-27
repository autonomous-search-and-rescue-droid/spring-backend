package com.cosmicbook.search_and_rescue_droid.controller;

import com.cosmicbook.search_and_rescue_droid.dto.JwtResponse;
import com.cosmicbook.search_and_rescue_droid.dto.ResetPasswordRequest;
import com.cosmicbook.search_and_rescue_droid.dto.SignInRequest;
import com.cosmicbook.search_and_rescue_droid.dto.SignUpRequest;
import com.cosmicbook.search_and_rescue_droid.model.*;
import com.cosmicbook.search_and_rescue_droid.repository.OtpVerificationRepository;
import com.cosmicbook.search_and_rescue_droid.repository.RoleRepository;
import com.cosmicbook.search_and_rescue_droid.repository.UserRepository;
import com.cosmicbook.search_and_rescue_droid.security.JwtUtils;
import com.cosmicbook.search_and_rescue_droid.service.EmailService;
import com.cosmicbook.search_and_rescue_droid.service.RefreshTokenService;
import com.cosmicbook.search_and_rescue_droid.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private OtpVerificationRepository otpVerificationRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        //check if mail is verified
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if (user.isPresent() && !user.get().isEmailVerified()) {
            return ResponseEntity.badRequest().body("Error: Email is not verified. Please verify your email before logging in.");
        }

        User user_details = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        String jwt = jwtUtils.generateJwtToken(user_details);


        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String requestToken = request.get("refreshToken");

        return refreshTokenService.findByToken(requestToken)
                .map(token -> {
                    if (refreshTokenService.isExpired(token)) {
                        refreshTokenService.deleteByUserId(token.getUserId());
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired. Please sign in again.");
                    }
                    User user = userRepository.findById(token.getUserId()).orElseThrow();
                    String newJwt = jwtUtils.generateJwtToken(user);

                    return ResponseEntity.ok(Map.of("accessToken", newJwt, "refreshToken", token.getToken()));
                })
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token"));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_COMMON)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found")));
                    case "user" -> roles.add(roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found")));
                    default -> roles.add(roleRepository.findByName(ERole.ROLE_COMMON)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found")));
                }
            });
        }

        user.setRole(roles);
        userRepository.save(user);

        try{
            //generate otp
            String otp = String.format("%06d", new Random().nextInt(999999));

            // save otp to the database
            OTPVerification otpEntry = new OTPVerification();
            otpEntry.setEmail(user.getEmail());
            otpEntry.setOtp(otp);
            otpEntry.setExpiryTime(LocalDateTime.now().plusMinutes(10));
            otpVerificationRepository.save(otpEntry);

            // Send Email
            emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), otp);

            return ResponseEntity.ok("User registered successfully. Please verify email. An OTP has been sent to your email address, which is valid for 10 minutes.");

        }catch (Exception e){
            return ResponseEntity.status(500).body("Error: Failed to send verification email. Please try again later.");
        }

    }

    //verify email
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String otp){
        Optional<OTPVerification> record = otpVerificationRepository.findByEmail(email);

        try{
            if (record.isEmpty() || !record.get().getOtp().equals(otp)) {
                return ResponseEntity.badRequest().body("Invalid OTP.");
            }

            if (record.get().getExpiryTime().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("OTP expired.");
            }

            // update user
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Error: User not found."));
            user.setEmailVerified(true);
            user.setEmailVerifiedAt(LocalDateTime.now());
            userRepository.save(user);

            // delete otp record
            otpVerificationRepository.delete(record.get());

            // Return success response
            return ResponseEntity.ok("Email verified successfully.");
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error: Failed to verify email. Please try again later.");
        }

    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: User not found.");
        }

        try {
            // Generate new OTP
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Save OTP to the database
            OTPVerification otpEntry = new OTPVerification();
            otpEntry.setEmail(email);
            otpEntry.setOtp(otp);
            otpEntry.setExpiryTime(LocalDateTime.now().plusMinutes(10));
            otpVerificationRepository.save(otpEntry);

            // Send Email
            emailService.sendVerificationEmail(email, user.get().getUsername(), otp);

            return ResponseEntity.ok("OTP has been resent to your email address.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: Failed to resend OTP. Please try again later.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("Error: User not found.");
        }
        try {
            // generate new OTP
            String otp = String.format("%06d", new Random().nextInt(999999));

            // save otp to the database
            OTPVerification otpEntry= new OTPVerification();
            otpEntry.setEmail(email);
            otpEntry.setOtp(otp);
            otpEntry.setExpiryTime(LocalDateTime.now().plusMinutes(10));

            otpVerificationRepository.save(otpEntry);
            // Send Email
            emailService.sendForgotPasswordEmail(email, user.get().getUsername(), otp);

            return ResponseEntity.ok("An OTP has been sent to your email address for password reset, which is valid for 10 minutes.");
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Error: Failed to send forgot password email. Please try again later.");
        }

    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        Optional<OTPVerification> otpVerification = otpVerificationRepository.findByEmail(resetPasswordRequest.getEmail());

        if(otpVerification.isEmpty() || !otpVerification.get().getOtp().equals(resetPasswordRequest.getOtpCode())){
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
        if(otpVerification.get().getExpiryTime().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body("OTP expired.");
        }

        try{
            Optional<User> user = userRepository.findByEmail(resetPasswordRequest.getEmail());
            if(user.isEmpty()){
                return ResponseEntity.badRequest().body("Error: User not found.");
            }
            // Update user password
            user.get().setPassword(encoder.encode(resetPasswordRequest.getNewPassword()));
            userRepository.save(user.get());
            // Delete OTP record
            otpVerificationRepository.delete(otpVerification.get());

            return ResponseEntity.ok("Password reset successfully.");
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error: Failed to reset password. Please try again later.");
        }
    }

}
