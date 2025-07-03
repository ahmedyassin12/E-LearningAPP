    package com.example.demo.entity;

    import com.example.demo.entity.Enums.Role;
    import com.example.demo.token.Token;
    import com.fasterxml.jackson.annotation.JsonIdentityInfo;
    import com.fasterxml.jackson.annotation.ObjectIdGenerators;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.ToString;
    import lombok.experimental.SuperBuilder;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDate;
    import java.util.Collection;
    import java.util.List;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Entity
    @SuperBuilder
    @Inheritance(strategy = InheritanceType.JOINED)
    @DiscriminatorColumn(name = "User_type", discriminatorType = DiscriminatorType.STRING)
    @DiscriminatorValue(value = "user")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @Table(name = "users")
    public class User implements UserDetails {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id ;


        @Column(name="email",nullable = false)
        private String email;

        @Column(name="username",nullable = false,unique = true)
        private String username ;

        @Column(name="password",nullable = false)
        private String password;

        @Column(name = "first_Name", nullable = false)
        private String firstName;

        @Column(name = "last_Name", nullable = false)
        private String lastName;

        @Column(name = "phoneNumber", nullable = false)
        private String phoneNumber;

        @Column(name = "dateNaissance")
        private LocalDate dateNaissance;



        @Enumerated(EnumType.STRING)
        private Role role;

        @OneToMany(mappedBy = "user" ,cascade=CascadeType.ALL)
        private List<Token> token ;

        //Enable Default = false till our User Verified
        private boolean enabled = false;


        @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {


          return role.getAuthorities() ;


      }


        public User(String username, String password) {
            this.username=username;
            this.password=password;

        }

        @Override
        public String getUsername() {
            return username;
        }


        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {

            return this.enabled;

        }


    }

