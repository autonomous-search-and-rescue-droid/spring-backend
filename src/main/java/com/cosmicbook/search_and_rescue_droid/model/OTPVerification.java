package com.cosmicbook.search_and_rescue_droid.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "otp_verifications")
public class OTPVerification {

    @Id
    private String id;

    private String email;
    private String otp;
    private LocalDateTime expiryTime;

}
