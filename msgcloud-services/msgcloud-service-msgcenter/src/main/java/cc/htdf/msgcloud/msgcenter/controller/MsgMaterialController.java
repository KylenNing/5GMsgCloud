package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgMaterialLabelVO;
import cc.htdf.msgcloud.msgcenter.service.MsgMaterialService;
import io.minio.errors.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Validated
@RestController
@RequestMapping("/material")
public class MsgMaterialController {

    @Resource
    private MsgMaterialService msgMaterialService;

    /**
     * 5G气象服务云平台获取素材列表
     *
     * @param materialType
     * @param materialName
     * @return
     */
    @GetMapping(value = "/getMaterialList")
    public WebResponse getMaterialList(@LoginUser CMsgUserPO user,
                                       @RequestParam(value = "materialType") String materialType,
                                       @RequestParam(value = "materialName", required = false) String materialName) {
        return WebResponseBuilder.ok().setMsg("成功获取素材列表！")
                .data(msgMaterialService.getMaterialList(user, materialType, materialName));
    }

    /**
     * 5G气象服务云平台获取素材分页
     *
     * @param labelId
     * @param materialType
     * @param materialName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getMaterialPage")
    public WebResponse getMaterialPage(@LoginUser CMsgUserPO user,
                                       @RequestParam(value = "labelId") String labelId,
                                       @RequestParam(value = "materialType") String materialType,
                                       @RequestParam(value = "materialName", required = false) String materialName,
                                       @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获5G气象服务云平台获取素材分页！")
                .data(msgMaterialService.getMaterialPage(user, labelId, materialType, materialName, pageNum, pageSize));
    }

    /**
     * 5G气象服务云平台上传素材
     *
     * @param file
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/uploadMaterial", method = RequestMethod.POST)
    public WebResponse uploadMaterial(@LoginUser CMsgUserPO user,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("labelId") String labelId) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {
        return WebResponseBuilder.ok().setMsg("成功创建素材！")
                .data(msgMaterialService.uploadMaterial(user, file, labelId));
    }

    /**
     * 5G气象服务云平台新建素材分类标签
     *
     * @param msgMaterialLabelVO
     * @return
     */
    @PostMapping(value = "/createMaterialLabel")
    public WebResponse createMaterialLabel(@LoginUser CMsgUserPO user, @RequestBody MsgMaterialLabelVO msgMaterialLabelVO) {
        return WebResponseBuilder.ok().setMsg("成功新建素材分类标签！")
                .data(msgMaterialService.createMaterialLabel(user, msgMaterialLabelVO));
    }

    /**
     * 5G气象服务云平台获取分类标签列表
     *
     * @param materialType
     * @return
     */
    @GetMapping(value = "/getMaterialLabel")
    public WebResponse getMaterialLabel(@LoginUser CMsgUserPO user, @RequestParam(value = "materialType") String materialType) {
        return WebResponseBuilder.ok().setMsg("成功获取素材分类标签列表！")
                .data(msgMaterialService.getMaterialLabel(user, materialType));
    }

    /**
     * 5G气象服务云平台移动素材
     *
     * @param materialIds
     * @param materialLabelId
     * @return
     */
    @GetMapping(value = "/moveMaterial")
    public WebResponse moveMaterial(@LoginUser CMsgUserPO user,
                                    @RequestParam(value = "materialIds") String materialIds,
                                    @RequestParam(value = "materialLabelId") String materialLabelId) {
        return WebResponseBuilder.ok().setMsg("成功移动素材！")
                .data(msgMaterialService.moveMaterial(user, materialIds, materialLabelId));
    }

    /**
     * 5G气象服务云平台删除素材
     *
     * @param materialIds
     * @return
     */
    @GetMapping(value = "/deleteMaterialByIds")
    public WebResponse deleteMaterialByIds(@RequestParam(value = "materialIds") String materialIds) {
        return WebResponseBuilder.ok().setMsg("成功删除素材！")
                .data(msgMaterialService.deleteMaterialByIds(materialIds));
    }

    /**
     * 5G气象服务云平台删除分类标签
     *
     * @param materialLabelId
     * @return
     */
    @GetMapping(value = "/deleteMaterialLabelById")
    public WebResponse deleteMaterialLabelById(@RequestParam(value = "materialLabelId") String materialLabelId) {
        return WebResponseBuilder.ok().setMsg("成功删除素材分类标签！")
                .data(msgMaterialService.deleteMaterialLabelById(materialLabelId));
    }
}
