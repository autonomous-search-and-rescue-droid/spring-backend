package com.cosmicbook.search_and_rescue_droid.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String email;
    private String otpCode;
    private String newPassword;

}
