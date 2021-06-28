package com.smsposterpro.service.js;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("dev")
public class JSTest {

    @Test
    public void testJs() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            String filePath = this.getClass().getResource("/static/Cn.js").getPath();
            engine.eval(new FileReader(new File(filePath)));
            if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                ScriptObjectMirror res = (ScriptObjectMirror) in.invokeFunction("CN");
                for (Map.Entry<String, Object> r : res.entrySet()) {
                    String key = r.getKey();
                    Object value = r.getValue();
                    System.out.println(key + "：" + value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
