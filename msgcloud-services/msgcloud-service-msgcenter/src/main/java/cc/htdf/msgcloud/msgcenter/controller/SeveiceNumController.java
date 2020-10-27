package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.dto.SeveiceNumDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.service.SeveiceNumService;
import io.minio.errors.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * @Author: renxh
 * @Date: 2020/8/12
 * @Description:
 */
@Validated
@RestController
@RequestMapping("/seveice")
public class SeveiceNumController {

    @Resource
    SeveiceNumService seveiceNumService;

    /**
     * 5G气象服务云平台获取SeveiceNum表数据
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getSeveiceNumListByPage")
    public WebResponse getSeveiceNumListByPage(@LoginUser CMsgUserPO user,
                                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取数据！")
                .data(seveiceNumService.getSeveiceNumListByPage(user, pageNum, pageSize));
    }

    /**
     * 5G气象服务云平台获取SeveiceNum表数据
     *
     * @param user
     * @return
     */
    @GetMapping(value = "/getSeveiceNumList")
    public WebResponse getMaterialList(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取数据！")
                .data(seveiceNumService.getSeveiceNumList(user));
    }


    /**
     * @param dto
     * @Description: 添加SeveiceNum数据记录
     * @author renxh
     * @date 2020/8/12
     */
    @PostMapping(value = "/addSeveiceNum")
    @ResponseBody
    public WebResponse addSeveiceNum(@LoginUser CMsgUserPO user, @RequestBody SeveiceNumDTO dto) {

        return WebResponseBuilder.ok().setMsg("添加成功！").data(seveiceNumService.addSeveiceNum(user, dto));
    }


    /**
     * @param id
     * @Description: 删除SeveiceNum数据记录
     * @author renxh
     * @date 2020/8/12
     */
    @GetMapping(value = "/deleteSeveiceNum")
    public WebResponse deleteSeveiceNum(@RequestParam("id") String id) {
        seveiceNumService.deleteById(id);
        return WebResponseBuilder.ok().setMsg("删除成功！");

    }

    /**
     * @param dto
     * @Description: 更新SeveiceNum数据记录
     * @author renxh
     * @date 2020/8/12
     */
    @PostMapping(value = "/updateSeveiceNum")
    public WebResponse updateSeveiceNum(@LoginUser CMsgUserPO user, @RequestBody SeveiceNumDTO dto) {
        seveiceNumService.updateseveiceNumById(user, dto);
        return WebResponseBuilder.ok().setMsg("更新成功！");
    }

    /**
     * 上传LOGO
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadMaterial", method = RequestMethod.POST)
    public WebResponse uploadMaterial(@RequestParam("file") MultipartFile file) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {
        return WebResponseBuilder.ok().setMsg("成功创建素材！")
                .data(seveiceNumService.uploadMaterial(file));
    }


}
