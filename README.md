# quartz

#### 项目简介
1. 基于quartz的二次集成
2. 支持集群
3. 支持其它web项目进行功能开发
4. 动态控制定时任务启动、暂停、重启、删除、添加、修改
5. 支持多数据库
6. 支持自实现Scheduler、Job、Trigger监听，系统自动注册

#### 使用教程

1. 引入依赖
2. 修改jdbc.properties数据源配置
3. 继承AbstractQuartzTask，实现自己的定时任务
4. 功能开发
5. 任务展示
6. 调用接口控制任务

#### 配置示例
```yaml
sys:
  quartz:
    thread-pool:
      core-pool-size: 20
      max-pool-size: 50
      queue-capacity: 100
    scheduler:
      config-location: classpath:quartz.properties
      scheduler-name: data-push-gz
      application-context-scheduler-contextKey: applicationContext
      overwrite-existing-jobs: true
      auto-startup: true
      Startup-delay: 10
```

```java
package com.xbd.quartz.config;

import com.xbd.quartz.AutowiredSpringBeanJobFactory;
import com.xbd.quartz.QuartzListenerAware;
import com.xbd.quartz.handler.DefaultQuartzTaskHandler;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class QuartzConfig {

    @Autowired
    private QuartzProperties quartzProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(quartzProperties.getThreadPool().getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(quartzProperties.getThreadPool().getMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(quartzProperties.getThreadPool().getQueueCapacity());

        return threadPoolTaskExecutor;
    }

    @Bean
    public AutowiredSpringBeanJobFactory autowiredSpringBeanJobFactory() {
        AutowiredSpringBeanJobFactory autowiredSpringBeanJobFactory = new AutowiredSpringBeanJobFactory();
        return autowiredSpringBeanJobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setConfigLocation(quartzProperties.getScheduler().getConfigLocation());
//        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setJobFactory(autowiredSpringBeanJobFactory());
        schedulerFactoryBean.setSchedulerName(quartzProperties.getScheduler().getSchedulerName());
        schedulerFactoryBean.setTaskExecutor(threadPoolTaskExecutor());
        schedulerFactoryBean.setTransactionManager(transactionManager);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey(quartzProperties.getScheduler().getApplicationContextSchedulerContextKey());
        schedulerFactoryBean.setOverwriteExistingJobs(quartzProperties.getScheduler().isOverwriteExistingJobs());
        schedulerFactoryBean.setAutoStartup(quartzProperties.getScheduler().isAutoStartup());
        schedulerFactoryBean.setStartupDelay(quartzProperties.getScheduler().getStartupDelay());

        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getObject();
    }

    @Bean
    public QuartzListenerAware quartzListenerAware() {
        QuartzListenerAware quartzListenerAware = new QuartzListenerAware();
        quartzListenerAware.setScheduler(scheduler());
        return quartzListenerAware;
    }

    @Bean
    public DefaultQuartzTaskHandler taskService() {
        DefaultQuartzTaskHandler taskService = new DefaultQuartzTaskHandler();
        taskService.setScheduler(scheduler());
        return taskService;
    }
}
```

#### 使用说明

1. QuartzTaskHandler 任务处理接口，其中有添加、修改、删除、暂停、重启等功能
2. AbstractSchedulerListener Scheduler监听，可自行实现自己需要的Scheduler监听
3. AbstractJobListener Job监听，可自行实现自己需要的Job监听
4. AbstractTriggerListener Trigger监听，可自行实现自己需要的Trigger监听

#### 版权说明
quartz使用 [Apache License 2.0](https://gitee.com/xbd521/quartz-boot/blob/master/LICENSE "Apache License 2.0") 协议



