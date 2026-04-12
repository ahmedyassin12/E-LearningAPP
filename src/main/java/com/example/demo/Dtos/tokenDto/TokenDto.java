package com.example.demo.Dtos.tokenDto;

import com.example.demo.token.TokenType;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenDto {



    private Long id ;

    private String token ;

    private TokenType tokenType ;

    private boolean expired ;

    private boolean revoked ;



    private Long user_id;



}
