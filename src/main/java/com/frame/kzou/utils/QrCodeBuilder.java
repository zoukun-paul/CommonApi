package com.frame.kzou.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/15
 * Time: 21:02
 * Description: 二维码构建器
 */
public  class QrCodeBuilder {
    private static final QRCodeWriter qrCodeWriter = new QRCodeWriter();
    /**
     * 必备字段
     */
    private final String qRContent;
    private final int qrSize;
    private final Map<EncodeHintType, Object> hints;

    /**
     * 核心字段
     */
    private BitMatrix bitMatrix = null;
    private BufferedImage qrImg;
    private int logoMargin;

    /**
     * 其他字段
     */
    private int qrColor = -1;
    private Image logoImg = null;

    private void initQrCode()  {
        try {
            bitMatrix = qrCodeWriter.encode(qRContent, BarcodeFormat.QR_CODE, qrSize, qrSize, hints);
            qrImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            throw new InvalidParameterException("qRContent error");
        }
    }

    /**
     * @param content   二维码编码内容
     * @param size      二维码大小
     * @param hints     配置信息
     */
    public QrCodeBuilder(String content, int size, Map<EncodeHintType, Object> hints) {
        qrSize = size;
        qRContent = content;
        this.hints = hints;
        hints.computeIfAbsent(EncodeHintType.ERROR_CORRECTION, k -> ErrorCorrectionLevel.H);
        hints.computeIfAbsent(EncodeHintType.CHARACTER_SET, k -> "utf-8");
        hints.computeIfAbsent(EncodeHintType.MARGIN, k -> 0);
        initQrCode();
    }

    /**
     * @param content   二维码编码内容
     * @param size      二维码大小
     */
    public QrCodeBuilder(String content, int size) {
        this(content, size, new HashMap<>());
    }

    /**
     * @param content   二维码类容
     */
    public QrCodeBuilder(String content) {
        this(content, 300);
    }

    /**
     * 给二维码设置颜色
     * @param color 16进制表示的颜色
     * @return QrCodeBuilder
     */
    public QrCodeBuilder setColor(int color) {
        this.qrColor = color;
        int qrWidth = bitMatrix.getWidth();
        int qrHeight = bitMatrix.getHeight();
        // 定义空白的缓冲流图
        qrImg = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_RGB);
        // 整改二维码的颜色
        for (int i = 0; i < qrWidth; i++) {
            for (int j = 0; j < qrHeight; j++) {
                // 获取二维码上的每个点（bitMatrix.get => true: 黑色  false: 白色）
                int rbg = bitMatrix.get(i, j) ? color : 0xFFFFFF;
                qrImg.setRGB(i, j, rbg);
            }
        }
        return this;
    }

    /**
     * 给二维码设置Logo
     * @param logo  logo
     * @param margin logo的 margin
     * @return QrCodeBuilder
     */
    public QrCodeBuilder setLogo(Image logo, int margin) {
        logoImg = logo;
        logoMargin = margin;
        return this;
    }

    /**
     * 真正添加Logo的实现方法
     */
    private void addLogo() {
        if (qrColor == -1) {
            setColor(0x000);
        }
        if (logoImg.getWidth(null) * 2 > qrImg.getWidth() ||
                logoImg.getHeight(null) * 2 > qrImg.getHeight()) {
            // 缩放logo
            logoImg = logoImg.getScaledInstance(
                    qrImg.getWidth() / 3,
                    qrImg.getHeight() / 3,
                    Image.SCALE_AREA_AVERAGING
            );
        }
        int logoWidth = logoImg.getWidth(null);
        int logoHeight = logoImg.getHeight(null);
        // logo 的放置位置
        int x = (qrImg.getWidth() - logoWidth) / 2;
        int y = (qrImg.getHeight() - logoHeight) / 2;
        // 绘制Logo
        Graphics2D graph = qrImg.createGraphics();
        graph.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
        // 设置logo 的边框大小
        graph.setStroke(new BasicStroke(logoMargin));
        // 绘制圆角空心矩形（圆角化logo）
        graph.drawRoundRect(x, y, logoWidth, logoHeight, 18, 18);
        // 设置logo 的边框颜色
        graph.setColor(Color.WHITE);
        graph.drawRect(x,y,logoWidth,logoHeight);
        graph.dispose();
        logoImg.flush();
    }

    public QrCodeBuilder setLogo(Image logo) {
        return setLogo(logo, 3);
    }

    /**
     * 构建二维码
     * @return  BufferedImage
     */
    public BufferedImage build() {
        // logo 想要单独处理（添加logo，必须在setColor之后）
        if (logoImg!=null) {
            addLogo();
        }
        bitMatrix.clear();
        return qrImg;
    }

    /**
     * 测试方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        BufferedImage logo = ImageIO.read(new File("F:\\desktop\\OIP.jpg"));

        BufferedImage qr = new QrCodeBuilder("https://github.com/zoukun-paul/CommonApi",200)
                .setLogo(logo)
                .setColor(0xc31)
                .build();
        Path path = FileSystems.getDefault().getPath("a61.png");
        ImageIO.write(qr, "png", path.toFile());
    }
}


