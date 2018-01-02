package com.dreamagain.jpic4boot.cache;

/**
 * 七牛token缓存类
 *
 * @author yihao
 */
public interface AbstractQiniuCache {

    /**
     * 得到token
     *
     * @return token
     */
    String getToken();

    /**
     * 设置token
     *
     * @param token 新token
     */
    void setToken(String token);
}