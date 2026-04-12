package com.example.demo.cacheConfig;

import com.example.demo.Dtos.formationDto.FormationStudentDto;
import com.example.demo.Dtos.tokenDto.TokenDto;
import com.example.demo.token.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class redisConfig {


    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){

        //setting up objectMapper for redis inorder to read Localdate
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        // Serializers using the configured ObjectMapper
        Jackson2JsonRedisSerializer<TokenDto> tokenSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,TokenDto.class);


      RedisCacheConfiguration formationStudentDtoConfiguration=  RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer<>(FormationStudentDto.class)));

        RedisCacheConfiguration TokenConfiguration=  RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(7))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(tokenSerializer)) ;


        RedisCacheConfiguration genericConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

// Map each cache name to its config
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("studentFormation", formationStudentDtoConfiguration);   // only FormationStudentDto
        cacheConfigs.put("AllStudentFormations", formationStudentDtoConfiguration);
        cacheConfigs.put("GetCourses", genericConfig);
        cacheConfigs.put("JwtTokens", TokenConfiguration);
        cacheConfigs.put("GetCourse", genericConfig);

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(genericConfig)        // fallback for any unnamed cache
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }



}
