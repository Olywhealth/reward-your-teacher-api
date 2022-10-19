package com.olaoye.rewardyourteacher.dto;

import com.olaoye.rewardyourteacher.entity.School;
import com.olaoye.rewardyourteacher.enums.Gender;
import com.olaoye.rewardyourteacher.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponseDTO {

    private Long Id;
    private String firstName;
    private String lastName;
    private String email;

    private Gender gender;
    private String about;

    private String profilePicture;
    private UserRole role;
    private String position;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime yearOfEmployment;
    private LocalDateTime yearOfResignation;

    private School school;
}
