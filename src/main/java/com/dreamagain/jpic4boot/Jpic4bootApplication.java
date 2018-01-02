package com.dreamagain.jpic4boot;

import com.dreamagain.jpic4boot.core.SystemClipboardMonitor;
import com.dreamagain.jpic4boot.util.QiniuUtil;
import com.dreamagain.jpic4boot.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Boot类
 *
 * @author jax
 */
@SpringBootApplication
@EnableScheduling
public class Jpic4bootApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(Jpic4bootApplication.class, MainView.class, args);
        new SystemClipboardMonitor(new QiniuUtil());
    }
}
