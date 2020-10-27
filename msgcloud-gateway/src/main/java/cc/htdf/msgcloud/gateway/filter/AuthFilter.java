package cc.htdf.msgcloud.gateway.filter;

import cc.htdf.msgcloud.common.config.LogConfig;
import cc.htdf.msgcloud.common.constants.JWTConstant;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.common.utils.JWTUtil;
import cc.htdf.msgcloud.gateway.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.gateway.service.CIgnoreUriService;
import cc.htdf.msgcloud.gateway.service.CMsgUserService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * author: JT
 * date: 2020/8/19
 * title:
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Resource
    private CMsgUserService cMsgUserService;

    @Resource
    private CIgnoreUriService cIgnoreUriService;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        MultiValueMap<String, String> headers = serverHttpRequest.getHeaders();
        MultiValueMap<String, String> queryParam = serverHttpRequest.getQueryParams();
        MultiValueMap<String, HttpCookie> cookies = serverHttpRequest.getCookies();

        String uri = serverHttpRequest.getURI().getPath();
        boolean isIgnore = false;
        List<String> ignoreUrls = cIgnoreUriService.ignoreUris();
        for (String ignoreUrl : ignoreUrls) {
            if (antPathMatcher.match(ignoreUrl, uri)) {
                isIgnore = true;
                break;
            }
        }
        if (isIgnore) {
            log.info(
                    MarkerFactory.getMarker(LogConfig.LOG_AUTH),
                    "URI:[{}]不需要鉴权！", uri
            );
            return chain.filter(exchange);
        }

        /**
         * 获取Token
         */
        List<String> tokens = headers.get(JWTConstant.JWT_TOKEN_FLAG);
        if (Objects.isNull(tokens) || tokens.isEmpty()) {
            tokens = queryParam.get(JWTConstant.JWT_TOKEN_FLAG);
        }
        if (Objects.isNull(tokens) || tokens.isEmpty()) {
            List<HttpCookie> cookieList = cookies.get(JWTConstant.JWT_TOKEN_FLAG);
            if (!Objects.isNull(cookieList) && !cookieList.isEmpty()) {
                tokens = cookieList.stream()
                        .filter(cookie -> JWTConstant.JWT_TOKEN_FLAG.equals(cookie.getName()))
                        .map(cookie -> cookie.getValue()).collect(Collectors.toList());
            }
        }
        if (Objects.isNull(tokens) || tokens.isEmpty()) {
            log.error(MarkerFactory.getMarker(LogConfig.LOG_AUTH), "未找到Token");
            return returnUnauthorizedResponse(exchange);
        }

        String token = tokens.get(0);
        String userAccount = JWTUtil.getUsername(token);
        if (Objects.isNull(userAccount) || "".equals(userAccount)) {
            log.error(
                    MarkerFactory.getMarker(LogConfig.LOG_AUTH),
                    "无法从Token中获取用户账号! [Token: {}]",
                    token
            );
            return returnUnauthorizedResponse(exchange);
        }

        CMsgUserPO user = cMsgUserService.findByUseraccoount(userAccount);
        boolean isVerify = JWTUtil.verify(token, user.getUserAccount(), user.getUserPassword());
        if (!isVerify) {
            log.error(
                    MarkerFactory.getMarker(LogConfig.LOG_AUTH),
                    "验证用户[{}]的JWTToken[{}]失败!",
                    userAccount, token
            );
            return returnUnauthorizedResponse(exchange);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 15;
    }

    public Mono<Void> returnUnauthorizedResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
