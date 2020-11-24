package com.smsposterpro.service.file.impl;

import com.smsposterpro.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_FILE_DIR;

/**
 * FileService实现
 *
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    public static final String DELETEDIRSTR = "./" + TEMP_FILE_DIR;

    @Override
    public void deleteDir(String dir) {
        File file = new File(dir);
        boolean delete;
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children != null && children.length > 0) {
                /*递归删除目录中的子目录下*/
                for (String child : children) {
                    deleteDir(file.getPath() + "/" + child);
                }
            }
            delete = file.delete();
            log.info("删除目录: {}", delete);
        } else {
            delete = file.delete();
            log.info("删除文件: {}", delete);
        }
    }

}
