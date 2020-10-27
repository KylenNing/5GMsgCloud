package cc.htdf.msgcloud.db.plugin;

import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.db.annotation.EnableMybatis;
import cc.htdf.msgcloud.db.exception.MybatisPlusConfigException;
import cc.htdf.msgcloud.db.utils.BeanRegistryUtil;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: JT
 * @date: 2019/11/19
 * @Title:
 *
 *  MybatisPlus 插件加载
 *
 */
@Slf4j
public class MybatisPlusLoader implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        /**
         * 解析注解
         */
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableMybatis.class.getName());
        Object xmlPathPattern = attributes.get("mapperXmlPathPattern");

        /**
         * 获取Spring IOC中注册的动态路由实例
         *      BeanDefinitionRegistry 此接口中无法获取实例
         *      DefaultListableBeanFactory 是 BeanDefinitionRegistry 的一个实现，可获取动态Bean实例
         *
         *      DefaultListableBeanFactory 实现了 BeanDefinitionRegistry 接口，可进行强制类型转换
         */
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) beanDefinitionRegistry;
        DataSource dataSource = beanFactory.getBean(DataSource.class);


        /**
         * 加载Mybatis Global配置
         */
        Map<String, Object> mybatisGlobalConfig = this.loadMybatisGlobalConfig();
        BeanRegistryUtil.registerBeanDefinitionIfNotExist(
                beanDefinitionRegistry, GlobalConfiguration.class, new LogicSqlInjector(), mybatisGlobalConfig

        );
        log.info("MybatisPlus 全局配置加载成功！");

        /**
         * 加载MybatisPlus SqlSessionFactory配置
         */
        try {
            Map<String, Object> sqlSessionFactoryConfig = this.loadSqlSessionFactoryConfig(dataSource, xmlPathPattern);
            BeanRegistryUtil.registerBeanDefinitionIfNotExist(
                    beanDefinitionRegistry, "sqlSessionFactory", MybatisSqlSessionFactoryBean.class,
                    null, sqlSessionFactoryConfig
            );
        } catch (IOException e) {
            throw new MybatisPlusConfigException(ExceptionCode.ERROR, "MybatisPlus 数据源及其它配置失败！");
        }
        log.info("[插件配置成功] - 成功插件:[MybatisPlus]");

    }

    private Map<String, Object> loadMybatisGlobalConfig() {
        Map<String, Object> globalConfig = new HashMap<>();
        globalConfig.put("logicDeleteValue", "-1");
        globalConfig.put("logicNotDeleteValue", "1");
        globalConfig.put("idType", 0);
        globalConfig.put("dbColumnUnderline", false);
        globalConfig.put("refresh", true);
        return globalConfig;
    }

    private Map<String, Object> loadSqlSessionFactoryConfig(DataSource dataSource, Object xmlPathPattern) throws IOException {
        // 加载Mybatis-plus 配置
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);

        Map<String, Object> configMap = new HashMap<>();
        configMap.put("dataSource", dataSource);
        configMap.put("configuration", configuration);
        configMap.put("plugins", new Interceptor[] {
                new PaginationInterceptor()                   // 添加分页功能
        });

        // xml目录配置
        if (!Objects.isNull(xmlPathPattern) && !Objects.equals("", xmlPathPattern)) {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            configMap.put("mapperLocations", resolver.getResources(xmlPathPattern.toString()));
        }
        return configMap;
    }


}
