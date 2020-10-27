package cc.htdf.msgcloud.db.plugin.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jutao
 * @date 2018/9/15
 */
public class DBContextHolder {
    private static final ThreadLocal contextHolder = new ThreadLocal<>();

    public static List<String> datasourceIds = new ArrayList<>();

    /**
     * 设置数据源
     * @param dbType
     */
    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDbType() {
        return (String) contextHolder.get();
    }

    public static boolean containsDatasource(String dataSourceName) {
        return datasourceIds.contains(dataSourceName);
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
}
