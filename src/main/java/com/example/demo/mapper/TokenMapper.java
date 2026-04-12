package com.example.demo.mapper;

import com.example.demo.Dtos.tokenDto.TokenDto;
import com.example.demo.token.Token;

public class TokenMapper {


    public TokenDto returnTokenDto(Token token){


        return TokenDto.builder()
                .id(token.getId())
                .tokenType(token.getTokenType())
                .expired(token.isExpired())
                .revoked(token.isRevoked())
                .token(token.getToken())
                .user_id(token.getUser().getId())
                .build();

    }


}
