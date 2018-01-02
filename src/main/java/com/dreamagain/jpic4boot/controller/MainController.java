package com.dreamagain.jpic4boot.controller;

import com.dreamagain.jpic4boot.core.StatusContro;
import com.dreamagain.jpic4boot.core.SystemClipboardMonitor;
import com.dreamagain.jpic4boot.util.QiniuConfig;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 主view控制器
 *
 * @author jax
 */
@FXMLController
@Slf4j
public class MainController {

    @FXML
    private TextField ak;

    @FXML
    private TextField sk;

    @FXML
    private TextField spaceName;

    @FXML
    private TextField qiNiuUrl;

    @FXML
    private TextField preFix;

    @FXML
    private Button run;

    @Autowired
    private QiniuConfig qiniuConfig;

    @Autowired
    private SystemClipboardMonitor systemClipboardMonitor;

    public void start() {
        String start = "Start";
        String stop = "Stop";
        if (start.equals(run.getText())) {
            run.setText(stop);
            StatusContro.setStart(true);
        } else {
            run.setText(start);
            StatusContro.setStart(false);
        }
    }

    public void saveConfig() {
        qiniuConfig.setAccessKey(ak.getText());
        qiniuConfig.setSecretKey(sk.getText());
        qiniuConfig.setBucket(spaceName.getText());
        qiniuConfig.setRomUrl(qiNiuUrl.getText());
        qiniuConfig.setPreFix(preFix.getText());
    }

    public void reSet() {
        ak.setText(qiniuConfig.getAccessKey());
        sk.setText(qiniuConfig.getSecretKey());
        spaceName.setText(qiniuConfig.getBucket());
        qiNiuUrl.setText(qiniuConfig.getRomUrl());
        preFix.setText(qiniuConfig.getPreFix());
    }
}