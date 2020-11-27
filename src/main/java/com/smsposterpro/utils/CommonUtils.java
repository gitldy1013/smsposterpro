package com.smsposterpro.utils;

import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class CommonUtils {

    /**
     * @param srImage 图片路径
     * @param ZH_CN   是否使用中文训练库,true-是
     * @return 识别结果
     */
    public static String FindOCR(String srImage, boolean ZH_CN) {
        try {
            double start = System.currentTimeMillis();
            File imageFile = new File(srImage);
            if (!imageFile.exists()) {
                return "图片不存在";
            }
            BufferedImage textImage = ImageIO.read(imageFile);
            Tesseract instance = new Tesseract();
            instance.setTessVariable("user_defined_dpi", "300");
            instance.setDatapath("C:\\Users\\liudongyang\\Desktop\\smsposterpro\\file");
            if (ZH_CN) {
                instance.setLanguage("chi_sim");//中文识别
            }
            String result = instance.doOCR(textImage);
            double end = System.currentTimeMillis();
            System.out.println("耗时" + (end - start) / 1000 + " s");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "发生未知错误";
        }
    }

    public static String getTime(long time) {
        long hour = time / (60 * 60 * 1000);
        long minute = (time - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        return (hour + "时" + minute + "分 " + second + "秒");
    }

    public static void main(String[] args) throws Exception {
        String result = FindOCR("C:\\Users\\liudongyang\\Desktop\\smsposterpro\\file\\test.png", true);
        System.out.println(result);
    }
}
