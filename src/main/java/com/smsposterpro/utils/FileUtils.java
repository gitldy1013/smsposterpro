package com.smsposterpro.utils;

import com.smsposterpro.exception.AesException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_FILE_DIR;

@Slf4j
public class FileUtils {
    public static final String DELETEDIRSTR = "./" + TEMP_FILE_DIR;

    public static void main(String[] args) {
        FileUtils.deleteDir(FileUtils.DELETEDIRSTR, "mp4", "jpg", "jpeg", "png");
    }

    /**
     * @Author: dyliu
     * @Description: 删除指定目录下所有文件 可指定类型保存
     * @Date: 14:00 2020/11/28
     * @Param: [dir, flag]
     * @return: void
     **/
    public static void deleteDir(String dir, String... flag) {
        File file = new File(dir);
        deleteDir(file, flag);
    }

    /**
     * flag为保留文件的类型列表数组
     *
     * @param file
     * @param flag
     */
    public static void deleteDir(File file, String... flag) {
        if (file.isDirectory()) {
            /*递归删除目录中的子目录下*/
            Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(child -> deleteDir(child, flag));
            file.delete();
        }
        if (file.isFile()) {
            if (!(flag != null && flag.length > 0 && file.getName().contains(".") && Arrays.asList(flag).contains(file.getName().substring(file.getName().lastIndexOf(".") + 1)))) {
                file.delete();
            } else {
                if (!file.getName().startsWith(DownM3U8FileUtil.PREFIX)) {
                    file.delete();
                } else {
                    log.info("此为资源文件：{}", file.getName());
                }
            }
        }
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     */
    public static void fileToZip(String sourceFilePath, String zipFilePath, String fileName) throws AesException {
        try {
            getZipOutputStream(sourceFilePath, zipFilePath, fileName, null).close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("压缩文件流关闭异常:", e);
        }
    }

    private static ZipOutputStream getZipOutputStream(String sourceFilePath, String zipFilePath, String fileName, ZipOutputStream zos) throws AesException {
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis;
        BufferedInputStream bis = null;
        if (!sourceFile.exists()) {
            throw new AesException("待压缩的文件目录：" + sourceFilePath + "不存在");
        }
        try {
            File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
            File[] sourceFiles = sourceFile.listFiles();
            if (null == sourceFiles || sourceFiles.length < 1) {
                throw new AesException("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
            } else {
                if (null == zos) {
                    zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
                }
                byte[] bufs = new byte[1024 * 10];
                for (File file : sourceFiles) {
                    if (file.isDirectory()) {
                        zos = getZipOutputStream(file.getAbsolutePath(), zipFilePath, fileName, zos);
                    } else {
                        String[] split = file.getPath().split(fileName);
                        String fileDir = file.getName();
                        if (split.length > 1) {
                            fileDir = split[1].substring(1);
                        }
                        //创建ZIP实体，并添加进压缩包
                        ZipEntry zipEntry = new ZipEntry(fileDir);
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(file);
                        bis = new BufferedInputStream(fis, 1024 * 10);
                        int read = 0;
                        while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("压缩文件异常:", e);
            throw new AesException("压缩文件异常");
        } finally {
            //关闭流
            try {
                if (null != bis) bis.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("压缩文件流关闭异常:", e);
            }
        }
        return zos;
    }
}
