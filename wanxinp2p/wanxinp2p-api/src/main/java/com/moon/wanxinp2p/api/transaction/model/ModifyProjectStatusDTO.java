package com.moon.wanxinp2p.api.transaction.model;

import com.alibaba.fastjson.JSON;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import com.moon.wanxinp2p.common.util.RSAUtil;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 修改标的状态DTO
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-18 16:19
 * @description
 */
@Data
public class ModifyProjectStatusDTO {

    /**
     * 请求流水号
     */
    private String requestNo;
    /**
     * 标的号
     */
    private String projectNo;
    /**
     * 更新标的状态
     */
    private String projectStatus;

    /**
     * 业务实体id
     */
    private Long id;

    /* 测试 */
    public static void main(String[] args) throws UnsupportedEncodingException {
        // 调用存管代理服务--修改状态为还款中
        // 构造请求参数
        ModifyProjectStatusDTO modifyProjectStatusDTO = new ModifyProjectStatusDTO();
        // 业务实体id
        modifyProjectStatusDTO.setId(1130782711460204546L);
        // 业务状态
        modifyProjectStatusDTO.setProjectStatus("REPAYING");
        // 请求流水号
        modifyProjectStatusDTO.setRequestNo("PRO_F6EBE6902F1D4C839EB6A9E351D27230");

        String reqData = EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(modifyProjectStatusDTO));
        final String sign = RSAUtil.sign(JSON.toJSONString(modifyProjectStatusDTO), "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEApkqNoES+508OiULK5UIEuZ9WxIUG7fB92V0vEi1FyNJgzMc2gi5hy8eGcyYyLWJdEt5h1vC8jclCgEcMY3lp3QIDAQABAkAUhQia6UDBXEEH8QUGazIYEbBsSZoETHPLGbOQQ6Pj1tb6CVC57kioBjwtNBnY2jBDWi5K815LnOBcJSSjJPwhAiEA2eO6VZMTkdjQAkpB5dhy/0C3i8zs0c0M1rPoTA/RpkUCIQDDYHJPqHLkQyd//7sEeYcm8cMBTvDKBXyiuGk8eLRauQIgQo6IlalGmg+Dgp+SP5Z9kjD/oCmp0XB0UoVEGS/f140CIQCsG9YXHgi31ACD3T9eHcBVKjvidyveix7UKSdrQdl+4QIgNCtRVLV+783e7PX5hRXD+knsWTQxDEMEsHi1KsAWtPk=", "utf-8");

        String str = "serviceName=" + URLEncoder.encode("MODIFY_PROJECT", "utf-8")
                + "&platformNo=" + URLEncoder.encode("wanxinp2p", "utf-8")
                + "&reqData=" + URLEncoder.encode(reqData, "utf-8")
                + "&signature=" + URLEncoder.encode(sign, "utf-8");
        System.out.println(str);
    }

}
