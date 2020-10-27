package cc.htdf.msgcloud.db.annotation;

import cc.htdf.msgcloud.db.plugin.DBPluginLoader;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: JT
 * @date: 2019/11/15
 * @Title:
 *
 * 开启DB插件
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DBPluginLoader.class)
public @interface EnableDB {

    /**
     * 开启的数据源，这里填写数据源的名称
     * @return
     */
    String[] db();

    /**
     * 默认数据源
     * @return
     */
    String defaultDB();

}
