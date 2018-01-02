package com.dreamagain.jpic4boot.util;

import com.dreamagain.jpic4boot.cache.AbstractQiniuCache;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 七牛工具类
 * Created on 2017/8/18.
 *
 * @author yihao
 */
@Slf4j
@Component
public class QiniuUtil {

    @Autowired
    private QiniuConfig config;

    @Autowired
    private AbstractQiniuCache cache;

    /**
     * 构造一个带指定Zone对象的配置类
     */
    private Configuration cfg = new Configuration(Zone.autoZone());

    /**
     * 上传类
     */
    private UploadManager uploadManager = new UploadManager(cfg);

    public boolean refreshToken() {
        try {
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            StringMap putPolicy = new StringMap();
            putPolicy.put("returnBody", "$(key)");
            putPolicy.put("saveKey", config.getPreFix() + "$(year)-$(mon)-$(day)_$(hour):$(min):$(sec)_$(fsize)$(ext)");
            String upToken = auth.uploadToken(config.getBucket(), null, config.getExpireSeconds(), putPolicy);
            cache.setToken(upToken);
            return true;
        } catch (Exception e) {
            log.info("获取七牛token失败", e);
            return false;
        }
    }

    @SneakyThrows
    public String upload(byte[] uploadBytes) {
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Response response = uploadManager.put(uploadBytes, null, cache.getToken());
        //解析上传成功的结果
        return config.getRomUrl() + response.bodyString().substring(1, response.bodyString().length() - 1);
    }
}
