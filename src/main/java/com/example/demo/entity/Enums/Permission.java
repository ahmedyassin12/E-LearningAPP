package com.example.demo.entity.Enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;



    @RequiredArgsConstructor
    public enum Permission {

        STUDENT_READ("student:read"),
        STUDENT_UPDATE("student:update"),
        STUDENT_ENROLL("student:enroll"),


        FORMATEUR_READ("formateur:read"),
        FORMATEUR_UPDATE("formateur:update"),



        MANAGER_READ("manager:read"),
        MANAGER_CREATE("manager:create"),
        MANAGER_UPDATE("manager:update"),
        MANAGER_DELETE("manager:delete") ;


        @Getter
        private final String permission;

    }


