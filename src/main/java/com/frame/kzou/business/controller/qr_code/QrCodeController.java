package com.frame.kzou.business.controller.qr_code;

import com.frame.kzou.business.base.BaseController;
import com.frame.kzou.business.vo.qr.QrReqVo;
import com.frame.kzou.common.CommonResult;
import com.frame.kzou.exception.BusinessException;
import com.frame.kzou.utils.QrCodeBuilder;
import com.frame.kzou.utils.ValidUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/15
 * Time: 9:54
 * Description: 二维码
 */
@Controller
@RequestMapping("/qr")
public class QrCodeController extends BaseController {

    @GetMapping("create")
    public void createQRCode(String content, Integer size) throws IOException {
        if (ValidUtil.isEmptyString(content)) {
            throw new BusinessException(CommonResult.ParameterError);
        }
        QrCodeBuilder qrCodeBuilder = size == null ? new QrCodeBuilder(content) : new QrCodeBuilder(content, size);
        BufferedImage res = qrCodeBuilder
                .setColor(Color.black)
                .build();
        exportQrImg(res);
    }

    @PostMapping("create")
    public void createQRCode(@RequestBody QrReqVo qrReqVo) throws IOException {
        Integer size = qrReqVo.getSize() == null ? 300 : qrReqVo.getSize();
        String content = qrReqVo.getContent();
        String logo = qrReqVo.getLogo();
        String bgColor = qrReqVo.getBgColor();
        String qrColor = qrReqVo.getQrColor();
        QrCodeBuilder qrCodeBuilder = new QrCodeBuilder(content, size)
                .setBgColor(bgColor == null ? Color.WHITE : Color.decode(bgColor))
                .setColor(qrColor == null ? Color.decode("0x000001") : Color.decode(qrColor));
        // 请求网络图片
        if (!ValidUtil.isEmptyString(logo)) {
            BufferedImage bufferedImage = ImageIO.read(new URL(logo));
            qrCodeBuilder.setLogo(bufferedImage);
        }

        exportQrImg(qrCodeBuilder.build());
    }

    /**
     * 响应二维码
     * @param img QRdata
     * @throws IOException
     */
    private void exportQrImg(BufferedImage img) throws IOException {
        HttpServletResponse response = getResponse();
        response.addHeader("Content-Disposition", "attachment;filename=qr.png");
        response.setContentType("multipart/form-data");
        ImageIO.write(img, "png", response.getOutputStream());
    }

}
