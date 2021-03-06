package com.leyou.upload.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.OSSProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class UpLoadService {



    @Autowired
    private OSSProperties prop;

    @Autowired
    private OSS client;

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
        File dir = new File("/usr/local/var/www");
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

    public Map<String,Object> getSignature() {

        try {
            long expireTime = prop.getExpireTime();
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, prop.getMaxFileSize());
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, prop.getDir());

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, Object> respMap = new LinkedHashMap<>();
            respMap.put("accessId", prop.getAccessKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", prop.getDir());
            respMap.put("host", prop.getHost());
            respMap.put("expire", expireEndTime);
            return respMap;
        }catch (Exception e){
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

    }
}
