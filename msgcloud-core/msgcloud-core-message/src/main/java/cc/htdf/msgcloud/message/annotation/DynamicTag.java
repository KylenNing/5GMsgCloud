package cc.htdf.msgcloud.message.annotation;

import java.lang.annotation.*;

/**
 * author: JT
 * date: 2020/8/10
 * title:
 *
 *      动态标签注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicTag {

    /**
     * 标签类型
     * @return
     */
    String type();

    /**
     * 标签名称
     * @return
     */
    String name();

    /**
     * 标签值
     * @return
     */
    String value();

}
