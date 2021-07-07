package com.smsposterpro.utils;

import net.sourceforge.tess4j.Tesseract;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.nodes.Element;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        LinkedHashMap<String, Element> map = new LinkedHashMap<>();
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
        List<Set<Map.Entry<String, Element>>> split = CommonUtils.split(map, 2);
        List<LinkedHashMap<String, Element>> linkedHashMaps = CommonUtils.splitOrder(map, 3);
//        System.out.println(split);
        String tests = "temp\\绚丽舞台|子滢\\绚丽舞台子滢";
        String s = filenameFilter(tests, FILE_PATTERN);
        System.out.println(s);
        System.out.println(linkedHashMaps);
    }

    /**
     * @param map   需要分隔的 集合
     * @param input 指定分隔size
     * @return
     */
    public static List<Set<Map.Entry<String, Element>>> split(LinkedHashMap<String, Element> map, int input) {
        int size = map.size();
        int limit = (size % input > 0) ? ((size / input) + 1) : (size / input);
        //当输入数量小于分隔数量需反转
        //if (input < limit) {
        //    int finalLimit = limit;
        //    splitList = Stream.iterate(0, n -> n + 1).limit(input).
        //            map(a -> map.entrySet().stream().skip(a * finalLimit).limit(finalLimit).collect(Collectors.toSet())).
        //            collect(Collectors.toList());
        //}
        return Stream.iterate(0, n -> n + 1).limit(limit).
                map(a -> map.entrySet().stream().skip(a * input).limit(input).collect(Collectors.toSet())).
                collect(Collectors.toList());
    }

    public static List<LinkedHashMap<String, Element>> splitOrder(LinkedHashMap<String, Element> map, int input) {
        if (input > map.size()) {
            input = map.size();
        }
        List<LinkedHashMap<String, Element>> res = new ArrayList<>();
        int i = 0;
        LinkedHashMap<String, Element> se = new LinkedHashMap<>();
        for (Map.Entry<String, Element> entry : map.entrySet()) {
            if ((i % input) == 0) {
                if (i != 0) {
                    res.add(se);
                }
                se = new LinkedHashMap<>();
            }
            se.put(entry.getKey(), entry.getValue());
            i += 1;
        }
        if (map.size() % input > 0 || i == map.size()) {
            res.add(se);
        }
        return res;
    }

    public static String filenameFilter(String str, Pattern pattern) {
        return str == null ? null : pattern.matcher(str).replaceAll("");
    }
}
