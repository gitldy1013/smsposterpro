package com.smsposterpro.utils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * javascript工具类,用于调用js方法
 *
 * @author dyliu
 */
public class JavaScriptUtil {
    public static void main(String[] args) throws ScriptException {
        //('9 8=[];8.0("3.d.1");8.0("3.l.1");8.0("3.j.1");8.0("3.i.1");8.0("3.h.1");8.0("3.g.1");8.0("3.f.1");8.0("3.e.1");9 n=8[4.b(4.a()*8.c)],7=[];7.0("3.d.1");7.0("3.l.1");7.0("3.j.1");7.0("3.i.1");7.0("3.h.1");7.0("3.g.1");7.0("3.f.1");7.0("3.e.1");9 t=7[4.b(4.a()*7.c)],6=[];6.0("3.d.1");6.0("3.l.1");6.0("3.j.1");6.0("3.i.1");6.0("3.h.1");6.0("3.g.1");6.0("3.f.1");6.0("3.e.1");9 p=6[4.b(4.a()*6.c)],5=[];5.0("3.d.1");5.0("3.l.1");5.0("3.j.1");5.0("3.i.1");5.0("3.h.1");5.0("3.g.1");5.0("3.f.1");5.0("3.e.1");9 o=5[4.b(4.a()*5.c)],m=[];m.0("q-r.1");9 s=m[4.b(4.a()*m.c)],k=[];k.0("2.u.1");9 v=k[4.b(4.a()*k.c)];', 32, 32, 'push|com||m3u8|Math|ipp4|ipp3|ipp2|ipp1|var|random|floor|length|40cdn|34cdn|63cdn|48cdn|47cdn|46cdn|44cdn|ipp6|41cdn|ipp5|CN1|CN4|CN3|dadi|bo|CN5|CN2|ddyunbo|CN6'.split('|'), 0, {}))
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("p", "9 8=[];8.0(\"3.d.1\");8.0(\"3.l.1\");8.0(\"3.j.1\");8.0(\"3.i.1\");8.0(\"3.h.1\");8.0(\"3.g.1\");8.0(\"3.f.1\");8.0(\"3.e.1\");9 n=8[4.b(4.a()*8.c)],7=[];7.0(\"3.d.1\");7.0(\"3.l.1\");7.0(\"3.j.1\");7.0(\"3.i.1\");7.0(\"3.h.1\");7.0(\"3.g.1\");7.0(\"3.f.1\");7.0(\"3.e.1\");9 t=7[4.b(4.a()*7.c)],6=[];6.0(\"3.d.1\");6.0(\"3.l.1\");6.0(\"3.j.1\");6.0(\"3.i.1\");6.0(\"3.h.1\");6.0(\"3.g.1\");6.0(\"3.f.1\");6.0(\"3.e.1\");9 p=6[4.b(4.a()*6.c)],5=[];5.0(\"3.d.1\");5.0(\"3.l.1\");5.0(\"3.j.1\");5.0(\"3.i.1\");5.0(\"3.h.1\");5.0(\"3.g.1\");5.0(\"3.f.1\");5.0(\"3.e.1\");9 o=5[4.b(4.a()*5.c)],m=[];m.0(\"q-r.1\");9 s=m[4.b(4.a()*m.c)],k=[];k.0(\"2.u.1\");9 v=k[4.b(4.a()*k.c)];");
//        params.put("a", 32);
//        params.put("c", 32);
//        params.put("k", "push|com||m3u8|Math|ipp4|ipp3|ipp2|ipp1|var|random|floor|length|40cdn|34cdn|63cdn|48cdn|47cdn|46cdn|44cdn|ipp6|41cdn|ipp5|CN1|CN4|CN3|dadi|bo|CN5|CN2|ddyunbo|CN6".split("|"));
//        params.put("e", 0);
//        params.put("r", new ArrayList<>());
//        ScriptObjectMirror res = (ScriptObjectMirror) jsCalculate(new File(JavaScriptUtil.class.getResource("/static/Cn.js").getPath()),
//                "CN", params);
//        for (String key : res.keySet()) {
//            System.out.println(key + ":" + res.get(key));
//        }


        String url = getUrl("https://\"+CN1+\"/movie-hls/170831/s16/index.m3u8");
        System.out.println(url);
    }

    /**
     * 使用指定的js代码,计算结果
     *
     * @param jsCode       js代码
     * @param functionName 需要调用的功能名
     * @param params       参数
     * @return
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public static Object jsCalculateFunction(String jsCode,
                                             String functionName, Object... params) throws ScriptException,
            NoSuchMethodException {
        Object result = null;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        engine.eval(jsCode);
        if (engine instanceof Invocable) {
            // 调用merge方法，并传入两个参数
            Invocable invoke = (Invocable) engine;
            Object obj = invoke.invokeFunction(functionName, params);
            return obj;
        }
        return result;
    }

    /**
     * 调用js的encodeURIComponent方法
     *
     * @param str 需要encode的内容
     * @return 编码后的内容
     * @throws ScriptException
     */
    public static String encodeURIComponent(String str, String functionName) throws ScriptException, NoSuchMethodException {
        Map m = new HashMap();
        m.put("str", str);
        return jsCalculate("result=encodeURIComponent(str)", functionName, m);
    }

    /**
     * 执行js代码,需要从ScriptEngine内自行获取结果
     *
     * @param jsCode js代码
     * @param params 参数
     * @return
     * @throws ScriptException
     */
    public static String jsCalculate(String jsCode, String functionName,
                                     Map<String, Object> params) throws ScriptException, NoSuchMethodException {
        String res = null;
        ScriptEngine engine = getScriptEngine(params);
        engine.eval(jsCode);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;
            Object eval = invoke.invokeFunction(functionName, params);
            if (eval != null) {
                res = eval.toString();
            }
        }
        return res;
    }

    public static Object jsCalculate(File jsFile, String functionName,
                                     Map<String, Object> params) throws ScriptException {
        Object res = null;
        ScriptEngine engine = getScriptEngine(params);
        try (Stream<String> lines = Files.lines(Paths.get(jsFile.getPath()))) {
            String content = lines.collect(Collectors.joining(System.lineSeparator()));
            engine.eval(content);
            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;
                res = invoke.invokeFunction(functionName, params);
            }
        } catch (IOException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static ScriptEngine getScriptEngine(Map<String, Object> params) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");

        if (null != params && params.size() > 0) {
            Set<Entry<String, Object>> set = params.entrySet();
            Iterator<Entry<String, Object>> it = set.iterator();

            while (it.hasNext()) {
                Entry<String, Object> e = it.next();
                String key = e.getKey();
                Object valueObj = e.getValue();
                engine.put(key, valueObj);
            }
        }
        return engine;
    }


    public static ScriptEngine getScripteEngine() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        return engine;
    }

    public static String getUrl(String url) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            String filePath = JavaScriptUtil.class.getResource("/static/Cn.js").getPath();
            engine.eval(new FileReader(new File(filePath)));
            if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                ScriptObjectMirror res = (ScriptObjectMirror) in.invokeFunction("CN");
                for (Map.Entry<String, Object> r : res.entrySet()) {
                    String key = r.getKey();
                    String value = r.getValue().toString();
                    url = url.replaceAll("\"\\+CN" + key + "\\+\"", value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
