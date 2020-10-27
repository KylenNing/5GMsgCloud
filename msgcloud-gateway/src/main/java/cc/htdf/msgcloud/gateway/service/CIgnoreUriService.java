package cc.htdf.msgcloud.gateway.service;

import cc.htdf.msgcloud.gateway.domain.po.CIgnoreUri;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 */
public interface CIgnoreUriService extends IService<CIgnoreUri> {

    List<String> ignoreUris();

}
