package com.dreamagain.jpic4boot.core;

import com.alibaba.fastjson.JSONObject;
import com.dreamagain.jpic4boot.util.QiniuUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 系统剪贴板控制器
 *
 * @author jax
 */
@Slf4j
@Component
public class SystemClipboardMonitor implements ClipboardOwner {

    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private final QiniuUtil qiniuUtil;

    @Autowired
    public SystemClipboardMonitor(QiniuUtil qiniuUtil) {
        this.qiniuUtil = qiniuUtil;
        //如果剪贴板中有文本，则将它的ClipboardOwner设为自己
        clipboard.setContents(clipboard.getContents(null), this);
    }

    /**
     * 如果剪贴板的内容改变，则系统自动调用此方法
     */
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        if (StatusContro.isStart()) {
            // 如果不暂停一下，经常会抛出IllegalStateException
            // 猜测是操作系统正在使用系统剪切板，故暂时无法访问
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] imageByteFromClipboard = getImageByteFromClipboard(this.clipboard);
            if (imageByteFromClipboard != null) {
                String upload = qiniuUtil.upload(imageByteFromClipboard);
                Transferable tText = new StringSelection("![mark](" + upload + ")");
                this.clipboard.setContents(tText, this);
            } else {
                this.clipboard.setContents(this.clipboard.getContents(null), this);
            }
        } else {
            this.clipboard.setContents(this.clipboard.getContents(null), this);
        }
    }

//    private String[] imgTypes = {".png", ".jpg", ".bmp", ".jpeg", ".gif"};

    @SneakyThrows
    private byte[] getImageByteFromClipboard(Clipboard clipboard) {
        Transferable cc = clipboard.getContents(null);
        log.info("剪贴板内容：" + JSONObject.toJSONString(cc.getTransferDataFlavors()));
        if (cc.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            log.info("剪贴板为图片");
            //剪贴板为图片
            Image transferData = (Image) cc.getTransferData(DataFlavor.imageFlavor);

            int width = transferData.getWidth(null);
            int height = transferData.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(transferData, 0, 0, width, height, null);
            graphics.dispose();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", bos);
            return bos.toByteArray();
        } else if (cc.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            log.info("剪贴板为文件");
            List<File> transferData = (List<File>) cc.getTransferData(DataFlavor.javaFileListFlavor);
            if (transferData.size() != 1) {
                if (log.isInfoEnabled()) {
                    log.info("暂时不支持多文件上传");
                    return null;
                }
            }
            if (transferData.size() == 1) {
                File file = transferData.get(0);
                String fileName = file.getName();
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".bmp")
                        || fileName.endsWith(".jpeg") || fileName.endsWith(".gif")) {
                    InputStream inputStream = new FileInputStream(file);
                    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                    byte[] buff = new byte[100];
                    int rc;
                    //每次读取长度
                    int readLen = 100;
                    while ((rc = inputStream.read(buff, 0, readLen)) > 0) {
                        swapStream.write(buff, 0, rc);
                    }
                    log.info("剪贴板为图片文件");
                    return swapStream.toByteArray();
                } else {
                    log.info("剪贴板文件不为图片");
                }
            }
            log.info("剪贴板文件Size:" + transferData.size());
            return null;
        }
        log.info("剪贴板中内容为文件或图片以外内容");
        return null;
    }
}