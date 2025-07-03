package com.example.demo.service;

import com.example.demo.Dtos.userDto.CreateUserDto;
import com.example.demo.Dtos.userDto.StudentDto;
import com.example.demo.Dtos.userDto.UserDto;
import com.example.demo.auth.Service.AuthenticationService;
import com.example.demo.auth.Dto.ChangePasswordRequest;
import com.example.demo.auth.Dto.RegisterRequest;
import com.example.demo.dao.UserDAO;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.mapper.UsersMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service

public class UserService {

    @Autowired
   private UserDAO userDAO;
    @Autowired
    private AuthenticationService authenticationService ;

    @Autowired
    private  PasswordEncoder passwordEncoder ;

    @Autowired
    private ObjectValidator<CreateUserDto> userValidator ;

    @Autowired
    private ObjectValidator<UserDto> updateUserValidator ;

    private UsersMapper userMapper=new UsersMapper();





    public List<UserDto> getAllUsers() {


        System.out.println("fetching from db");

        Iterable<User> users= userDAO.findAll();

        List<UserDto>userDtos=new ArrayList<>();

        users.forEach(user -> userDtos.add(userMapper.returnUserDto(user)));

        return userDtos ;



    }

    public UserDto findUserByEmail(String Email  ){

        Optional<User> optional= userDAO.findUserByEmail(Email) ;

        if(optional.isPresent()){

         return userMapper.returnUserDto(optional.get());
        }



            throw new EntityNotFoundException("User not found for email  ::  "+Email )  ;





    }

    public UserDto getUserByUsername(String username) {

        User user= userDAO.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found for username: " + username));

        return userMapper.returnUserDto(user) ;

    }

    public UserDto getUserbyId(Long id ){
        Optional<User> optional= userDAO.findById(id) ;

        if(optional.isPresent()){


            return userMapper.returnUserDto(optional.get());
        }

            throw new RuntimeException("Student not found for id  ::  "+id  )  ;







    }

    public UserDto createNewUser(CreateUserDto createUserDto ){


        userValidator.validate(createUserDto);

        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        User user = userMapper.returnUser(createUserDto);
        userDAO.save(user);
        log.info("user {} is saved", user.getId());

        return userMapper.returnUserDto(user);


    }
    public String rem_user(long id ){

        userDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + id));

        userDAO.deleteById(id);
        return "User removed" ;

    }

    //manager
    public UserDto update_user(UserDto userDto){



        updateUserValidator.validate(userDto);

        User findUser=userDAO.findById(userDto.getId()).orElseThrow(()->new EntityNotFoundException("User Not found " +
                "for Update ")) ;

        User user = userMapper.returnUser(userDto);

        user.setPassword(findUser.getPassword());
        user.setToken(findUser.getToken());

         userDAO.save(user) ;

         return userDto ;


    }






    public void initManager() throws ParseException {

        String dateString = "05/12/2002";

        LocalDate date = LocalDate.parse(dateString);

        var manager = new RegisterRequest().builder()
                .firstName("ssss")
                .lastName("kakka")
                .email("helahoula@os.com")
                .password(passwordEncoder.encode("kekw"))
                .phoneNumber("50289999")
                .username("hola")
                .dateNaissance(date)
                .build();




    }

    //for all users
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal() ;


                //check if current pass is correct :
                if( !passwordEncoder.matches(request.getCurrentPassword(),user.getPassword() ) ){

                    throw new IllegalArgumentException("wrong password") ;

                }


                //check if password are the same :
                if (! request.getConfirmationPassword().equals(request.getNewPassword()))
                    throw new IllegalArgumentException("password are not the same") ;


                //update password
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));

                //save pass
                userDAO.save(user) ;


    }

    //forManager
    public void changePassword(ChangePasswordRequest request, Long userid ) {



        User user = userDAO.findById(userid).orElseThrow(()->new EntityNotFoundException("User Not found " +
                "for Update ")) ;

        //check if password are the same :
        if (! request.getConfirmationPassword().equals(request.getNewPassword()))
            throw new IllegalArgumentException("password are not the same") ;



        //update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        //save pass
        userDAO.save(user) ;


    }




    public UserDto getMyProfile(Principal connectedUser){


        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();



        return  userMapper.returnUserDto(user) ;







    }




}
