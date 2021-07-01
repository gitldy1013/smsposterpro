package com.smsposterpro.utils;

import net.sourceforge.tess4j.Tesseract;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonUtils {

    public static final Pattern FILE_PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");
    public static final Pattern DIR_PATTERN = Pattern.compile("[:*?\"<>|]");

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
//        String result = FindOCR("C:\\Users\\liudongyang\\Desktop\\smsposterpro\\file\\test.png", true);
//        System.out.println(result);
        HashMap<String, Element> map = new HashMap<>();
        map.put("12d3", null);
        map.put("432", null);
        map.put("432", null);
        map.put("112", null);
        map.put("1df12", null);
        map.put("d112", null);
        map.put("1s12", null);
        map.put("1x12", null);
        map.put("11d2", null);
        map.put("11sz2", null);
        map.put("11c2", null);
        map.put("11z2", null);
        List<Set<Map.Entry<String, Element>>> split = CommonUtils.split(map, 3);
        System.out.println(split);
        String tests = "temp\\绚丽舞台|子滢\\绚丽舞台子滢";
        String s = filenameFilter(tests, FILE_PATTERN);
        System.out.println(s);
    }

    /**
     * @param map   需要分隔的 集合
     * @param input 指定分隔size
     * @return
     */
    public static List<Set<Map.Entry<String, Element>>> split(HashMap<String, Element> map, int input) {
        int size = map.size();
        int limit = (size + input - 1) / input;
        Set<Map.Entry<String, Element>> entries = map.entrySet();
        List<Set<Map.Entry<String, Element>>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).
                map(a -> map.entrySet().stream().skip(a * input).limit(input).collect(Collectors.toSet())).
                collect(Collectors.toList());
        //当输入数量小于分隔数量需反转
        if (input < limit) {
            splitList = Stream.iterate(0, n -> n + 1).limit(input).
                    map(a -> map.entrySet().stream().skip(a * limit).limit(limit).collect(Collectors.toSet())).
                    collect(Collectors.toList());
        }
        return splitList;
    }

    public static String filenameFilter(String str, Pattern pattern) {
        return str == null ? null : pattern.matcher(str).replaceAll("");
    }
}
