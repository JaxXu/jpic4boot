package com.dreamagain.jpic4boot.task;

import com.dreamagain.jpic4boot.util.QiniuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器
 * Created on 2017/9/24.
 *
 * @author yihao
 */
@Component
@Slf4j
public class TaskQuartz {

    @Autowired
    private QiniuUtil qiniuUtil;

    @Scheduled(fixedRate = 30000)
    public void refreshToken() {
        boolean flag = qiniuUtil.refreshToken();
        while (!flag) {
            flag = qiniuUtil.refreshToken();
        }
    }
}
