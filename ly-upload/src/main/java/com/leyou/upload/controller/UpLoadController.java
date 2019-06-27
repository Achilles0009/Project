package com.leyou.upload.controller;

import com.leyou.upload.service.UpLoadService;
import org.assertj.core.internal.ObjectArrayElementComparisonStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
//@RequestMapping("upload")
public class UpLoadController {

    @Autowired
    private UpLoadService upLoadService;

    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        return ResponseEntity.ok(this.upLoadService.upload(file));
    }

    @GetMapping("signature")
    public ResponseEntity<Map<String,Object>> getAliSignature(){
        return ResponseEntity.ok(upLoadService.getSignature());
    }

}
