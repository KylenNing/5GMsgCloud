package cc.htdf.msgcloud.mongo.exception;

import cc.htdf.msgcloud.common.exceptions.BasicException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: JT
 * @date: 2019/11/26
 * @Title:
 *
 * Mongodb 配置异常类
 */
@Slf4j
public class MongoConfigException extends BasicException {
    public MongoConfigException(Integer code, String message) {
        super(code, message);
    }

    public MongoConfigException(Integer code, String messageFormat, String... args) {
        super(code, messageFormat, args);
        log.error("[插件配置异常] - 异常插件:[Mongodb] - 异常代码: [{}] - 异常信息[{}]", code, super.getMessage());
    }
}
