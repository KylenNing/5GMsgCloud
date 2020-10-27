package cc.htdf.msgcloud.db.config;

import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.db.annotation.TargetDataSource;
import cc.htdf.msgcloud.db.exception.DBConfigException;
import cc.htdf.msgcloud.db.plugin.handler.DBContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * author: JT
 * date: 2020/8/6
 * title:
 */
@Component
@Aspect
@Order(-100)
@Slf4j
public class TargetDatasourceHandler {

    @Before("@annotation(dataSource)")
    public void datasourceRouter(JoinPoint point, TargetDataSource dataSource) {
        String dataSourceName = dataSource.value();
        if (!DBContextHolder.containsDatasource(dataSourceName)) {
            throw new DBConfigException(ExceptionCode.ERROR, "数据源[{0}]不存在！", dataSourceName);
        }
        DBContextHolder.setDbType(dataSourceName);
        log.debug("切换数据源[{}]成功!", dataSourceName);
    }

    @After("@annotation(dataSource)")
    public void clearDatasourceRouter(JoinPoint point, TargetDataSource dataSource) {
        DBContextHolder.clearDbType();
        log.debug("数据源使用完毕，清理数据源信息!");
    }
}
