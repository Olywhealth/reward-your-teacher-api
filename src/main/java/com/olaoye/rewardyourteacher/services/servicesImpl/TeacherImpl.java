package com.olaoye.rewardyourteacher.services.servicesImpl;

import com.olaoye.rewardyourteacher.config.security.AuthenticateService;
import com.olaoye.rewardyourteacher.config.security.JwtService;
import com.olaoye.rewardyourteacher.dto.*;
import com.olaoye.rewardyourteacher.entity.Notification;
import com.olaoye.rewardyourteacher.entity.School;
import com.olaoye.rewardyourteacher.entity.User;
import com.olaoye.rewardyourteacher.exceptions.CustomException;
import com.olaoye.rewardyourteacher.exceptions.UserNotFoundException;
import com.olaoye.rewardyourteacher.repository.SchoolRepository;
import com.olaoye.rewardyourteacher.repository.UserRepository;
import com.olaoye.rewardyourteacher.services.TeacherService;
import com.olaoye.rewardyourteacher.utils.EmailValidatorService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import static com.olaoye.rewardyourteacher.enums.UserRole.TEACHER;

@AllArgsConstructor
@Service
public class TeacherImpl implements TeacherService {

    private final UserRepository userRepository;
    private final NotificationServiceImp notificationService;
    private final PasswordEncoder passwordEncoder;
    private final SchoolRepository schoolRepository;
    private final AuthenticateService authenticateService;
    private final AuthenticationManager authenticationManager;
    private final UserImpl userImpl;
    private final JwtService jwtService;


    @Override
    public UserResponseDTO signUp(SignUpDto signUpDto) throws Exception {
        String email = signUpDto.getEmail().toLowerCase();

        if(!EmailValidatorService.isValid(email)){
            throw new CustomException("Enter a valid email address");
        }

        if (Objects.nonNull(userRepository.findByEmail(email))) {
            throw new CustomException("Teacher already exist");
        }

        School school = schoolRepository.findById(signUpDto.getSchoolId())
                .orElseThrow(Exception::new);


        User teacher = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .email(email)
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .phoneNumber(signUpDto.getPhoneNumber())
                .status(signUpDto.getStatus())
                .yearOfService(signUpDto.getYearOfService())
                .position(signUpDto.getPosition())
                .gender(signUpDto.getGender())
                .about(signUpDto.getAbout())
                .createdAt(LocalDateTime.now())
                .school(school)
                .profilePicture(signUpDto.getProfilePicture())
                .role(TEACHER)
                .build();

        User user = userRepository.save(teacher);
        return new ModelMapper().map(user, UserResponseDTO.class);
    }

    @Override
    public LoginResponse teacherLogin(LoginDTO loginDTO) {
        return authenticateService.getLoginResponse(loginDTO, authenticationManager, jwtService, TEACHER);
    }

    @Override
    public UserResponseDTO updateTeacher(TeacherRequestDTO teacherRequestDTO, String email) {
        return userImpl.updateTeacherResponseEntity(teacherRequestDTO, userRepository, email);

    }



    @Override
    public Notification teacherAppreciatesStudent(String email, Long studentId, MessageDTO messageDTO) {
        return notificationService.studentAppreciatedNotification(email, studentId, messageDTO);
    }

    @Override
    public LoginResponse teacherSocialLogin(SocialLoginRequest socialLoginRequest) {
        socialLoginRequest.setPassword("");
        User user = userRepository.findByEmail(socialLoginRequest.getEmail());
        if (user == null) {
            user = User.builder()
                    .firstName(socialLoginRequest.getFirstName())
                    .lastName(socialLoginRequest.getLastName())
                    .password(passwordEncoder.encode(socialLoginRequest.getPassword()))
                    .email(socialLoginRequest.getEmail())
                    .role(TEACHER)
                    .profilePicture(socialLoginRequest.getDisplayPicture())
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }
        String token = "Bearer " + jwtService.generateToken
                (new org.springframework.security.core.userdetails.User(socialLoginRequest.getEmail(), socialLoginRequest.getFirstName(),
                        new ArrayList<>()));

        return LoginResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .token(token)
                .gender(user.getGender())
                .role(user.getRole())
                .position(user.getPosition())
                .school(user.getSchool())
                .about(user.getAbout())
                .profilePicture(user.getProfilePicture())
                .yearOfEmployment(user.getYearOfEmployment())
                .yearOfResignation(user.getYearOfResignation())
                .build();
    }

    @Override
    public StudentResponseDTO findStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(UserNotFoundException::new);

        return StudentResponseDTO.builder()
                .studentId(studentId)
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .gender(student.getGender())
                .profilePicture(student.getProfilePicture())
                .build();
    }
}
