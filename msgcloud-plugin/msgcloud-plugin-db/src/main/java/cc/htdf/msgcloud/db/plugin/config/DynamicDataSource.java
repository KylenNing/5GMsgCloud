package cc.htdf.msgcloud.db.plugin.config;

import cc.htdf.msgcloud.db.plugin.handler.DBContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author jutao
 * @date 2018/9/15
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 取得当前使用哪个数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.getDbType();
    }
}
