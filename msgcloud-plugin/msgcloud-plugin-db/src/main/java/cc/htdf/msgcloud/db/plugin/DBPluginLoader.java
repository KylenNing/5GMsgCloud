package cc.htdf.msgcloud.db.plugin;

import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.common.plugin.ConfigLoad;
import cc.htdf.msgcloud.db.annotation.EnableDB;
import cc.htdf.msgcloud.db.domain.DB;
import cc.htdf.msgcloud.db.domain.DBExtendConfig;
import cc.htdf.msgcloud.db.exception.DBConfigException;
import cc.htdf.msgcloud.db.plugin.config.DBConfigDefaultLoad;
import cc.htdf.msgcloud.db.plugin.config.DataSources;
import cc.htdf.msgcloud.db.plugin.config.DynamicDataSource;
import cc.htdf.msgcloud.db.plugin.handler.DBContextHolder;
import cc.htdf.msgcloud.db.utils.BeanRegistryUtil;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: JT
 * @date: 2019/11/14
 * @Title:
 *
 * DB插件加载
 *
 */
@Slf4j
public class DBPluginLoader implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        /**
         * 获取激活环境，并根据环境加载db配置文件
         */
        String env = BeanRegistryUtil.readEnvFromBeanRegistry(beanDefinitionRegistry);
        ConfigLoad configLoad = new DBConfigDefaultLoad(env);
        DataSources.dataSourceMap = configLoad.load();

        /**
         * 获取注解配置的值
         */
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableDB.class.getName());
        String[] datasourceNames = (String[]) attributes.get("db");
        String defaultDatasource = (String) attributes.get("defaultDB");


        /**
         * 根据注解配置，配置数据源及数据路由
         */
        Map<String, Object> dynamicDataSourceMap = this.loadDynamicDataSource(datasourceNames, defaultDatasource, DataSources.dataSourceMap);

        /**
         * 注册数据路由
         */
        boolean isRegister = BeanRegistryUtil.registerBeanDefinitionIfNotExist(beanDefinitionRegistry, DynamicDataSource.class, dynamicDataSourceMap);
        if (isRegister) {
            log.info("[插件配置成功] - 成功插件:[DB]");
        } else {
            throw new DBConfigException(ExceptionCode.ERROR, "未成功注册数据路由/数据源!");
        }
    }

    /**
     * 载入数据源及数据路由
     * @param datasourceNames 数据源名称
     * @param datasourceMap  数据源Map
     * @return
     */
    private Map<String, Object> loadDynamicDataSource(String[] datasourceNames, String defaultDatasource, Map<String, DB> datasourceMap) {
        if (datasourceNames.length == 0)
            throw new DBConfigException(ExceptionCode.ERROR, "未开启任何数据源!");

        Map<String, Object> dynamicDataSourceMap = new HashMap<>();
        Map<Object, Object> dataSourcePoolMap = new HashMap<>();
        DBExtendConfig dbExtendConfig = new DBExtendConfig();
        for (int i = 0; i < datasourceNames.length; i++) {
            DB db = datasourceMap.get(datasourceNames[i]);
            if (Objects.isNull(db)) continue;

            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(db.getUrl());
            druidDataSource.setUsername(db.getUsername());
            druidDataSource.setPassword(db.getPassword());
            druidDataSource.setDriverClassName(db.getDriverClassName());
            druidDataSource.setInitialSize(dbExtendConfig.getInitSize());
            druidDataSource.setMinIdle(dbExtendConfig.getMinIdle());
            druidDataSource.setMaxActive(dbExtendConfig.getMaxActive());
            druidDataSource.setTimeBetweenEvictionRunsMillis(dbExtendConfig.getTimeBetweenEvictionRunsMillis());
            druidDataSource.setMinEvictableIdleTimeMillis(dbExtendConfig.getMinEvictableIdleTimeMillis());
            druidDataSource.setValidationQuery(dbExtendConfig.getValidationQuery());
            druidDataSource.setTestWhileIdle(dbExtendConfig.isTestWhileIdle());
            druidDataSource.setTestOnBorrow(dbExtendConfig.isTestOnBorrow());
            druidDataSource.setTestOnReturn(dbExtendConfig.isTestOnReturn());
            druidDataSource.setPoolPreparedStatements(dbExtendConfig.isPoolPreparedStatements());
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(dbExtendConfig.getMaxPoolPreparedStatementPerConnectionSize());
            druidDataSource.setConnectionProperties(dbExtendConfig.getConnectionProperties());
            druidDataSource.setUseGlobalDataSourceStat(dbExtendConfig.isUseGlobalDataSourceStat());
            dataSourcePoolMap.put(db.getName(), druidDataSource);

            // 设置默认数据源
            if (Objects.equals(defaultDatasource, db.getName())) {
                dynamicDataSourceMap.put("defaultTargetDataSource", druidDataSource);
            }
            DBContextHolder.datasourceIds.add(datasourceNames[i]);
        }
        dynamicDataSourceMap.put("targetDataSources", dataSourcePoolMap);
        return dynamicDataSourceMap;
    }
}
