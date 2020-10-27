package cc.htdf.msgcloud.common.plugin;

import java.util.Map;

/**
 * @author: JT
 * @date: 2019/11/14
 * @Title:
 */
public interface ConfigLoad<T> {

    /**
     * 数据源配置加载
     *
     * Key： 数据源名称
     * Value： 数据源
     * @return
     */
    Map<String, T> load();
}
