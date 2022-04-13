package com.moon.farming.handler;


import com.moon.farming.common.domain.RestResponse;
import com.moon.farming.entity.FileObject;
import com.moon.farming.entity.UploadInfo;
import com.moon.farming.entity.UploadStrategy;

import java.util.Map;

/**
 * 文件存储接口
 */
public interface StorageHandler {


    /**
     * 生成凭证
     *
     * @param uploadStrategy 上传策略
     * @param params         各存储源差异化参数
     * @return
     */
    String generateToken(UploadStrategy uploadStrategy, Map<String, Object> params);


    /**
     * 执行上传
     *
     * @param uploadInfo
     * @return
     */
    FileObject upload(UploadInfo uploadInfo);

    /**
     * 批量生成下载url
     *
     * @param fileKeys 文件标识列表
     * @return Map key为fileKey，value为文件下载url
     */
    Map<String, String> generateDownloadUrls(String[] fileKeys) throws Exception;

    /**
     * 批量删除文件
     *
     * @param fileKeys 文件标识列表
     * @return
     */
    RestResponse<String> deleteFiles(String[] fileKeys);
}
