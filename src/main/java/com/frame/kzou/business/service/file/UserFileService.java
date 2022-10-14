package com.frame.kzou.business.service.file;

import com.frame.kzou.business.mapper.UserFileMapper;
import com.frame.kzou.business.pojo.UserFile;
import com.frame.kzou.common.CommonResult;
import com.frame.kzou.exception.BusinessException;
import com.frame.kzou.utils.DateUtil;
import com.frame.kzou.utils.FileUtil;
import com.itextpdf.xmp.impl.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/9
 * Time: 16:14
 * Description:
 */
@Service
public class UserFileService {

    @Value("${file.save-path}")
    private String savePath;

    // TODO X
//    @Autowired
    private String baseUrl="http://127.0.0.1:8080/";

    @Resource
    private UserFileMapper userFileMapper;


    @Transactional(rollbackFor = Exception.class)
    public UserFile uploadFile(String userId, MultipartFile file, String randomFileName) throws IOException {
        if (userId == null || userId.isEmpty()) {
            throw new BusinessException(CommonResult.UserNotBindError);
        }

        byte[] data = file.getBytes();
        String path = createDirectoryIfNotExist();

        // 入库
        File targetFile = new File(path, randomFileName);
        UserFile userFile = new UserFile(targetFile.getPath(), userId, "",
                file.getOriginalFilename(), (long) data.length);
        userFileMapper.insert(userFile);

        Integer fileId = userFile.getId();
        userFile.setOnline(encodeOnline(userId, fileId));

        userFileMapper.updateById(userFile);
        // 保存本地文件
        FileUtil.saveFile(data, targetFile);
        return userFile;
    }

    /**
     * 创建存储目录
     * @return  path
     */
    private String createDirectoryIfNotExist() {
        File parentFile = Paths.get(
                savePath,
                "user",
                DateUtil.getNow("yyyy"),
                DateUtil.getNow("MM")
        ).toFile();

        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return parentFile.getPath();
    }

    /**
     * 在线访问链接
     *
     * @param fileId 文件ID
     * @return link
     */
    private String encodeOnline(String userId, Integer fileId) {
        // Base64 编码
        String encode = Base64.encode(userId + "," + fileId);
        return baseUrl + "/file/user/" + encode;
    }

    /**
     * 解码
     * @param encode 在线链接的编码
     * @return  [userId, fileId]
     */
    public Integer[] decodeOnline(String encode) {
        String decode = Base64.decode(encode);
        if (decode.isEmpty()) {
            throw new BusinessException(CommonResult.ParameterError);
        }
        String[] split = decode.split(",");
        if (split.length != 2) {
            throw new BusinessException(CommonResult.ParameterError);
        }
        return new Integer[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
    }

    public UserFile queryByFileCode(String fileCode, String currUserId) {
        if (currUserId == null || currUserId.isEmpty()) {
            throw new BusinessException(CommonResult.NoAccessPermissionError);
        }
        Integer[] ids = decodeOnline(fileCode);
        Integer fileId = ids[1];
        UserFile userFile = userFileMapper.selectById(fileId);
        if (!Objects.equals(userFile.getUserId(), currUserId) && !checkAccess(userFile)) {
            throw new BusinessException(CommonResult.NoAccessPermissionError);
        }
        return userFile;
    }

    /**
     * 检测用户文件的访问权限
     * @param userFile  用户文件对象
     * @return  true-可访问（开放）、false-私有
     */
    public boolean checkAccess(UserFile userFile) {
        Date availableTime = userFile.getAvailableTime();
        if (userFile.getOpenShare() && (availableTime == null || availableTime.after(DateUtil.getNow()))) {
            return true;
        }
        return false;
    }

    /**
     * 根据ID更新
     * @param userFile  用户文件表对象
     * @return 用户文件表对象
     */
    public UserFile updateById(UserFile userFile) {
        userFileMapper.updateById(userFile);
        return userFile;
    }

    /**
     * 删除文件
     * @param fileCode
     * @param currUser
     */
    public void delFile(String fileCode, String currUser) {
        Integer fileId = null;
        try {
            fileId = decodeOnline(fileCode)[1];
        } catch (Exception e) {
            try {
                fileId = Integer.parseInt(fileCode);
            } catch (NumberFormatException exception) {
                throw new BusinessException(CommonResult.ParameterError);
            }
        }
        UserFile userFile = userFileMapper.selectById(fileId);
        if (!Objects.equals(userFile.getUserId(), currUser)) {
            throw new BusinessException(CommonResult.NoAccessPermissionError);
        }
        userFileMapper.deleteById(fileId);
    }
}
