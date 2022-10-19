package com.olaoye.rewardyourteacher.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PaystackVerifyRequestDTO {
    private String verificationCode;

}
