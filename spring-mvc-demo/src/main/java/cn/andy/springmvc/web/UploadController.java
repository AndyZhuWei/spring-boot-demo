package cn.andy.springmvc.web;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author: zhuwei
 * @Date:2018/10/24 15:23
 * @Description:
 */
@Controller
public class UploadController {

    //使用MultipartFile file接受上传的文件
    @RequestMapping(value="/upload",method = RequestMethod.POST)
    public @ResponseBody String upload(MultipartFile file) {
        try {
            //使用FileUtils.writeByteArrayToFile快速写文件到磁盘
            FileUtils.writeByteArrayToFile(new File("e:/upload/"+file.getOriginalFilename()),file.getBytes());
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            return "wrong";
        }
    }
}
