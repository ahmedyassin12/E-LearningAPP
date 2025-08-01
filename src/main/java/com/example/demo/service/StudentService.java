package com.example.demo.service;


import com.example.demo.Dtos.userDto.CreateStudentDto;
import com.example.demo.Dtos.userDto.FormateurDto;
import com.example.demo.Dtos.userDto.StudentDto;
import com.example.demo.auth.Service.AuthenticationService;
import com.example.demo.auth.Dto.StudentRequest;
import com.example.demo.dao.StudentDAO;
import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.Formateur;
import com.example.demo.entity.Student;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class StudentService {

    @Autowired
    private  PasswordEncoder passwordEncoder ;

    @Autowired
   private StudentDAO studentDAO ;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectValidator<CreateStudentDto> studentValidator ;

    @Autowired
    private ObjectValidator<StudentDto> updateStudentValidator ;

    private UsersMapper studentMapper =new UsersMapper() ;


    public List<StudentDto> getAllStudents(){


        Iterable<Student> students = studentDAO.findAll();
        List<StudentDto> studentDto=new ArrayList<>();
        students.forEach(student -> studentDto.add(studentMapper.returnStudentDto(student)));

        return  studentDto ;


    }

    public List<StudentDto> getStudentOfFormation(Long formation_id){

        Iterable<Student> students=this.studentDAO.getStudentsByFormation(formation_id);

        if(students.iterator().hasNext()) {

            List<StudentDto> studentDto=new ArrayList<>();
            students.forEach(student -> studentDto.add(studentMapper.returnStudentDto(student)));

            return  studentDto ;


        }

        throw new EntityNotFoundException("Student not found for that formation ") ;


    }

    public StudentDto getStudentByEmail(String Email  ){

        Optional<Student> optional=studentDAO.findStudentByEmail(Email) ;

        if(optional.isPresent()){



            StudentDto studentDto =studentMapper.returnStudentDto(optional.get());

            return studentDto ;

        }


            throw new RuntimeException("Student not found for email  ::  "+Email )  ;




    }
        public StudentDto getStudentByUsername(String username  ){

            log.info("Received request to fetch student with username: {}", username);

            Optional<Student> optional=studentDAO.findStudentByUsername(username) ;

            if(optional.isPresent()){


                return studentMapper.returnStudentDto(optional.get()) ;

            }


                throw new RuntimeException("Student not found for username  ::  "+username )  ;





        }
    public StudentDto getstudentbyId(Long id ){

        Optional<Student> optional=studentDAO.findById(id) ;

        if(optional.isPresent()){


            return studentMapper.returnStudentDto(optional.get());


       }

            throw new RuntimeException("Student not found for id  ::  "+id  )  ;




    }
    public StudentDto getStudentbyFirstName(String firstName  ){

        Optional<Student> optional=studentDAO.findStudentByFirstName(firstName) ;


        if(optional.isPresent()){

        return studentMapper.returnStudentDto(optional.get()) ;

        }


            throw new RuntimeException("Student not found for name  ::  "+firstName  )  ;




    }

    public StudentDto createNewStudent(CreateStudentDto createStudentDto ){

        createStudentDto.setRole(Role.STUDENT);
        studentValidator.validate(createStudentDto);
        createStudentDto.setPassword(passwordEncoder.encode(createStudentDto.getPassword()));

        Student student=studentMapper.returnStudent(createStudentDto);

        studentDAO.save(student);
        log.info("Student {} is saved", student.getId());

        return studentMapper.returnStudentDto(student);


    }














    public void initStudent() throws ParseException {

        String dateString = "05/12/2002";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        LocalDate date = LocalDate.parse(dateString);


        var student = new StudentRequest().builder()
                .firstName("ssss")
                .lastName("hohoh")
                .email("smlfjkqsmldjf@sqf.com")
                .password("smdlfjkqs")
                 .phoneNumber("2112058")
                 .username("ss")
                 .dateNaissance(date)
                .build();


    }

    public void updateStudentImage(Long studentId, String imageUrl,String publicID) {
        Optional<Student> optionalStudent = studentDAO.findById(studentId);
        if (optionalStudent.isPresent()) {

            Student student = optionalStudent.get();
            student.setImageUrl(imageUrl);
            student.setPublicId(publicID);
            // Save the updated student object back to the database
            studentDAO.save(student);
            System.out.println("image updated");
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

    }

  public String  getPublicIdFromStudentData(Long id ){

        String public_id= studentDAO.getPublicIdByStudent(id) ;

        if (public_id!=null) return public_id ;


        throw  new NullPointerException("Public Id not found ") ;



  }

    public String rem_student(Long id ){

        studentDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("student not found with id: " + id));

        studentDAO.deleteById(id);

        return "Student deleted successfully " ;

    }



    public StudentDto update_student(StudentDto studentDto){


        updateStudentValidator.validate(studentDto);

        Student student = studentDAO.findById(studentDto.getId()).orElseThrow
                (()->new EntityNotFoundException("student NOt found ")) ;

        //update only these ( others I already assign for each its endPoint cuz they are sensible) :
        student.setRole(studentDto.getRole());
        student.setAge(studentDto.getAge());
        student.setDateNaissance(studentDto.getDateNaissance());
        student.setEmail(studentDto.getEmail());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setPhoneNumber(studentDto.getPhoneNumber());
        student.setUsername(studentDto.getUsername());
         studentDAO.save(student) ;
//11
         return studentMapper.returnStudentDto(student);


    }

    public StudentDto getMyProfile(Principal connectedUser){


        Student student = (Student) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();



        return  studentMapper.returnStudentDto(student) ;







    }
}
