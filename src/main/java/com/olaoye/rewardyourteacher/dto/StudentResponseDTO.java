package com.olaoye.rewardyourteacher.dto;

import com.olaoye.rewardyourteacher.enums.Gender;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class StudentResponseDTO {
    private long studentId;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String profilePicture;
}
