package com.cosmicbook.search_and_rescue_droid.config;

import com.cosmicbook.search_and_rescue_droid.model.User;
import com.cosmicbook.search_and_rescue_droid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserCleanupRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.cosmicbook.search_and_rescue_droid.repository.OtpVerificationRepository otpVerificationRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting duplicate user cleanup check...");
        List<User> allUsers = userRepository.findAll();

        // Group users by email
        Map<String, List<User>> usersByEmail = allUsers.stream()
                .filter(u -> u.getEmail() != null)
                .collect(Collectors.groupingBy(User::getEmail));

        usersByEmail.forEach((email, users) -> {
            if (users.size() > 1) {
                System.out.println("Found " + users.size() + " duplicates for email: " + email);
                // Keep the last one (assuming last added has highest ObjectID or just keep one
                // arbitrarily)
                // Ideally we sort by ID if the ID is sortable (Mongo ObjectId is timestamped)
                // But simplified: keep the last one in the list

                for (int i = 0; i < users.size() - 1; i++) {
                    User duplicate = users.get(i);
                    userRepository.delete(duplicate);
                    System.out.println("Deleted duplicate user with ID: " + duplicate.getId());
                }
                System.out.println("Kept user with ID: " + users.get(users.size() - 1).getId());
            }
        });

        // Cleanup duplicate OTPs
        System.out.println("Starting duplicate OTP cleanup check...");
        List<com.cosmicbook.search_and_rescue_droid.model.OTPVerification> allOtps = otpVerificationRepository
                .findAll();
        Map<String, List<com.cosmicbook.search_and_rescue_droid.model.OTPVerification>> otpsByEmail = allOtps.stream()
                .filter(o -> o.getEmail() != null)
                .collect(Collectors.groupingBy(com.cosmicbook.search_and_rescue_droid.model.OTPVerification::getEmail));

        otpsByEmail.forEach((email, otps) -> {
            if (otps.size() > 1) {
                System.out.println("Found " + otps.size() + " duplicate OTPs for email: " + email);
                for (int i = 0; i < otps.size() - 1; i++) {
                    otpVerificationRepository.delete(otps.get(i));
                }
            }
        });

        System.out.println("Duplicate user/OTP cleanup check completed.");
    }
}
