package com.olaoye.rewardyourteacher.dto;

import com.olaoye.rewardyourteacher.enums.Gender;
import com.olaoye.rewardyourteacher.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class TeacherRequestDTO {
    private String firstName;
    private String lastName;
    private String password;
    private Gender gender;
    private String phoneNumber;
    private String profilePicture;
    private String about;
    private String position;
    private Status status;
    private String yearOfService;

    private LocalDateTime yearOfEmployment;
    private LocalDateTime yearOfResignation;
}
