package com.smsposterpro.cron;

import com.smsposterpro.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 使用quartz框架 项目中所有的定时任务统一放这里处理
 */
@Component
@EnableScheduling
@Async // 启动多线程
@Slf4j
public class ScheduledTask {

    @Scheduled(cron = "0 0 23 ? * *")
    public void deleteTask() {
        int count = FileUtils.deleteDirWithCount(FileUtils.DELETEDIRSTR, "mp4", "jpg", "jpeg", "png");
        log.info("总文件数为：" + count);
    }

}
