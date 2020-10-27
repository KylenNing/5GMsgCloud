package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.common.utils.JWTUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgMenuTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgUserDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgMenuPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.LoginVO;
import cc.htdf.msgcloud.msgcenter.service.CMsgMenuService;
import cc.htdf.msgcloud.msgcenter.service.CMsgUserService;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/8/18
 * title:
 *      登录模块
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private CMsgUserService userService;

    @Resource
    private CMsgMenuService cMsgMenuService;

    /**
     * 登录接口
     * @param loginVO
     * @return
     */
    @PostMapping
    public WebResponse login(@RequestBody LoginVO loginVO){
        String passwordHex = DigestUtils.md5Hex(loginVO.getPassword());
        CMsgUserPO user = userService.findByUseracountAndPass(loginVO.getUserAcount(), passwordHex);
        if (Objects.isNull(user)) {
            return WebResponseBuilder
                    .fail("登录失败，用户或密码输入错误！");
        }
        List<CMsgMenuTreeDTO> menuTreeList = cMsgMenuService.findMenuTreeAllByUserId(user);
        CMsgUserDTO userDTO = new CMsgUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setUserPassword("******");
        return WebResponseBuilder.ok().data(
                ImmutableMap.of(
                        "user", userDTO,
                        "token", JWTUtil.sign(user.getUserAccount(), passwordHex),
                        "menuTree", menuTreeList
                )
        );
    }

}
