package cc.htdf.msgcloud.db.exception;

import cc.htdf.msgcloud.common.exceptions.BasicException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: JT
 * @date: 2019/11/14
 * @Title:
 *
 *  数据库配置异常类
 */
@Slf4j
public class DBConfigException extends BasicException {

    public DBConfigException(Integer code, String message) {
        super(code, message);
    }

    public DBConfigException(Integer code, String messageFormat, String... args) {
        super(code, messageFormat, args);
        log.error("[插件配置异常] - 异常插件:[DB] - 异常代码: [{}] - 异常信息[{}]", code, super.getMessage());
    }
}
