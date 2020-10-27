package cc.htdf.msgcloud.common.exceptions;

/**
 * author: JT
 * date: 2020/6/7
 * title:
 *
 *  系统异常
 */
public class SystemException extends BasicException {
    public SystemException(Integer code, String message) {
        super(code, message);
    }

    public SystemException(Integer code, String messageFormat, String... args) {
        super(code, messageFormat, args);
    }
}
