package com.dreamagain.jpic4boot.util;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Wini;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 七牛配置类
 *
 * @author jax
 */
@Component
@Slf4j
public class QiniuConfig {

    @Getter
    @Setter
    private Integer expireSeconds = 36000;

    @Getter
    private String bucket = "hszy";

    @Getter
    private String accessKey = "FHfkeideCldIc5GXGDDEkEN0J0ggBDjWuBdl0RR5";

    @Getter
    private String secretKey = "_K_UG0SjzPTXEggxt8_89VmK3xmjtzViRoChZk8Z";

    @Getter
    private String romUrl = "http://ozulhlchy.bkt.clouddn.com/";

    @Getter
    private String preFix = "upload/";

    /**
     * 七牛在ini中的SECTION_NAME
     */
    private final static String SECTION_NAME = "qiniu";

    /**
     * 内部维护一个ini
     */
    private Wini ini;

    @SneakyThrows
    public QiniuConfig() {
        String configIni = System.getProperty("user.dir") + "\\conf.ini";
        File file = new File(configIni);
        //是否存在配置文件
        if (file.exists()) {
            //存在
            ini = new Wini(file);
            this.bucket = ini.get(SECTION_NAME, "bucket");
            this.accessKey = ini.get(SECTION_NAME, "accessKey");
            this.secretKey = ini.get(SECTION_NAME, "secretKey");
            this.romUrl = ini.get(SECTION_NAME, "romUrl");
            this.preFix = ini.get(SECTION_NAME, "preFix");
        } else {
            //不存在,生成默认的
            this.bucket = "";
            this.accessKey = "";
            this.secretKey = "";
            this.romUrl = "";
            this.preFix = "";
            boolean createFlag = file.createNewFile();
            if (!createFlag) {
                log.error("初始化配置文件失败");
                throw new RuntimeException("初始化配置文件失败");
            }
            ini = new Wini(file);
            ini.putComment(SECTION_NAME, "七牛云配置");
            ini.add(SECTION_NAME, "bucket", bucket);
            ini.add(SECTION_NAME, "accessKey", accessKey);
            ini.add(SECTION_NAME, "secretKey", secretKey);
            ini.add(SECTION_NAME, "romUrl", romUrl);
            ini.add(SECTION_NAME, "preFix", preFix);
            ini.store();
        }
    }

    @SneakyThrows
    public void setBucket(String bucket) {
        this.bucket = bucket;
        ini.put(SECTION_NAME, "bucket", bucket);
        ini.store();
    }

    @SneakyThrows
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        ini.put(SECTION_NAME, "accessKey", accessKey);
        ini.store();
    }

    @SneakyThrows
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        ini.put(SECTION_NAME, "secretKey", secretKey);
        ini.store();
    }

    @SneakyThrows
    public void setRomUrl(String romUrl) {
        this.romUrl = romUrl;
        ini.put(SECTION_NAME, "romUrl", romUrl);
        ini.store();
    }

    @SneakyThrows
    public void setPreFix(String preFix) {
        this.preFix = preFix;
        ini.put(SECTION_NAME, "preFix", preFix);
        ini.store();
    }
}
