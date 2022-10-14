package com.frame.kzou.business.service.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.kzou.business.mapper.ShareFileMapper;
import com.frame.kzou.business.pojo.ShareFile;
import com.frame.kzou.utils.DateUtil;
import com.frame.kzou.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/9
 * Time: 13:32
 * Description:
 */
@Service
public class ShareFileService {

    @Value("${file.save-path}")
    private String savePath;

//    @Autowired(required = false)
//    @Qualifier("baseUrl")
    private String baseUrl = "http://127.0.0.1:8080/";

    @Resource
    private ShareFileMapper shareFileMapper;

    /**
     * 共享文件上传
     * @param data  文件字节数组
     * @param fileName  文件名称
     * @return  文件表对象
     * @throws IOException
     */
    public ShareFile uploadFile(byte[] data, String fileName) throws IOException {
        String path = createDirectoryIfNotExist();
        String md5Code = FileUtil.getMd5Code(data);
        ShareFile file = getFile(md5Code);
        if (file != null) {
            return file;
        }
        // 入库
        File targetFile = new File(path, fileName);
        ShareFile shareFile = new ShareFile(md5Code, targetFile.getPath(), getOnline(md5Code), (long) data.length);
        shareFileMapper.insert(shareFile);

        // 保存本地文件
        FileUtil.saveFile(data, targetFile);
        return shareFile;
    }

    /**
     * 获取文件对象
     *
     * @param code
     * @return
     */
    public ShareFile getFile(String code) {
        QueryWrapper<ShareFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return shareFileMapper.selectOne(queryWrapper);
    }

    /**
     * 创建存储目录
     * @return  path
     */
    private String createDirectoryIfNotExist() {
        File parentFile = Paths.get(
                savePath,
                "share",
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
     * @param code 文件code
     * @return link
     */
    private String getOnline(String code) {
        return baseUrl + "/file/public/" + code;
    }

}
