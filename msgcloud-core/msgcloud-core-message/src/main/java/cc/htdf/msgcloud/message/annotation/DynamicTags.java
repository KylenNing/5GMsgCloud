package cc.htdf.msgcloud.message.annotation;

import java.lang.annotation.*;

/**
 * author: JT
 * date: 2020/8/16
 * title:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicTags {

    DynamicTag[] tags();

}
