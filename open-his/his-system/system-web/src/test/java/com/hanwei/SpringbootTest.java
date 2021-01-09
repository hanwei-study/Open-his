package com.hanwei;

import com.hanwei.service.DictTypeService;
import org.apache.shiro.dao.DataAccessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Random;

/**
 * @author hanwei
 * @ClassName Test
 * @date 2020/11/7
 */
@SpringBootTest
public class SpringbootTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() {
        redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) {
                connection.flushAll();
                connection.flushDb();
                return "ok";
            }
        });
    }

}
