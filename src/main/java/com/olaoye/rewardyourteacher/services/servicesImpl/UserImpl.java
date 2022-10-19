package com.olaoye.rewardyourteacher.services.servicesImpl;
import com.olaoye.rewardyourteacher.dto.StudentRequestDTO;
import com.olaoye.rewardyourteacher.dto.TeacherRequestDTO;
import com.olaoye.rewardyourteacher.dto.UserResponseDTO;
import com.olaoye.rewardyourteacher.entity.User;
import com.olaoye.rewardyourteacher.exceptions.UserNotFoundException;
import com.olaoye.rewardyourteacher.repository.UserRepository;
import com.olaoye.rewardyourteacher.utils.MapStructMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserImpl {

    @Autowired
    MapStructMapper mapStructMapper;

     UserResponseDTO updateTeacherResponseEntity(TeacherRequestDTO teacherRequestDTO, UserRepository userRepository,
                                                 String email
                                                 ) {
        User user = userRepository.findByEmail(email);
         if (user == null) {
             throw new UserNotFoundException("Invalid user");
         }
        user = mapStructMapper.updateUserFromTeacherRequestDTO(teacherRequestDTO, user);

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return mapStructMapper.userToUserResponse(user);
    }

    UserResponseDTO updateStudentResponseEntity(StudentRequestDTO studentRequestDTO, UserRepository userRepository,
                                                String email
    ) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Invalid user");
        }
        user = mapStructMapper.updateUserFromStudentRequestDTO(studentRequestDTO, user);

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return mapStructMapper.userToUserResponse(user);
    }
}
