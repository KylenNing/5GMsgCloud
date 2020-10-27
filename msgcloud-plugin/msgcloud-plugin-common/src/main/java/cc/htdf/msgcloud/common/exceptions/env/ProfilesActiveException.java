package cc.htdf.msgcloud.common.exceptions.env;


import cc.htdf.msgcloud.common.exceptions.BasicException;

/**
 * @author: JT
 * @date: 2019/11/25
 * @Title:
 */
public class ProfilesActiveException extends BasicException {

    public ProfilesActiveException(Integer code, String message) {
        super(code, message);
    }

    public ProfilesActiveException(Integer code, String messageFormat, String... args) {
        super(code, messageFormat, args);
    }
}
