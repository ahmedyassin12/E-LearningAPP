package com.example.demo.auth.Service;

import com.example.demo.dao.TokenDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {


    private final TokenDAO tokenDAO;
    @Override
    public void logout(
            HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication
    ) {

        final String authHeader=request.getHeader("Authorization") ;
        final String jwt  ;

        System.out.println("authHeader = "+authHeader);

        if(authHeader==null||!authHeader.startsWith("Bearer ") ){

            return ;

        }

        jwt=authHeader.substring(7) ;
        var storedToken=tokenDAO.findByToken(jwt)
                .orElse(null) ;

        System.out.println("storedToken ="+storedToken.getToken());

        if(storedToken!=null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenDAO.save(storedToken);

        }

    }
}
