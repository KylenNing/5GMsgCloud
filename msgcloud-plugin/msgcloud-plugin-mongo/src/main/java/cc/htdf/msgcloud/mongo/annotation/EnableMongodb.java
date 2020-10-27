package cc.htdf.msgcloud.mongo.annotation;


import cc.htdf.msgcloud.mongo.plugin.MongoLoadPlugin;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: JT
 * @date: 2019/11/25
 * @Title:
 *
 * 开启Mongodb插件
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MongoLoadPlugin.class)
public @interface EnableMongodb {

    String[] sources();

    String defaultSource();
}
