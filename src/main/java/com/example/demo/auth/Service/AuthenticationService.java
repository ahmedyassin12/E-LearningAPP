package com.example.demo.auth.Service;

import com.example.demo.auth.Dto.*;
import com.example.demo.configuration.JwtService;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Role;
import com.example.demo.token.Token;
import com.example.demo.token.TokenType;
import com.example.demo.token.VerificationToken;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final SkillDao skillDao;
    private final FieldDao fieldDao;
    private final UserDAO repository ;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenDAO tokenDAO ;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ObjectValidator<AuthenticationRequest> authRequestvalidator ;
    private final ObjectValidator<RegisterRequest> registerRequestvalidator ;
    private final ObjectValidator<StudentRequest> studentRequestvalidator ;
    private final ObjectValidator<FormateurRequest> formateurRequestvalidator ;
    private final ObjectValidator<AuthenticationRequest> authenticationRequestvalidator ;


    private final EmailService emailService ;


    public String registerManager(RegisterRequest request) {

        registerRequestvalidator.validate(request);
        User user;


            user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phoneNumber(request.getPhoneNumber())
                    .dateNaissance(request.getDateNaissance())
                    .role(Role.MANAGER)
                    .build();


        Optional<User> CheckUser = repository.findUserByEmail(user.getEmail()) ;

        if (CheckUser.isPresent()) {

            //use isEnabled to know if he is Verified
            if(   CheckUser.get().isEnabled() )
            {
                throw new IllegalArgumentException("User already verified") ;

            }

            //if user exist but didnt verified
            else {

                //user exist, but it's not verified so we go and verify him :
                return createAndSendVerificationToken(CheckUser.get());

            }
        }


//new User :
       User savedUser= repository.save(user) ;




        return createAndSendVerificationToken(savedUser);

    }


    //-----------
    private String createAndSendVerificationToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .user(user)
                .build();

        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user.getEmail(), token);
        return token;



    }
    public String registerStudent(StudentRequest request) {
        studentRequestvalidator.validate(request);
        Student student;


        student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .dateNaissance(request.getDateNaissance())
                .role(Role.STUDENT)
                .age(request.getAge())
                .build();

        Optional<User> CheckUser = repository.findUserByEmail(student.getEmail()) ;

        if (CheckUser.isPresent()) {

            //use isEnabled to know if he is Verified
            if(   CheckUser.get().isEnabled() )
            {
                throw new IllegalArgumentException("User already verified") ;

            }

            //if user exist but didnt verified
            else {

                //user exist, but it's not verified so we go and verify him :
                return createAndSendVerificationToken(CheckUser.get());

            }
        }





        var savedUser= repository.save(student) ;


        return createAndSendVerificationToken(savedUser);


    }

    public String registerFormateur(FormateurRequest request) {

        formateurRequestvalidator.validate(request);
        Formateur formateur;
        Set<Skill> skills = new HashSet<>();

        for( String name : request.getSkillNames() ) {


            skills.add(

                    skillDao.findByName(name).orElseThrow(
                            ()->new EntityNotFoundException("skill  not found invalid name "+name+" !!") )

            )  ;


        }

        Field field=fieldDao.getFieldByFieldName(request.getFieldName()).orElseThrow(
                ()->new EntityNotFoundException("field " + "not found invalid Name !! "));



        formateur = Formateur.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .dateNaissance(request.getDateNaissance())
                .role(Role.FORMATEUR)
                .skills(skills)
                .experienceYears(request.getExperience_time())
                .field(field)
                .availability(request.getAvailability())
                .build();

        Optional<User> CheckUser = repository.findUserByEmail(formateur.getEmail()) ;

        if (CheckUser.isPresent()) {

            //use isEnabled to know if he is Verified
            if(   CheckUser.get().isEnabled() )
            {
                throw new IllegalArgumentException("User already verified") ;

            }

            //if user exist but didnt verified
            else {

                //user exist, but it's not verified so we go and verify him :
                return createAndSendVerificationToken(CheckUser.get());

            }
        }

        System.out.println("\n \n formateur : "+formateur+"\n \n");

        var savedUser= repository.save(formateur) ;



        return createAndSendVerificationToken(savedUser);


    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) throws BadRequestException {

        authenticationRequestvalidator.validate(request);
        System.out.println("username = "+request.getUsername() +" pass = "+request.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );


        } catch (BadCredentialsException e) {
            System.out.println("username = "+request.getUsername() +" pass = "+request.getPassword());

                throw new BadRequestException("Invalid username or password");
        }


        var user = repository.findByUsername(request.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username : " + request.getUsername())
        );

        var jwtToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user,jwtToken);
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build() ;
        tokenDAO.save(token) ;
    }

    private void revokeAllUserTokens(User user){


        var validUserTokens = tokenDAO.findAllValidTokenByUser(user.getId()) ;
        if(validUserTokens.isEmpty()) return;

        validUserTokens.forEach(t->{

            t.setExpired(true);

            t.setRevoked(true);


        });

        tokenDAO.saveAll(validUserTokens);

    }

}