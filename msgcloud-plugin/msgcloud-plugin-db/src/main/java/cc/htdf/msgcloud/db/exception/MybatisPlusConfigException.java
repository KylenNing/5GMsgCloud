package cc.htdf.msgcloud.db.exception;

import cc.htdf.msgcloud.common.exceptions.BasicException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: JT
 * @date: 2019/11/24
 * @Title:
 *
 * MybatisPlus配置异常类
 */
@Slf4j
public class MybatisPlusConfigException extends BasicException {

    public MybatisPlusConfigException(Integer code, String message) {
        super(code, message);
    }

    public MybatisPlusConfigException(Integer code, String messageFormat, String... args) {
        super(code, messageFormat, args);
        log.error("[插件配置异常] - 异常插件:[DB_MybatisPlus] - 异常代码: [{}] - 异常信息[{}]", code, super.getMessage());
    }
}
