package com.olaoye.rewardyourteacher.services;

import com.olaoye.rewardyourteacher.dto.*;
import com.olaoye.rewardyourteacher.entity.Notification;

public interface TeacherService {

    UserResponseDTO signUp(SignUpDto signUpDto) throws Exception;
    LoginResponse teacherLogin(LoginDTO loginDTO);
    UserResponseDTO updateTeacher(TeacherRequestDTO teacherRequestDTO, String email);
    Notification teacherAppreciatesStudent(String email, Long userId, MessageDTO messageDTO);
    LoginResponse teacherSocialLogin(SocialLoginRequest socialLoginRequest);

    StudentResponseDTO findStudent(Long studentId);
}
