package cc.htdf.msgcloud.mongo.plugin;

import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.common.plugin.ConfigLoad;
import cc.htdf.msgcloud.mongo.annotation.EnableMongodb;
import cc.htdf.msgcloud.mongo.domain.MongoSource;
import cc.htdf.msgcloud.mongo.exception.MongoConfigException;
import cc.htdf.msgcloud.mongo.plugin.config.MongoDefaultConfigLoad;
import cc.htdf.msgcloud.mongo.utils.BeanRegistryUtil;
import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @author: JT
 * @date: 2019/11/26
 * @Title:
 */
@Slf4j
public class MongoLoadPlugin implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        /**
         * 环境配置获取
         */
        String env = BeanRegistryUtil.readEnvFromBeanRegistry(beanDefinitionRegistry);
        ConfigLoad<MongoSource> configLoad = new MongoDefaultConfigLoad(env);
        Map<String, MongoSource> mongoSourceMap = configLoad.load();

        /**
         * 获取注解配置的
         */
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableMongodb.class.getName());
        String[] sources = (String[]) attributes.get("sources");
        String defaultSource = (String) attributes.get("defaultSource");

        /**
         * 加载 MongoTemplate 配置
         */
        for (String source : sources) {
            MongoSource mongoSource = mongoSourceMap.get(source);
            if (Objects.isNull(mongoSource))
                throw new MongoConfigException(ExceptionCode.ERROR, "未在配置文件中找到该数据源[{}]配置", source);
            MongoDbFactory factory = this.createMongoFactory(mongoSource);
            boolean isRegister = BeanRegistryUtil.registerBeanDefinition(
                        beanDefinitionRegistry, source, MongoTemplate.class, factory, null
                    );
            this.checkIsRegister(isRegister, source);

            if (source.equals(defaultSource)) {
                isRegister = BeanRegistryUtil.registerBeanDefinition(
                        beanDefinitionRegistry, "mongoTemplate", MongoTemplate.class, factory, null
                );
                this.checkIsRegister(isRegister, source);
            }
        }
        log.info("[插件配置成功] - 成功插件:[Mongodb]");
    }

    /**
     * 未在注册中心注册成功异常
     * @param source
     */
    private void checkIsRegister(boolean isRegister, String source) {
        if (!isRegister)
            throw new MongoConfigException(ExceptionCode.ERROR, "Bean[{}]未在注册中心注册成功！", source);
    }

    /**
     * 创建 MongoFactory
     * @param mongoSource
     * @return MongodbFactory
     */
    private MongoDbFactory createMongoFactory(MongoSource mongoSource) {
        return new SimpleMongoDbFactory(new MongoClientURI(mongoSource.getUri()));
    }


}
