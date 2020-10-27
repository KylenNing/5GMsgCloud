package cc.htdf.msgcloud.gateway.service;

import cc.htdf.msgcloud.gateway.domain.po.CIgnoreUri;
import cc.htdf.msgcloud.gateway.mapper.CIgnoreUriMapper;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 */
@Service
public class CIgnoreUriServiceImpl extends ServiceImpl<CIgnoreUriMapper, CIgnoreUri> implements CIgnoreUriService {

    @Override
    public List<String> ignoreUris() {
        List<CIgnoreUri> list = this.selectList(new Condition());
        return list.stream()
                .map(ignoreUri -> ignoreUri.getIgnoreUri())
                .collect(Collectors.toList());
    }
}
