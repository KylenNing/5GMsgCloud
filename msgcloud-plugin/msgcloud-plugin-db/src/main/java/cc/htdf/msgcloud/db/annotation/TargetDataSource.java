package cc.htdf.msgcloud.db.annotation;

import java.lang.annotation.*;

/**
 * @author jutao
 * @date 2018/9/16
 *
 *  自定义注解，用于指定数据源
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    /**
     * 使用的数据源，指定数据源名称
     * @return
     */
    String value();
}
