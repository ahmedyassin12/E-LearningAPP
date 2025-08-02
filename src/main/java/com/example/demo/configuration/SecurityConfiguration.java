package com.example.demo.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.demo.entity.Enums.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    @Autowired
    private final JwtAuthenticationFilter jwtAuthFilter;


    @Autowired
    private final AuthenticationProvider authenticationProvider;


    @Autowired
    private final LogoutHandler logoutHandler ;


    private static final String[] WHITE_LIST_URL = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html","/api/v1/**","/api/v1/auth/verify?token/**," +
            "https://e-learningapp-5dxn.onrender.com"};

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http ) throws Exception {


    http


            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req->


                         req.requestMatchers("/api/v1/**",
                                         "/v3/api-docs",
                                         "/v3/api-docs/**",
                                         "/swagger-resources",
                                         "/swagger-resources/**",
                                         "/configuration/ui",
                                         "/configuration/security",
                                         "/swagger-ui/**",
                                         "/webjars/**",
                                         "/swagger-ui.html"



                                         )
                         .permitAll()

                                 //Payment AUTHENTICATION
                                 .requestMatchers(GET,"/api/v9/Paiement/getAllPaiements").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/api/v9/Paiement/getPaiementsOfEnrollement/{enrollementId}").hasAnyRole(
                                         MANAGER.name(),STUDENT.name())
                                 .requestMatchers(GET,"/api/v9/Paiement/getPaiementByStudent_id/{student_id}").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/api/v9/Paiement/getPaiementById/{id}").hasRole(MANAGER.name())


                                 .requestMatchers(POST,"/api/v9/Paiement/createPaiement").hasRole(MANAGER.name())
                                 .requestMatchers(PUT,"/api/v9/Paiement/updatePaiement").hasRole(MANAGER.name())
                                 .requestMatchers(DELETE,"/api/v9/Paiement/deletePaiement/{id}").hasRole(MANAGER.name())

                                 //StudentImage AUTHENTICATION
                 .requestMatchers(DELETE,"/api/v6/images/delete_image/{id}").hasAnyRole(STUDENT.name())
                                 .requestMatchers(POST,"/api/v6/images/upload_image/{student_id}").hasAnyRole(STUDENT.name())



                 //User AUTHENTICATION
                 .requestMatchers(GET,"/api/users/getAllUsers").hasAnyRole(MANAGER.name())
                 .requestMatchers(GET,"/api/users/getUserById/{id}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/api/users/findUserByEmail/{email}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/api/users/getUserByUsername/{username}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/api/users/getManagerByName/{name}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/managerGetMyProfile").hasRole(MANAGER.name())

                 .requestMatchers(POST,"/api/users/createNewUser").hasRole(MANAGER.name())
                 .requestMatchers(PUT,"/api/users/updateUser").hasRole(MANAGER.name())
                 .requestMatchers(DELETE,"/api/users/deleteUser/{id}").hasRole(MANAGER.name())
                                 .requestMatchers(PATCH,"api/users/changePassword").hasAnyRole(MANAGER.name(),STUDENT.name(),FORMATEUR.name())
                                 .requestMatchers(PATCH,"api/users/ManagerChangePassword").hasRole(MANAGER.name())

                                 //Formation AUTHENTICATION
                 .requestMatchers(GET,"/getAllFormation").hasAnyRole(MANAGER.name(),STUDENT.name())
                 .requestMatchers(GET,"/getFormationsforEnrolledStudent").hasAnyRole(STUDENT.name())
                 .requestMatchers(GET,"/getFormationsForFormateur").hasAnyRole(FORMATEUR.name())
                 .requestMatchers(GET,"/getFormationById/{id}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/getFormationByName/{name}").hasAnyRole(MANAGER.name(),STUDENT.name(),FORMATEUR.name())


                 .requestMatchers(POST,"/createNewFormation").hasRole(MANAGER.name())
                 .requestMatchers(POST,"/upload_FormationImage/{formation_id}").hasRole(MANAGER.name())
                 .requestMatchers(PUT,"/update_formation").hasRole(MANAGER.name())
                 .requestMatchers(DELETE,"/rem_formation/{id}").hasRole(MANAGER.name())
                .requestMatchers(DELETE,"/delete_imageForFormation/{id}").hasRole(MANAGER.name())

                                 //Formation_Image auth :
                                 .requestMatchers(POST,"/upload_FormationImage/{formation_id}").hasRole(MANAGER.name())
                                 .requestMatchers(DELETE,"/delete_imageForFormation/{id}").hasRole(MANAGER.name())

                                 //formateur AUTHENTICATION

                 .requestMatchers(GET,"/getAllFormateurs").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/getFormateurByNom/{nom}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/getFormateurByUsername/{username}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/getFormateurByid/{id}").hasAnyRole(MANAGER.name())
                 .requestMatchers(GET,"/getFormateurByEmail/{Email}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/formateurGetMyProfile").hasRole(FORMATEUR.name())
                 .requestMatchers(GET,"/UpdateAvailability").hasRole(FORMATEUR.name())

                 .requestMatchers(POST,"/createNewFormateur").hasRole(MANAGER.name())
                 .requestMatchers(PUT,"/update_formateur").hasAnyRole(MANAGER.name(),FORMATEUR.name())
                 .requestMatchers(DELETE,"/rem_formateur/{id}").hasRole(MANAGER.name())

                                 //Student AUthentication
                                 .requestMatchers(GET,"/getAllStudents").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/getStudentByNom/{nom}").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/getStudentByUsername/{username}").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/getStudentOfFormation/{formation_id}").hasAnyRole(MANAGER.name(),FORMATEUR.name())
                                 .requestMatchers(GET,"/getStudentByEmail/{Email}").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/getStudentById/{id}").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/StudentGetMyProfile").hasRole(STUDENT.name())


                                 .requestMatchers(POST,"/createNewStudent").hasRole(MANAGER.name())
                                 .requestMatchers(PUT,"/update_student").hasAnyRole(MANAGER.name(),STUDENT.name())
                                 .requestMatchers(DELETE,"/rem_student/{id}").hasRole(MANAGER.name())

                 //eventEnrollement AUTHENTICATION
                  .requestMatchers(GET,"/getEventEnrollementForEvent/{event_id}").hasAnyRole(MANAGER.name())
                 .requestMatchers(GET,"/getAllEventEnrollements").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/getEventEnrollementForStudent").hasRole(STUDENT.name())
                 .requestMatchers(GET,"/getEventEnrollementForStudent/{studentId}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/getEventEnrollementById/{id}").hasRole(MANAGER.name())

                 .requestMatchers(POST,"/createNewEventEnrollement").hasAnyRole(STUDENT.name(),MANAGER.name())
                 .requestMatchers(POST,"/EnrollInEvent/{event_name}").hasRole(STUDENT.name())
                 .requestMatchers(PUT,"/updateEventEnrollement").hasRole(MANAGER.name())
                 .requestMatchers(DELETE,"/removeEventEnrollement/{id}").hasRole(MANAGER.name())


        //EVENT AUTHENTICATION
                 .requestMatchers(GET,"/api/v5/event/getAllEvents").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/api/v5/event/getEventforEnrolledStudent/{studentId}").hasAnyRole(STUDENT.name(),MANAGER.name())
                 .requestMatchers(GET,"/api/v5/event/getEventByName/{name}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/api/v5/event/getEventById/{id}").hasRole(MANAGER.name())

                 .requestMatchers(POST,"/api/v5/event/createNewEvent").hasRole(MANAGER.name())
                 .requestMatchers(PUT,"/api/v5/event/update_event").hasRole(MANAGER.name())
                 .requestMatchers(DELETE,"/api/v5/event/rem_event/{id}").hasRole(MANAGER.name())

            // Event_Image :

            .requestMatchers(POST,"/upload_EventImage/{event_id}").hasRole(MANAGER.name())
            .requestMatchers(POST,"/delete_imageForEvent/{id}").hasRole(MANAGER.name())

            //COURSE CLOUD :

                 .requestMatchers("/api/v2/courseCloud/**").hasRole(MANAGER.name())

              //   Courses AUTHENTICATION
                 .requestMatchers(GET,"/api/v3/course/getAllCourses").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/api/v3/course/getStudentCourses/{student_id}/{formation_id}").hasRole(STUDENT.name())
                 .requestMatchers(GET,"/api/v3/course/getFormateurCourses/{formateur_id}/{formation_id}").hasRole(FORMATEUR.name())
                 .requestMatchers(GET,"/api/v3/course/getFormationCourses/{formation_id}").hasAnyRole(MANAGER.name(),FORMATEUR.name()
                                         ,STUDENT.name())
                 .requestMatchers(GET,"/api/v3/course/getCourseById/{id}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/api/v3/course/getCourseByNom/{nom}").hasAnyRole(MANAGER.name(),STUDENT.name(),FORMATEUR.name())

                 .requestMatchers(POST,"/api/v3/course/createNewCourse").hasRole(MANAGER.name())
                 .requestMatchers(PUT,"/api/v3/course/update_Course").hasRole(MANAGER.name())
                 .requestMatchers(DELETE,"/api/v3/course/rem_course/{id}").hasRole(MANAGER.name())

                 //   Enrollement AUTHENTICATION


                 .requestMatchers(GET,"/getAllEnrollements").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/getEnrollementsForStudent/{studentId}").hasRole(MANAGER.name())
                 .requestMatchers(GET,"/getEnrollementsForStudent").hasRole(STUDENT.name())

                 .requestMatchers(GET,"/getFormationEnrollement/{formation_id}").hasAnyRole(MANAGER.name())
                 .requestMatchers(GET,"/getEnrollementById/{id}").hasRole(MANAGER.name())

                 .requestMatchers(POST,"/createNewEnrollement").hasRole(MANAGER.name())
                 .requestMatchers(POST,"/EnrollInFormation/{formation_name}").hasRole(STUDENT.name())

                 .requestMatchers(PUT,"/updateEnrollement").hasRole(MANAGER.name())
                 .requestMatchers(DELETE,"/removeEnrollement/{id}").hasRole(MANAGER.name())


                                 //Field Authentication
                                 .requestMatchers(GET,"/api/fields/getAllFields").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/api/fields/getFieldById/**").hasRole(MANAGER.name())
                                 .requestMatchers(POST,"/api/fields/createField").hasRole(MANAGER.name())
                                 .requestMatchers(PUT,"/api/fields/updateField").hasRole(MANAGER.name())
                                 .requestMatchers(DELETE,"/api/fields/deleteField/**").hasRole(MANAGER.name())

                                 //Skill Authentication :
                                 .requestMatchers(GET,"/getAllSkills").hasRole(MANAGER.name())
                                 .requestMatchers(GET,"/getSkillByID/{id}").hasRole(MANAGER.name())
                                 .requestMatchers(POST,"/addSkill").hasRole(MANAGER.name())
                                 .requestMatchers(PUT,"/updateSkill").hasRole(MANAGER.name())
                                 .requestMatchers(DELETE,"/deleteSkill/{id}").hasRole(MANAGER.name())

                                 .anyRequest()
                 .authenticated()

            )



          .sessionManagement(session -> session.sessionCreationPolicy(STATELESS) )

            .authenticationProvider(authenticationProvider)

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

      .logout(

              logout->
            logout.logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler(
                (request,response,authentication)->
        SecurityContextHolder.clearContext()

                )
      )
        ;

    return http.build() ;

}


}
