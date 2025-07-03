package com.example.demo.service;

import com.example.demo.Dtos.formationDto.CreateFormationDto;
import com.example.demo.Dtos.formationDto.FormationDto;
import com.example.demo.Dtos.formationDto.FormationManagerDto;
import com.example.demo.Dtos.formationDto.FormationStudentDto;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormateurDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.entity.Enums.Availability;
import com.example.demo.entity.Formateur;
import com.example.demo.entity.Formation;
import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.FormationMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormationService {


    @Autowired
    private FormationDAO formationDAO ;

    @Autowired
    private FormateurDAO formateurDao;
    @Autowired
    private EnrollementDAO enrollementDAO;
    @Autowired
    private ObjectValidator<CreateFormationDto> formationValidator ;



    private FormationMapper formationMapper =new FormationMapper() ;





    //manager , student
    public Iterable<?> getAllFormation(Principal connectedUser){

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Iterable<Formation>formations=formationDAO.findAll();


        if (formations.iterator().hasNext()){

            if(user.getRole().equals(Role.MANAGER)){

                List<FormationManagerDto>formationManagerDtos=new ArrayList<>();

                formations.forEach(formation ->
                        formationManagerDtos.   add(
                                formationMapper.returnformationManagerDto(formation)
                        ) );


                return formationManagerDtos ;

            }

            else if (user.getRole() == Role.STUDENT) {

                boolean isPaid;
                boolean isEnrolled;

                List<FormationStudentDto>formationStudentDtos=new ArrayList<>() ;

                for(Formation formation:formations){
                    isEnrolled=enrollementDAO.isStudentEnrolled(user.getId(), formation.getId()) ;
                    isPaid= enrollementDAO.isEnrollmentPaid(user.getId(), formation.getId());

                    formationStudentDtos.add(formationMapper.returnformationStudentDto(formation,isPaid,isEnrolled));


                }

                return formationStudentDtos ;

            }
        }

        throw  new EntityNotFoundException("Formations are empty") ;


    }

    //formateur
    public Iterable<?> getFormationsForFormateur(Principal connectedUser ) {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if( ! user.getRole().equals(Role.FORMATEUR))  throw new IllegalArgumentException("this only accessible by formateurs !!!");

       Iterable< Formation >  formations=formationDAO.getFormationsForFormateur(user.getId());

       if(formations.iterator().hasNext()){
           List<FormationDto>formationDtos=new ArrayList<>();

           formations.forEach(formation ->
                   formationDtos.add(
                           formationMapper.returnformationDto(formation)
                   ) );

           return formationDtos ;

       }

       throw new EntityNotFoundException("Formations are empty");

    }

    //manager
    public void updateFormationImage(Long formationId, String imageUrl,String publicID) {
        Optional<Formation> optionalFormation = formationDAO.findById(formationId);

        if (!optionalFormation.isPresent()) {


            throw new IllegalArgumentException("formation not found with ID: " + formationId);


        }

        Formation formation = optionalFormation.get();
        formation.setImageUrl(imageUrl);
        formation.setPublicId(publicID);
        System.out.println("public id in updation kekkkkwww  : "+formation.getPublicId());
        // Save the updated student object back to the database
        formationDAO.save(formation);
        System.out.println("image updated");


    }





    //manager
    public String  getPublicIdFromFormationData(Long id ){



        Formation formation= formationDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formation not found "));

        String public_id ;


            public_id = formation.getPublicId() ;

            if(public_id!=null) return public_id ;

            throw new NullPointerException("public_id is null ") ;





    }
    //student
    public List<FormationStudentDto> getFormationsforEnrolledStudent(Principal connectedUser ) {


        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (! user.getRole().equals(Role.STUDENT)) throw new IllegalArgumentException("this is only accessible by Students !!");

        Iterable<Formation>formations=formationDAO.getFormationsforEnrolledStudent(user.getId()) ;

        if(formations.iterator().hasNext() || formations==null){

            List<FormationStudentDto>formationDtos=new ArrayList<>();
           boolean isPaid;


            for(Formation formation:formations){
                isPaid= enrollementDAO.isEnrollmentPaid(user.getId(), formation.getId()) ;
                formationDtos.add(
                        formationMapper.returnformationStudentDto(formation,isPaid,true)
                ) ;

            }

            return  formationDtos;
        }

        throw  new EntityNotFoundException("formation is Empty");

    }

    //manager
    public FormationManagerDto getFormationById(Long id ){
        Optional<Formation> optional=formationDAO.findById(id) ;

        Formation formation;
        if(optional.isPresent()){

            formation=optional.get();
            return formationMapper.returnformationManagerDto(formation) ;

        }


            throw new RuntimeException("formation not found for id  ::  "+id  )  ;







    }



    //formateur,student,manager

    public Object getFormationByName(String formationName, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Handle Formateur case first
        if (user.getRole() == Role.FORMATEUR) {
            Formation formation = formationDAO.findFormationByNameForFormateur(formationName, user.getId())
                    .orElseThrow(() -> new AccessDeniedException(
                            "Formateur not associated with formation: " + formationName
                    ));
            return formationMapper.returnformationDto(formation) ;


        }

        // Handle other roles
        Formation formation = formationDAO.findFormationByName(formationName)
                .orElseThrow(() -> new EntityNotFoundException(formationName));

        if (user.getRole() == Role.STUDENT) {

            boolean isPaid= enrollementDAO.isEnrollmentPaid(user.getId(), formation.getId());
            boolean isEnrolled= enrollementDAO.isStudentEnrolled(user.getId(), formation.getId());

            return formationMapper.returnformationStudentDto(formation,isPaid,isEnrolled);

        }

        if (user.getRole() == Role.MANAGER) {
            return  formationMapper.returnformationManagerDto(formation);
        }

        throw new AccessDeniedException("Unauthorized role: " + user.getRole());
    }


    //manager
    public FormationManagerDto  createNewFormation(CreateFormationDto createFormationDto ){



        formationValidator.validate(createFormationDto);


            Set<Formateur>formateurs = new HashSet<>() ;
           Optional <Formateur> formateur ;
            for(Long i : createFormationDto.getFormateur_ids()){



                formateur=formateurDao.findById(i) ;
                if(formateur.isPresent()&&formateur.get().getAvailability().equals(Availability.AVAILABLE)){
                    formateurs.add( formateur.get()  ) ;
                }
                else {
                    System.out.println("u cant assign formateur "
                            + formateur.get().getFirstName() + " because he is not availabale!");
                }



            }

            Formation formation =formationMapper.ToFormationForCreation(createFormationDto,formateurs);

            formationDAO.save(formation);
            log.info("Formation {} is saved", formation.getId());

            return formationMapper.returnformationManagerDto(formation);






    }

    public void rem_Formation(Long id ){

        formationDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("formation not found with id: " + id));

        formationDAO.deleteById(id);


    }







    //manager
    public FormationDto update_formation(CreateFormationDto createFormationDto) {


        formationValidator.validate(createFormationDto);
        Optional<Formation> formation = formationDAO.findById(createFormationDto.getFormationDto().getFormation_id());


        if (formation.isPresent()) {
            Set<Formateur> formateurs = new HashSet<>();
            Optional<Formateur> formateur;
            for (Long i : createFormationDto.getFormateur_ids()) {


                formateur = formateurDao.findById(i);
                if (formateur.isPresent()&&formateur.get().getAvailability().equals(Availability.AVAILABLE)) {
                    formateurs.add(formateur.get());
                }
                else {

                    System.out.println("u cant assign formateur "+formateur.get().getFirstName() +" because he is not availabale!");

                }


            }

            formation.get().setFormateurs(formateurs);

            Formation updated_formation = formationMapper.returnFormation(createFormationDto, formation.get());

            formationDAO.save(updated_formation);

            return formationMapper.returnformationDto(updated_formation);


        }

        throw new EntityNotFoundException("cant find formation to update !! ");
    }



}
