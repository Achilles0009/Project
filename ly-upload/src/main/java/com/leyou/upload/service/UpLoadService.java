package com.leyou.upload.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UpLoadService {

    //支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png",
            "image/jpeg","image/bmp");


    public String upload(MultipartFile file) {
        //1、图片信息校验
        //（1）校验文件类型
        String type = file.getContentType();
        if (!suffixes.contains(type)){
            throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        //2、校验图片内容
        BufferedImage image = null;

        try {
            image = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        if (image == null){
            throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        //3、保存图片
        //3.1、生成保存目录，保存到nginx所在目录的html下，这样可以直接通过nginx来访问
        File dir = new File("/usr/local/Cellar/nginx/1.17.0/html");
        if (!dir.exists()){
            dir.mkdirs();
        }

        //3.2、保存图片
        try {
            file.transferTo(new File(dir,file.getOriginalFilename()));

            //3.3、拼接图片地址
            return "http://image.leyou.com/" + file.getOriginalFilename();

        } catch (IOException e) {
            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }

    }
}
