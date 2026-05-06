package com.example.demo.service;

public class FormationCachedServiceIntegrationTest {



    /*


     // getFormationByName tests
    @Test
    void getFormationByName_FormateurRole_ReturnsFormation() {




        mockUser.setRole(Role.FORMATEUR);
        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.findFormationByNameForFormateur(anyString(), anyLong()))
                .thenReturn(Optional.of(testFormation));


        when(formationMapper.returnformationDto(testFormation)).thenReturn(formationDto);
        Object result = formationService.getFormationByName("Test", authentication);

        Assertions.assertThat(result).isNotNull();

    }



      // getAllFormation tests
    @Test
    void getAllFormation_ManagerRole_ReturnsFormations() {
        mockUser.setRole(Role.MANAGER);
        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);
        FormationManagerDto expectedDto = FormationManagerDto.builder()
                .formation_id(testFormation.getId())
                .formation_name(testFormation.getFormationName())
                .description(testFormation.getDescription())
                .date(testFormation.getDate())
                .imageUrl(testFormation.getImageUrl())
                .publicId(testFormation.getPublicId())
                .build();

        when(formationDAO.findAll()).thenReturn(List.of(testFormation) );
        when(formationMapper.returnformationManagerDto(testFormation)).thenReturn(expectedDto);

        Iterable<?> result = formationService.getAllFormation(authentication);

        assertTrue(result.iterator().hasNext());
        assertEquals(expectedDto, result.iterator().next());


    }



    @Test
    void getAllFormation_StudentRole_ReturnsDTOs() {

        FormationStudentDto testFormationDto = FormationStudentDto.builder()
                .formation_name("Java Fundamentals")
                .formation_id(testFormation.getId())
                .isStudentEnrolled(true)
                .IsEnrollementpaid(true)
                .date(LocalDate.now())
                .build();


        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.findAll()).thenReturn((Iterable) List.of(testFormation) );
        when(enrollementDAO.isStudentEnrolled(mockUser.getId(), testFormation.getId())).thenReturn(true);
        when(enrollementDAO.isEnrollmentPaid(mockUser.getId(), testFormation.getId())).thenReturn(true);
        when(formationMapper.returnformationStudentDto(testFormation, true, true))
                .thenReturn(testFormationDto);

        Iterable<?> result = formationService.getAllFormation(authentication);

        assertTrue(result.iterator().hasNext());
        verify(formationMapper).returnformationStudentDto(any(), anyBoolean(), anyBoolean());
    }

    @Test
    void getAllFormation_EmptyFormations_ThrowsException() {
        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,
                () -> formationService.getAllFormation(authentication));
    }


     */
}
