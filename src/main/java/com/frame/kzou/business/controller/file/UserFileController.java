package com.frame.kzou.business.controller.file;

import com.frame.kzou.business.base.BaseController;
import com.frame.kzou.business.pojo.UserFile;
import com.frame.kzou.business.service.file.UserFileService;
import com.frame.kzou.business.vo.file.UserFileUpdateVo;
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
 * Time: 16:13
 * Description: 用户文件控制器
 */
@RestController
@RequestMapping("/file/user/")
public class UserFileController extends BaseController {

    @Autowired(required = false)
    private UserFileService userFileService;

    @ApiOperation(value = "上传文件")
    @PostMapping("upload/{userId}")
    public ResultResponse uploadFile(@RequestPart("file") MultipartFile file, @PathVariable String userId) throws IOException {
        String originalName = file.getOriginalFilename();
        assert originalName != null;
        String type = FileUtil.getFileType(originalName);
        UserFile shareFile = userFileService.uploadFile(userId, file, FileUtil.randomFileName(type));
        return ResultResponse.success(shareFile);
    }

    @ApiOperation(value = "上传文件")
    @PostMapping("upload")
    public ResultResponse uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        return uploadFile(file, getBindUser());
    }

    @ApiOperation(value = "查询文件信息")
    @GetMapping("query/{fileCode}")
    public ResultResponse queryFileInfo(@PathVariable String fileCode,String userId) {
        UserFile userFile = userFileService.queryByFileCode(fileCode, userId == null ? getBindUser() : userId);
        return ResultResponse.success(userFile);
    }

    @ApiOperation(value = "下载文件")
    @GetMapping("{fileCode}")
    public void downloadUserFile(@PathVariable String fileCode, String userId) {
        UserFile userFile = userFileService.queryByFileCode(fileCode, userId == null ? getBindUser() : userId);
        exportFile(new File(userFile.getSource()));
    }

    @ApiOperation(value = "更新文件")
    @PostMapping("update")
    public ResultResponse<UserFile> updateFile(UserFileUpdateVo userFile) {
        UserFile file = userFileService.updateById(userFile.ofUserFile());
        file.setSource(null);
        return ResultResponse.success(file);
    }

    @ApiOperation(value = "删除文件[fileCode|fileId]")
    @DeleteMapping("del/{code}")
    public ResultResponse<Void> delFile(@PathVariable String code, String userId) {
        userFileService.delFile(code, userId == null ? getBindUser() : userId);
        return ResultResponse.success();
    }

}
