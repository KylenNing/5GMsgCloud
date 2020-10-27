package cc.htdf.msgcloud.db.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: JT
 * @date: 2019/11/14
 * @Title:
 */
@Setter
@Getter
public class DB {

    private String name;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
