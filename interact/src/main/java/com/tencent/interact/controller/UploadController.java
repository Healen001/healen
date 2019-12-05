package com.tencent.interact.controller;

import com.tencent.interact.domain.Upload;
import com.tencent.interact.service.UploadService;
import com.tencent.interact.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/file")
public class UploadController {
    @Value("${file.location.imagePath}")
    private String imagePath;
    @Value("${server.port}")
    private String port;
    private String url;
    @Autowired
    private UploadService uploadService;
    // 日志
    public final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") Integer type, @RequestParam("urlType") Integer flag) {
        ArrayList<Upload> uploadList = new ArrayList<>();
        Upload upload = new Upload();
//        logger.debug("传入的文件参数：{}", JSON.toJSONString(file, true));
        if (Objects.isNull(file) || file.isEmpty()) {
            logger.error("文件为空");
            return new Result(201, "文件为空，请重新上传", "");
        }
        try {
            UUID uuid = UUID.randomUUID();
            byte[] bytes = file.getBytes();
            Path path = null;
            if (flag == 0) {
                path = Paths.get(imagePath + "hand/" + uuid.toString() + file.getOriginalFilename());
            } else if (flag == 1) {
                path = Paths.get(imagePath + "image/" + uuid.toString() + file.getOriginalFilename());
            } else {
                path = Paths.get(imagePath + "other/" + uuid.toString() + file.getOriginalFilename());
            }
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                if (flag == 0) {
                    Files.createDirectories(Paths.get(imagePath + "hand/"));
                } else if (flag == 1) {
                    Files.createDirectories(Paths.get(imagePath + "image/"));
                } else {
                    Files.createDirectories(Paths.get(imagePath + "other/"));
                }

            }
            //文件写入指定路径
            Files.write(path, bytes);
            logger.debug("文件写入成功...");
            if (type == 1) {
                //调用截取视频第一帧的方法
                boolean falg = uploadService.getThumb(path.toString(), path.toString().substring(0, path.toString().length() - 4) + ".jpg");
                if (falg) {
                    File file1 = new File(path.toString().substring(0, path.toString().length() - 4) + ".jpg");
                    BufferedImage bufferedImage = ImageIO.read(file1);
                    int width = bufferedImage.getWidth();
                    int height = bufferedImage.getHeight();
                    Upload upload1 = new Upload();
                    if (width >= height) {
                        upload1.setType("h");
                    } else {
                        upload1.setType("s");
                    }
                    upload1.setId(UUID.randomUUID().toString());
                    upload1.setName(uuid.toString() + file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 4) + ".jpg");
                    upload1.setUrl("other/" + upload1.getName());
                    uploadList.add(upload1);
                }
            }
            upload.setId(UUID.randomUUID().toString());
            upload.setName(uuid.toString()+file.getOriginalFilename());
            if (flag == 0) {
                upload.setUrl("hand/" + uuid.toString() + file.getOriginalFilename());
            } else if (flag == 1) {
                upload.setUrl("image/" + uuid.toString() + file.getOriginalFilename());
            } else {
                upload.setUrl("other/" + uuid.toString() + file.getOriginalFilename());
            }
            uploadList.add(upload);
            return Result.ok(uploadList);
        } catch (Exception e) {
            logger.error("", e);
            e.printStackTrace();
            return new Result(201, "e.getLocalizedMessage()", "");
        }
    }

}