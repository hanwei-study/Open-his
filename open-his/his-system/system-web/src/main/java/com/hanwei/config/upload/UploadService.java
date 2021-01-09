package com.hanwei.config.upload;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    private Log log= LogFactory.getLog(UploadService.class);

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties prop;

    public String uploadImage(MultipartFile file) {
        // 1、校验文件类型
        String contentType = file.getContentType();
        if (!prop.getAllowTypes().contains(contentType)) {
            throw new RuntimeException("文件类型不支持");
        }
        // 2、校验文件内容
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
                throw new RuntimeException("上传文件有问题");
            }
        } catch (IOException e) {
            log.error("校验文件内容失败....{}", e);
            throw new RuntimeException("校验文件内容失败"+e.getMessage());
        }

        try {
            // 3、上传到FastDFS
            // 3.1、获取扩展名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 3.2、上传
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            // 返回路径
            return prop.getBaseUrl() +"/"+ storePath.getFullPath();
        } catch (IOException e) {
            log.error("【文件上传】上传文件失败！....{}", e);
            throw  new RuntimeException("【文件上传】上传文件失败！"+e.getMessage());
        }
    }

    /**
     * 返回相对路径
     * @param file
     * @return
     */
    public String uploadImageReRPath(MultipartFile file) {
        // 1、校验文件类型
        String contentType = file.getContentType();
        if (!prop.getAllowTypes().contains(contentType)) {
            throw new RuntimeException("文件类型不支持");
        }
        // 2、校验文件内容
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
                throw new RuntimeException("上传文件有问题");
            }
        } catch (IOException e) {
            log.error("校验文件内容失败....{}", e);
            throw new RuntimeException("校验文件内容失败"+e.getMessage());
        }

        try {
            // 3、上传到FastDFS
            // 3.1、获取扩展名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 3.2、上传
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            // 返回路径
            return storePath.getFullPath();
        } catch (IOException e) {
            log.error("【文件上传】上传文件失败！....{}", e);
            throw  new RuntimeException("【文件上传】上传文件失败！"+e.getMessage());
        }
    }
    /**
     * 删除图片 相对路径
     * @param path
     * @return
     */
    public void deleteFile(String path) {
        if(StringUtils.isNotBlank(path)){
            try {
                //删除
                storageClient.deleteFile(path);
                log.info("【文件删除】文件删除成功！...."+path);
            } catch (Exception e) {
                log.error("【文件删除】文件删除失败！....{}", e);
                throw  new RuntimeException("【文件上传】上传文件失败！"+e.getMessage());
            }
        }
    }
}
