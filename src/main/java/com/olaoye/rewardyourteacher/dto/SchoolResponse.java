package com.olaoye.rewardyourteacher.dto;

import com.olaoye.rewardyourteacher.enums.SchoolType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolResponse {
    private int id;
    private String name;
    private SchoolType schoolType;
    private String address;
    private String city;
    private String state;

}
