package com.thrift.hft.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {


    @Autowired
    private RedisTemplate redisTemplate;


    public <T> T get(String key,Class<T> entityClass){
        try {
            String jsonString = (String) redisTemplate.opsForValue().get(key);
            if (jsonString != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonString, entityClass);
            }else
                return null;
        }catch (Exception e){
            log.error("Error",e);
            return null;
        }
    }

    public void set(String key,Object o ,Long ttl){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(o);
             redisTemplate.opsForValue().set(key,jsonString,ttl, TimeUnit.HOURS);
        }catch (Exception e){
            log.error("Error",e);
        }
    }


}
