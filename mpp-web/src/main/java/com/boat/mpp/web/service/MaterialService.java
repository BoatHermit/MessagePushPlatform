package com.boat.mpp.web.service;


import com.boat.mpp.common.vo.BasicResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 素材接口
 *
 * @author boat
 */
public interface MaterialService {


    /**
     * 钉钉素材上传
     *
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType);


    /**
     * 企业微信（机器人）素材上传
     *
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    BasicResultVO enterpriseWeChatRootMaterialUpload(MultipartFile file, String sendAccount, String fileType);

    /**
     * 企业微信（应用消息）素材上传
     *
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    BasicResultVO enterpriseWeChatMaterialUpload(MultipartFile file, String sendAccount, String fileType);
}
