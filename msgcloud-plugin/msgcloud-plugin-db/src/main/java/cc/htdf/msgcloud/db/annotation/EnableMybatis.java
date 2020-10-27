package cc.htdf.msgcloud.db.annotation;

import cc.htdf.msgcloud.db.plugin.MybatisPlusLoader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author: JT
 * @date: 2019/11/19
 * @Title:
 *
 * 开启MybatisPlus插件
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@MapperScan
@Import(MybatisPlusLoader.class)
public @interface EnableMybatis {

    @AliasFor(annotation = MapperScan.class, attribute = "value")
    String[] basePackages() default {};

    /**
     * mapper XML文件所在地址的正则表达式
     * @return
     */
    String mapperXmlPathPattern() default "";

}
