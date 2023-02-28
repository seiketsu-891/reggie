package com.reggie.controller;

import com.reggie.common.JsonResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    // 报错：我这里不小心写成了#
    private String basePath;

    // 这里是一个模拟方法，就是把文件存放到本地；
    // 前面几步基本上是上传文件都会做的
    @PostMapping("/upload")
    // 报错1： 之前test的时候把admin账号禁用了， 导致登录不上，提示用户名密码失败
    // 去排除service的条件，才发现；
    // 报错2： 404，忘记写地址了
    public JsonResponse<String> uploadFile(MultipartFile file) {
        // 它这里都没有进行参数校验，ok吧...
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + extension;

        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            //
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse.success(fileName);
    }
}
