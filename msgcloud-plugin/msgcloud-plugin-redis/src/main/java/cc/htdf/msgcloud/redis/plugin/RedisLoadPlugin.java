package cc.htdf.msgcloud.redis.plugin;

import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.common.plugin.ConfigLoad;
import cc.htdf.msgcloud.redis.annotation.EnableRedis;
import cc.htdf.msgcloud.redis.domain.RedisSource;
import cc.htdf.msgcloud.redis.exception.RedisConfigException;
import cc.htdf.msgcloud.redis.plugin.config.RedisDefaultConfigLoad;
import cc.htdf.msgcloud.redis.serializer.FastJson2JsonRedisSerializer;
import cc.htdf.msgcloud.redis.utils.BeanRegistryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/8/7
 * title:
 */
@Slf4j
public class RedisLoadPlugin implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        /**
         * 环境配置获取
         */
        String env = BeanRegistryUtil.readEnvFromBeanRegistry(beanDefinitionRegistry);
        ConfigLoad<RedisSource> configLoad = new RedisDefaultConfigLoad(env);
        Map<String, RedisSource> redisSourceMap = configLoad.load();

        /**
         * 获取注解配置的值
         */
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableRedis.class.getName());
        String[] sourceNames = (String[]) attributes.get("source");
        String defaultSource = (String) attributes.get("defaultSource");


        for (String sourceName : sourceNames) {
            RedisSource redisSource = redisSourceMap.get(sourceName);
            if (Objects.isNull(redisSource)) {
                continue;
            }

            LettuceConnectionFactory factory;

            /**
             * 设置连接池
             */
//            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//            jedisPoolConfig.setMaxTotal(redisSource.getPoolMaxActive());
//            jedisPoolConfig.setMaxIdle(redisSource.getPoolMaxIdle());
//            jedisPoolConfig.setMinIdle(redisSource.getPoolMinIdle());
//            jedisPoolConfig.setMaxWaitMillis(redisSource.getPoolMaxWait());
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMaxTotal(redisSource.getPoolMaxActive());
            poolConfig.setMaxIdle(redisSource.getPoolMaxIdle());
            poolConfig.setMinIdle(redisSource.getPoolMinIdle());
            poolConfig.setMaxWaitMillis(redisSource.getPoolMaxWait());

            LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                    .poolConfig(poolConfig).commandTimeout(Duration.ofSeconds(10000L))
                    .build();

            /**
             * 集群与非集群模式
             */
            if (redisSource.getIsCluster()) {
                RedisClusterConfiguration configuration = new RedisClusterConfiguration();
                for (String temp : redisSource.getNodes()) {
                    String[] hostport = temp.split(":");
                    RedisNode redisNode = new RedisNode(hostport[0], Integer.valueOf(hostport[1]));
                    configuration.addClusterNode(redisNode);
                }
                if (!Objects.isNull(redisSource.getPassword()) || !Objects.equals("", redisSource.getPassword())) {
                    configuration.setPassword(redisSource.getPassword());
                }
                configuration.setMaxRedirects(redisSource.getMaxRedirects());
                factory = new LettuceConnectionFactory(configuration, clientConfig);
            } else {
                RedisStandaloneConfiguration configuration;
                String hostPort = redisSource.getNodes().get(0);
                String[] conn = hostPort.split(":");
                if (conn.length == 1) {
                    configuration = new RedisStandaloneConfiguration(conn[0]);
                } else {
                    configuration = new RedisStandaloneConfiguration(conn[0], Integer.valueOf(conn[1]));
                }
                if (!Objects.isNull(redisSource.getDatabase())) {
                    configuration.setDatabase(redisSource.getDatabase());
                }
                if (!Objects.isNull(redisSource.getPassword()) || !Objects.equals("", redisSource.getPassword())) {
                    configuration.setPassword(redisSource.getPassword());
                }
                factory = new LettuceConnectionFactory(configuration, clientConfig);
            }
            factory.afterPropertiesSet();

            /**
             * 配置RedisTemplate
             */
            FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer(Object.class);
//            RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//            template.setConnectionFactory(factory);
//            //redis开启事务
//            template.setEnableTransactionSupport(true);
//            template.setKeySerializer(new StringRedisSerializer());
//            template.setValueSerializer(fastJson2JsonRedisSerializer);
//            template.setHashKeySerializer(new StringRedisSerializer());
//            template.setHashValueSerializer(fastJson2JsonRedisSerializer);
//            template.setDefaultSerializer(new StringRedisSerializer());
//            template.afterPropertiesSet();

            RedisSerializer stringSerializer = new StringRedisSerializer();
            Map<String, Object> param = new HashMap<>();
            param.put("connectionFactory", factory);
            param.put("enableTransactionSupport", true);
            param.put("keySerializer", stringSerializer);
            param.put("valueSerializer", fastJson2JsonRedisSerializer);
            param.put("hashKeySerializer", stringSerializer);
            param.put("hashValueSerializer", fastJson2JsonRedisSerializer);
            param.put("defaultSerializer", stringSerializer);

            boolean isRegister = BeanRegistryUtil.registerBeanDefinition(
                    beanDefinitionRegistry, sourceName, RedisTemplate.class, null, param
            );
            this.checkIsRegister(isRegister, sourceName);

            if (sourceName.equals(defaultSource)) {
                String beanName = "redisTemplate";
                isRegister = BeanRegistryUtil.registerBeanDefinition(
                        beanDefinitionRegistry, beanName, RedisTemplate.class, null, param
                );
                this.checkIsRegister(isRegister, beanName);
            }

            log.info("[插件配置成功] - 成功插件:[Redis]");

        }
    }



    /**
     * 未在注册中心注册成功异常
     * @param source
     */
    private void checkIsRegister(boolean isRegister, String source) {
        if (!isRegister) {
            throw new RedisConfigException(ExceptionCode.ERROR, "Bean[{}]未在注册中心注册成功！", source);
        }
    }
}
