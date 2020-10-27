package cc.htdf.msgcloud.redis.plugin;

import cc.htdf.msgcloud.redis.serializer.FastJson2JsonRedisSerializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * author: JT
 * date: 2020/8/8
 * title:
 */
@Configuration
public class RedisTemplateSerializer {

    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    public void redisTemplateSerializer() {
        Map<String, RedisTemplate> map = applicationContext.getBeansOfType(RedisTemplate.class);
        FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer(Object.class);
        for (RedisTemplate template : map.values()) {
            //redis开启事务
            template.setEnableTransactionSupport(true);
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(fastJson2JsonRedisSerializer);
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(fastJson2JsonRedisSerializer);
            template.setDefaultSerializer(new StringRedisSerializer());
            template.afterPropertiesSet();
        }
    }
}
