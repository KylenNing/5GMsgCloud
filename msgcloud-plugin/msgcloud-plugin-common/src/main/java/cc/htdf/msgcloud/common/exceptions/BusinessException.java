package cc.htdf.msgcloud.common.exceptions;

/**
 * author: JT
 * date: 2020/2/7
 * title:
 *
 * 业务异常
 */
public class BusinessException extends BasicException {
    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(Integer code, String messageFormat, Object... args) {
        super(code, messageFormat, args);
    }
}
