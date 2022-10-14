package com.frame.kzou.business.controller.file;

import com.frame.kzou.business.base.BaseController;
import com.frame.kzou.business.pojo.ShareFile;
import com.frame.kzou.business.service.file.ShareFileService;
import com.frame.kzou.common.ResultResponse;
import com.frame.kzou.utils.FileUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/9
 * Time: 13:19
 * Description: 共享文件控制器
 */
@RestController
@RequestMapping("file/public")
public class ShareFileController extends BaseController {

    @Autowired
    private ShareFileService shareFileService;

    @ApiOperation(value = "上传文件")
    @PostMapping("upload")
    public ResultResponse uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        assert originalName != null;
        String type = FileUtil.getFileType(originalName);
        ShareFile shareFile = shareFileService.uploadFile(file.getBytes(), FileUtil.randomFileName(type));
        return ResultResponse.success(shareFile.getOnline());
    }

    @ApiOperation("访问在线共享文件")
    @GetMapping("{code}")
    public void getOnlineFile(@PathVariable String code) {
        ShareFile shareFile = shareFileService.getFile(code);
        File file = new File(shareFile.getSource());
        if (file.exists()) {
            exportFile(file);
        }
    }
}
