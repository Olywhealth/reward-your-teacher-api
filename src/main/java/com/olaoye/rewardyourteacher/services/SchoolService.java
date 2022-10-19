package com.olaoye.rewardyourteacher.services;

import com.olaoye.rewardyourteacher.dto.SchoolResponse;
import com.olaoye.rewardyourteacher.entity.School;

import java.util.List;

public interface SchoolService {
    List<SchoolResponse> getAllSchools(String schoolName, int pageNumber, int pageSize, String sortProperty);

    int getSchoolCount();

    String saveSchool(List<School> schoolEntities);
}
