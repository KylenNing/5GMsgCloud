package cc.htdf.msgcloud.redis.exception;

import cc.htdf.msgcloud.common.exceptions.BasicException;

/**
 * author: JT
 * date: 2020/8/7
 * title:
 */
public class RedisConfigException extends BasicException {
    public RedisConfigException(Integer code, String message) {
        super(code, message);
    }

    public RedisConfigException(Integer code, String messageFormat, Object... args) {
        super(code, messageFormat, args);
    }
}
