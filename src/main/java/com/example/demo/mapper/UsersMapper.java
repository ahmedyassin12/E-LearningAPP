package com.example.demo.mapper;
import com.example.demo.Dtos.userDto.*;
import com.example.demo.entity.Formateur;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsersMapper {



    //UserMapper(Manager)

    public UserDto returnUserDto(User user ){


        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .username(user.getUsername())
                .dateNaissance(user.getDateNaissance())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())

                .build() ;

    }







    public User returnUser(CreateUserDto createUserDto){


        return User.builder()
                .id(createUserDto.getId())
                .email(createUserDto.getEmail())
                .phoneNumber(createUserDto.getPhoneNumber())
                .username(createUserDto.getUsername())
                .dateNaissance(createUserDto.getDateNaissance())
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .role(createUserDto.getRole())
                .password(createUserDto.getPassword())

                .build();


    }
    public User returnUser(UserDto userDto){


        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .username(userDto.getUsername())
                .dateNaissance(userDto.getDateNaissance())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .role(userDto.getRole())

                .build();


    }


    //UserMapper(Student)

    public StudentDto returnStudentDto(Student student){


        return StudentDto.builder()
                .id(student.getId())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .username(student.getUsername())
                .dateNaissance(student.getDateNaissance())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .role(student.getRole())
                .imageUrl(student.getImageUrl())
                .age(student.getAge())
                .build() ;

    }



    public Student returnStudent(CreateStudentDto  createStudentDto){


        return Student.builder()
                .id(createStudentDto.getId())
                .email(createStudentDto.getEmail())
                .phoneNumber(createStudentDto.getPhoneNumber())
                .username(createStudentDto.getUsername())
                .dateNaissance(createStudentDto.getDateNaissance())
                .firstName(createStudentDto.getFirstName())
                .lastName(createStudentDto.getLastName())
                .role(createStudentDto.getRole())
                .imageUrl(createStudentDto.getImageUrl())
                .age(createStudentDto.getAge())
                .password(createStudentDto.getPassword())
                .publicId(createStudentDto.getPublicId())
                .build() ;

    }


    //UserMapper(Formateur)


    public FormateurDto returnFormateurDto(Formateur formateur ){

        Set<String> skills =new HashSet<>();

                formateur.getSkills().forEach(formateurSkill-> skills.add(formateurSkill.getName()));


        return FormateurDto.builder()
                .id(formateur.getId())
                .email(formateur.getEmail())
                .phoneNumber(formateur.getPhoneNumber())
                .username(formateur.getUsername())
                .dateNaissance(formateur.getDateNaissance())
                .firstName(formateur.getFirstName())
                .lastName(formateur.getLastName())
                .experience_time(formateur.getExperienceYears())
                .availability(formateur.getAvailability())
                .role(formateur.getRole())
                .SkillNames(skills)
                .fieldName(formateur.getField().getFieldName())
                .build() ;

    }
    public Formateur returnFormateur(CreateFormateurDto createFormateurDto ){


        return Formateur.builder()
                .id(createFormateurDto.getId())
                .email(createFormateurDto.getEmail())
                .phoneNumber(createFormateurDto.getPhoneNumber())
                .username(createFormateurDto.getUsername())
                .dateNaissance(createFormateurDto.getDateNaissance())
                .firstName(createFormateurDto.getFirstName())
                .lastName(createFormateurDto.getLastName())
                .experienceYears(createFormateurDto.getExperience_time())
                .availability(createFormateurDto.getAvailability())
                .password(createFormateurDto.getPassword())
                .role(createFormateurDto.getRole())
                .build() ;

    }




}
