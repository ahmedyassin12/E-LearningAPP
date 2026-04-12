package com.example.demo.dao;

import com.example.demo.token.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenDAO extends CrudRepository<Token,Long> {


    @Query("""
            select t from Token t inner join User u on t.user.id=u.id
            
            where u.id=:userId and (t.expired=false and  t.revoked=false)
            
            
            """)
    List<Token> findAllValidTokenByUser(Long userId );


    @Query("""
            select t from Token t inner join User  u on t.user.id=u.id
            
            where u.id=:userId and (t.expired=false and  t.revoked=false and t.tokenType='BEARER')
            
            
            """)
    List<Token> findAllValidBearerTokenByUser(Long userId );


    @Modifying
    @Query("UPDATE Token t SET t.expired = true, t.revoked = true WHERE t.user.id = :userId")
    void revokeAllTokensByUser(Long userId);

    @Modifying
    @Query("UPDATE Token t SET t.expired = true, t.revoked = true" +
            " WHERE t.user.id = :userId AND t.tokenType = 'BEARER'")
    void revokeBearerTokensByUser(Long userId);


    Optional<Token> findByToken(String token ) ;


}
