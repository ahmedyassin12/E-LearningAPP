package com.example.demo.auth.Service;

import com.example.demo.Dtos.userDto.UserDto;
import com.example.demo.configuration.JwtService;
import com.example.demo.dao.TokenDAO;
import com.example.demo.service.UserService;
import com.example.demo.token.Token;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {


    private final TokenDAO tokenDAO;

private final UserService userService;
private final JwtService jwtService;
private final CacheManager cacheManager;


    @Transactional
    @Override
    public void logout(
            HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication
    ) {

        final String authHeader=request.getHeader("Authorization") ;
        final String jwt  ;


        if(authHeader==null||!authHeader.startsWith("Bearer ") ){

            return ;

        }

        jwt=authHeader.substring(7) ;



        try{

            String username = jwtService.extractUserName(jwt);


            UserDto user = userService.getUserByUsername(username);



            List<Token> tokens = tokenDAO.findAllValidTokenByUser(user.getId()) ;



            tokens.forEach(t-> Objects.requireNonNull(cacheManager.getCache("JwtTokens")).evict(t.getToken()) );


            tokenDAO.revokeAllTokensByUser(user.getId());



        }
        catch (EntityNotFoundException e){
            return;
        }
        catch (NullPointerException e){
            return;
        }


    }
}
