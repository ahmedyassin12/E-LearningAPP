package com.example.demo.entity.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.entity.Enums.Permission.*;

@RequiredArgsConstructor
public enum Role {


    STUDENT (
            Set.of(
                    STUDENT_READ,
                   STUDENT_UPDATE,
                    STUDENT_ENROLL

            )


    ),

    FORMATEUR  (
            Set.of(
                    FORMATEUR_UPDATE,
                    FORMATEUR_READ
            )
    ),

    MANAGER    (
            Set.of(
                    MANAGER_CREATE,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_READ,

                  FORMATEUR_UPDATE,
                    FORMATEUR_READ,

                    STUDENT_UPDATE,
                    STUDENT_READ,
                    STUDENT_ENROLL




            )
    )

    ;




      @Getter
  private final Set<Permission> permissions;



public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name() ));
        return authorities;
    }


}
