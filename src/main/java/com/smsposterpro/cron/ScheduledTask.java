package com.smsposterpro.cron;

import com.smsposterpro.service.file.FileService;
import com.smsposterpro.service.file.impl.FileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FileService fileService;

    @Scheduled(cron = "0 0 23 ? * *")
    public void deleteTask() {
        fileService.deleteDir(FileServiceImpl.DELETEDIRSTR);
    }

}
