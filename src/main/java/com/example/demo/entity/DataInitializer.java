/*package com.example.demo.entity;

import com.example.demo.auth.Service.AuthenticationService;
import com.example.demo.dao.PaymentDAO;
import com.example.demo.entity.Enums.Role;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


// filling data
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollementService enrollementRepository;

    @Autowired
    private FormationService formationService ;



    @Autowired
    private PaymentDAO paiementRepository;

    @Autowired
    private EventService eventRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PaymentService paiementService ;
    @Autowired
    EventEnrollementService eventEnrollementService;

    @Autowired
    FormateurService formateurService;
    @Autowired
    private AuthenticationService authenticationService ;


    @Override
    public void run(String... args) throws Exception {
        initStudents(2);  // Specify the number of students you want to create
    }

    public void initStudents(int numberOfStudents) throws ParseException {
        String dateString = "05/12/2002";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dateString,dateFormat);
        Random random = new Random();

        var formateur = new Formateur().builder()
                .firstName("3amtek")
                .lastName("mtek")
                .email("k@os.com")
                .password(passwordEncoder.encode("aa33"))
                .phoneNumber("12321085")
                .username("toraww")
                .role(Role.FORMATEUR)
                .dateNaissance(date)
                .build();
        var formateur2= new Formateur().builder()
                .firstName("ttt")
                .lastName("tttt")
                .email("tgdg@os.com")
                .password(passwordEncoder.encode("adfgdga33"))
                .phoneNumber("12321085")
                .username("todoroki")
                .role(Role.FORMATEUR)
                .dateNaissance(date)
                .build();

        formateurService.createNewFormateur(formateur) ;
        formateurService.createNewFormateur(formateur2) ;

        // Create some formations

        Formation formation1 = new Formation.FormationBuilder()
                .nom(
                        "formation1"
                )
                .date(LocalDate.now().minusMonths(5))
                .description("Description1")
                .build()
                ;
        Formation formation2 = new Formation.FormationBuilder()
                .nom("formation2")
                .date(LocalDate.now().minusMonths(5))
                .description("Description2")
                .build()

                ;
        Set<Formateur> formateurs=new HashSet<>();
        formateurs.add(formateur);
        formateurs.add(formateur2);
        formation1.setFormateurs(formateurs);



        formationService.createNewFormation(formation1);
        formationService.createNewFormation(formation2);



        for (int i = 1; i <= numberOfStudents; i++) {
            // Create Student
            var student = new Student().builder()
                    .firstName("Student" + i)
                    .lastName("Student"+"lastname"+i)
                    .email("student" + i + "@example.com")
                    .password(passwordEncoder.encode("password" + i))
                    .phoneNumber("123456" + i)
                    .username("student" + i)
                    .age(6 + new Random().nextInt(13 - 6 + 1))
                    .role(Role.STUDENT)
                    .dateNaissance(date)

                    .build();


            studentService.createNewStudent (student);

            //create event
            Event event1 =new Event();
            event1.setDate(LocalDate.now().minusMonths(3+i));
            event1.setDescription("Description event"+i);
            event1.setLocation("Location:event"+i);
            event1.setNom("event"+i);
            eventRepository.createNewEvent(event1);

            //Enroll the student in event

            EventEnrollement eventEnrollement =EventEnrollement.builder()
                    .event(event1)
                    .student(student)
                    .Rating(5)
                    .build();


            eventEnrollementService.createNewEnrollement(eventEnrollement);
            // Enroll the student in formations
            Enrollement enrollement1 = new Enrollement();
            enrollement1.setStudent(student);
            enrollement1.setFormation(formation1);
            enrollement1.setEnrollement_date(LocalDate.now().minusMonths(4));
            enrollement1.setPayment_Status("notPaid");
            enrollement1.setRating(random.nextInt(10) + 1);

            Enrollement enrollement2 = new Enrollement();
            enrollement2.setStudent(student);
            enrollement2.setFormation(formation2);
            enrollement2.setEnrollement_date(LocalDate.now().minusMonths(5) );
            enrollement2.setPayment_Status("Paid");
            enrollement2.setRating(random.nextInt(10) + 1);





            enrollementRepository.createNewEnrollement(enrollement1);
            enrollementRepository.createNewEnrollement(enrollement2);



                //create paiements for each enrollement
            for(int j=3;j>-1;j--){
                if (i==0){
                    j--;
                }

                Payment paiement11 = Payment.builder()
                        .paymentDate(LocalDate.now().minusMonths(j+1))
                        .enrollement(enrollement1)
                        .amount(500)
                        .build();

                paiementService.createPaiement(paiement11) ;
            }

            for(int j=4;j>-1;j--){

                if(i==1) j-- ;
                Payment paiement12 = Payment.builder()
                        .paymentDate(LocalDate.now().minusMonths(j+1))
                        .enrollement(enrollement2)
                        .amount(500)
                        .build();

                paiementService.createPaiement(paiement12) ;

            }






        }








    }




    }*/