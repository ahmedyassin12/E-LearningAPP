    package com.example.demo.configuration;

    import com.example.demo.dao.TokenDAO;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jdk.swing.interop.SwingInterOpUtils;
    import lombok.NonNull;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;

    @Component
    @RequiredArgsConstructor
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final  UserDetailsService userDetailsService ;
        private final TokenDAO tokenDAO ;


        @Override
        protected void doFilterInternal(
         @NonNull HttpServletRequest request,
         @NonNull   HttpServletResponse response,
         @NonNull     FilterChain filterChain
        ) throws ServletException, IOException {
            if (request.getServletPath().contains("/api/v1/auth")) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader=request.getHeader("Authorization") ;
            final String jwt  ;
            final String Username;

            System.out.println("authHeader = "+authHeader);
            if(authHeader==null|| !authHeader.startsWith("Bearer ") ){

                System.out.println("blocker  0");
                filterChain.doFilter(request,response);
                return ;

            }
            System.out.println("blocker  1");

            jwt=authHeader.substring(7) ;

            Username=jwtService.extractUserName(jwt) ;
            System.out.println("blocker  2 : "+Username);

            if(Username!=null && SecurityContextHolder.getContext().getAuthentication()==null){


                UserDetails userDetails = this.userDetailsService.loadUserByUsername(Username) ;

                var isTokenValid = tokenDAO.findByToken(jwt) .map(t->!t.isExpired()&&!t.isRevoked())
                        .orElse(false) ;



                    if(jwtService.IsTokenValid(jwt,userDetails) &&isTokenValid) {


                        UsernamePasswordAuthenticationToken authToken =new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()


                    ) ;

                        authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)

                    );

                        SecurityContextHolder.getContext().setAuthentication(authToken);



                }


            }

            filterChain.doFilter(request ,response);


        }




    }
