package cc.htdf.msgcloud.msgcenter.handler;

import cc.htdf.msgcloud.common.constants.JWTConstant;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.utils.JWTUtil;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgOrganVO;
import cc.htdf.msgcloud.msgcenter.service.CMsgOrganService;
import cc.htdf.msgcloud.msgcenter.service.CMsgUserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/6/10
 * title:
 * 有@LoginUser注解的方法参数，注入当前登录用户
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private CMsgUserService cMsgUserService;

    @Resource
    private CMsgOrganService cMsgOrganService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(CMsgUserPO.class) && methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        String token = request.getHeader(JWTConstant.JWT_TOKEN_FLAG);
        if (Objects.isNull(token) || "".equals(token)) {
            token = request.getParameter(JWTConstant.JWT_TOKEN_FLAG);
        }
        if (Objects.isNull(token)) {
            throw new BusinessException(500, "用户未登录, 请重新登录！");
        }

        String userAccount = JWTUtil.getUsername(token);
        if (Objects.isNull(userAccount)) {
            throw new BusinessException(500, "签名验证失败，无法从Token中获取用户名，请重新登录！");
        }
        CMsgUserPO cMsgUser = cMsgUserService.findByUseraccount(userAccount);
        boolean isVerify = JWTUtil.verify(token, userAccount, cMsgUser.getUserPassword());
        if (!isVerify) {
            throw new BusinessException(500, "签名验证失败，请重新登录！");
        }

//        List<CMsgOrganPO> cMsgOrganPOList = cMsgOrganService.findOrgs();
        List<Integer> allOrgs = cMsgOrganService.findOrgsByOrganId(cMsgUser.getOrganId());
        cMsgUser.setOrgs(allOrgs);
        if (cMsgUser.getOrganId() == 1) {// 最高组织特殊处理
            cMsgUser.setOrganArea(1);
            cMsgUser.setOrganType("1,2");
        } else {
            CMsgOrganVO cMsgOrganVO = cMsgOrganService.findOrganAreaAndType(cMsgUser.getOrganId());
            cMsgUser.setOrganArea(cMsgOrganVO.getOrganArea());
            cMsgUser.setOrganType(String.valueOf(cMsgOrganVO.getOrganType()));
        }
        return cMsgUser;
    }

}
