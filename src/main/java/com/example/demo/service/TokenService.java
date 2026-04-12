package com.example.demo.service;

import com.example.demo.Dtos.tokenDto.TokenDto;
import com.example.demo.dao.TokenDAO;

import com.example.demo.mapper.TokenMapper;
import com.example.demo.token.Token;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class TokenService {




    @Autowired
    private TokenDAO tokenDAO ;


    private TokenMapper tokenMapper= new TokenMapper();

    @Cacheable(cacheNames = "JwtTokens", key ="#token")
    public TokenDto getToken(String token){


        System.out.println("fetching from database hohoho ");

        Token token1= tokenDAO.findByToken(token).orElseThrow
                (() ->new EntityNotFoundException(("Token not found for token :  " +token) ) ) ;


        return tokenMapper.returnTokenDto(token1) ;

    }







}
