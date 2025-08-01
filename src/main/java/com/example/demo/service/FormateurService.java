package com.example.demo.service;
import com.example.demo.Dtos.userDto.CreateFormateurDto;
import com.example.demo.Dtos.userDto.FormateurDto;
import com.example.demo.dao.FieldDao;
import com.example.demo.dao.FormateurDAO;
import com.example.demo.dao.SkillDao;
import com.example.demo.entity.Enums.Availability;
import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.Field;
import com.example.demo.entity.Formateur;
import com.example.demo.entity.Skill;
import com.example.demo.entity.User;
import com.example.demo.mapper.UsersMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FormateurService {



    @Autowired
    private final PasswordEncoder passwordEncoder ;

        @Autowired
        private FormateurDAO formateurDAO ;

    @Autowired
    private ObjectValidator<CreateFormateurDto> CreateValidator ;

    @Autowired
    private ObjectValidator<FormateurDto> updateValidator ;
    private UsersMapper formateurMapper =new UsersMapper() ;
    @Autowired
    private SkillDao skillDao;
    @Autowired
    private FieldDao fieldDao;

    public List<FormateurDto> getallFormateur(){


            Iterable<Formateur> formateurs = formateurDAO.findAll();
            List<FormateurDto> formateurDtos=new ArrayList<>();


            formateurs.forEach(formateur ->
                    formateurDtos.add(formateurMapper.returnFormateurDto(formateur)));

            return  formateurDtos ;



        }






        public FormateurDto getFormateurByEmail(String Email  ){

            Optional<Formateur> optional=formateurDAO.findFormateurByEmail(Email) ;

            if(optional.isPresent()){


                return formateurMapper.returnFormateurDto(optional.get()) ;
            }


                throw new RuntimeException("Trainer not found for email  ::  "+Email )  ;




        }
        public FormateurDto getFormateurByFirstName(String firstName  ){

            Optional<Formateur> optional=formateurDAO.findFormateurByFirstName(firstName) ;


            if(optional.isPresent()){

                return formateurMapper.returnFormateurDto(optional.get()) ;

            }



                throw new RuntimeException("formateur not found for name  ::  "+firstName  )  ;





        }

    public FormateurDto getFormateurByUsername(String username  ){

        Optional<Formateur> optional=formateurDAO.findFormateurByUsername(username) ;


        if(optional.isPresent()){

formateurMapper.returnFormateurDto(optional.get());

        }


            throw new RuntimeException("formateur not found for username  ::  "+username  )  ;





    }
    public FormateurDto getFormateurById(long formateurId  ){

        Optional<Formateur> optional=formateurDAO.findById(formateurId) ;


        if(optional.isPresent()){


            formateurMapper.returnFormateurDto(optional.get());

        }


            throw new EntityNotFoundException("formateur not found for id  ::  "+formateurId  )  ;




    }

        public FormateurDto createNewFormateur(CreateFormateurDto createFormateurDto ){

        createFormateurDto.setRole(Role.FORMATEUR);
            CreateValidator.validate(createFormateurDto);

            createFormateurDto.setPassword(passwordEncoder.encode(createFormateurDto.getPassword()));

            HashSet<Skill>skills=new HashSet<>();
            for( String name : createFormateurDto.getSkillNames() ) {


                skills.add(

                        skillDao.findByName(name).orElseThrow(
                                ()->new EntityNotFoundException("skill  not found invalid name: " +name+" !!"))

                )  ;


            }

            Field field=fieldDao.getFieldByFieldName(createFormateurDto.getFieldName()).orElseThrow(()->new EntityNotFoundException(
                    "field not found invalid name : "+createFormateurDto.getFieldName() + "!!"
            ));

            Formateur formateur=formateurMapper.returnFormateur(createFormateurDto);
            formateur.setSkills(skills);
            formateur.setField(field);
            formateurDAO.save(formateur);

            return formateurMapper.returnFormateurDto(formateur);


        }
       /* public void initFormateur() throws ParseException {

            String dateString = "05/12/2002";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            LocalDate date = LocalDate.parse(dateString);


            var formateur = new FormateurRequest().builder()
                    .firstName("formateur")
                    .lastName("zozo")
                    .email("trao@os.com")
                    .password("naLambou123§")
                    .phoneNumber("12321085")
                    .username("jugking")
                    .dateNaissance(date)
                    .Skills("java")
                    .field("programming")
                    .availability("always")
                    .experience_time(2)
                    .build();







        }
*/

        public String rem_formateur(long id ){

            formateurDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("formateur not found with id: " + id));

            formateurDAO.deleteById(id);

            return  "formateur removed successfully";


        }



        public FormateurDto update_formateur(FormateurDto formateurDto){

            Formateur formateur = formateurDAO.findById(formateurDto.getId()).orElseThrow
                    (()->new EntityNotFoundException("formateur NOt found ")) ;

            formateurDto.setRole(Role.FORMATEUR);
            updateValidator.validate(formateurDto);
            Set<Skill> skills = new HashSet<>();

            for( String name : formateurDto.getSkillNames() ) {


               skills.add(

                       skillDao.findByName(name).orElseThrow(
                               ()->new EntityNotFoundException("skill  not found invalid name "+name+" !!") )

               )  ;


            }

            Field field=fieldDao.getFieldByFieldName(formateurDto.getFieldName()).orElseThrow(
                    ()->new EntityNotFoundException("field " + "not found invalid Name !! "));



            //update only these ( others I already assign for each its own endPoint cuz they are sensible exp : password) :
            formateur.setExperienceYears(formateurDto.getExperience_time());
            formateur.setDateNaissance(formateurDto.getDateNaissance());
            formateur.setEmail(formateurDto.getEmail());
            formateur.setFirstName(formateurDto.getFirstName());
            formateur.setLastName(formateurDto.getLastName());
            formateur.setPhoneNumber(formateurDto.getPhoneNumber());
            formateur.setUsername(formateurDto.getUsername());
            formateur.setField(field);
            formateur.setSkills(skills);
            formateur.setAvailability(formateurDto.getAvailability());
            formateurDAO.save(formateur) ;
//11

            return formateurMapper.returnFormateurDto(formateur);




        }



        public FormateurDto getMyProfile(Principal connectedUser){


            Formateur formateur = (Formateur) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();



            return  formateurMapper.returnFormateurDto(formateur) ;







        }


    public String UpdateAvailability(Availability availability ,Principal connectedUser){




        if(availability==null){


            throw new IllegalArgumentException("availability must not be null");


        }

        Formateur formateur = (Formateur) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        formateur.setAvailability(availability);

        formateurDAO.save(formateur);

        return "now U are "+availability;

    }



    }



