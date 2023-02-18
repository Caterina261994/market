package com.market.market.conf;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
@PropertySource("classpath:application.properties")
@Log4j2
public class RedisConf {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.database}")
    private Integer redisDatabase;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        configuration.setPassword(RedisPassword.of(redisPassword));
        configuration.setDatabase(redisDatabase);

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder = JedisClientConfiguration.builder();
        jedisClientConfigurationBuilder.connectTimeout(Duration.ofSeconds(60));
        return new JedisConnectionFactory(configuration, jedisClientConfigurationBuilder.build());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory) {
        if(jedisConnectionFactory == null) {
            log.error("Redis template Service is not available.");
            return null;
        }
        log.info("Redis connection factory: host-{}. port-{}", jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort());
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }
}