package cc.htdf.msgcloud.redis.annotation;

import cc.htdf.msgcloud.redis.plugin.RedisLoadPlugin;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * author: JT
 * date: 2020/8/7
 * title:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RedisLoadPlugin.class)
public @interface EnableRedis {

    /**
     * 开启的数据源，这里填写数据源的名称
     * @return
     */
    String[] source();

    /**
     * 默认数据源
     * @return
     */
    String defaultSource();
}
