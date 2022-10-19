package com.olaoye.rewardyourteacher.services;


import com.olaoye.rewardyourteacher.dto.EmailResponse;
import com.olaoye.rewardyourteacher.utils.EmailDetails;

public interface EmailService {

    EmailResponse sendSimpleMail(EmailDetails details, String email);
    EmailResponse sendMailWithAttachment(EmailDetails details, String email);
}
