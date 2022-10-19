package com.olaoye.rewardyourteacher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olaoye.rewardyourteacher.entity.School;
import com.olaoye.rewardyourteacher.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class RewardYourTeacherApplication  {
    @Autowired
    private SchoolService schoolService;

    public static void main(String[] args) {

        SpringApplication.run(RewardYourTeacherApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        if (schoolService.getSchoolCount() < 1) {
            return args -> {
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<List<School>> typeReference = new TypeReference<>() {
                };
                InputStream inputStream = TypeReference.class.getResourceAsStream("/csv/school.json");
                try {
                    List<School> schools = mapper.readValue(inputStream, typeReference);
                    schoolService.saveSchool(schools);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            };
        }
        return null;
    }
}

