package com.olaoye.rewardyourteacher.dto;

import com.olaoye.rewardyourteacher.enums.Gender;
import com.olaoye.rewardyourteacher.enums.UserRole;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String schoolName;
    private String phoneNumber;
    private String position;

    private String profilePicture;
    private UserRole role;
    private String about;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime yearOfEmployment;
    private LocalDateTime yearOfResignation;
}
