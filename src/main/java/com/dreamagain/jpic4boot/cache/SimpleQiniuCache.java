package com.dreamagain.jpic4boot.cache;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 七牛token缓存类
 *
 * @author yihao
 */
@Component
@Data
public class SimpleQiniuCache implements AbstractQiniuCache {

    private String token;
}