package com.xbd.quartz.handler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.xbd.quartz.QuartzJob;
import com.xbd.quartz.QuartzTaskHandler;
import com.xbd.quartz.QuartzTrigger;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * <code>QuartzTaskHandler</code>的默认实现
 *
 * <pre>
 *     SpringBoot项目
 *     {@code @Bean}
 *      public DefaultQuartzTaskHandler defaultQuartzTaskHandler() {
 *          DefaultQuartzTaskHandler defaultQuartzTaskHandler = new DefaultQuartzTaskHandler();
 *          defaultQuartzTaskHandler.setScheduler(scheduler);
 *          return defaultQuartzTaskHandler;
 *      }
 *
 *     同样也可以用在SpringMVC项目中：
 *    {@code <bean id="defaultQuartzTaskHandler" class="cn.com.trade365.framework.quartz.handler.DefaultQuartzTaskHandler">}
 *         {@code <property name="scheduler" ref="scheduler"></property>}
 *    {@code </bean>}
 * </pre>
 *
 * @author 刘明磊
 * @since 1.0
 */
public class DefaultQuartzTaskHandler extends QuartzTaskHandler {

    /**
     * 动态添加任务
     */
    public void addJob(QuartzJob quartzJob) throws SchedulerException {
        Assert.notNull(quartzJob, "job不能为空！");
        Assert.notNull(quartzJob.getJobClass(), "job class不能为空！");
        Assert.notNull(quartzJob.getKey(), "Key不能为空！");
        Assert.notNull(quartzJob.getKey().getName(), "Key名称不能为空！");

        JobDetail jobDetail = createJobDetail(quartzJob);

        Set<? extends Trigger> triggers = createTriggers(quartzJob);

        if (CollectionUtils.isEmpty(triggers)) {
            this.scheduler.addJob(jobDetail, false);
        } else {
            scheduler.scheduleJob(jobDetail, triggers, false);
        }
    }

    /**
     * 批量动态添加任务
     */
    public void addJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "jobs不能为空！");

        for (QuartzJob quartzJob : quartzJobs) {
            addJob(quartzJob);
        }
    }

    /**
     * 动态更新任务
     */
    public void updateJob(QuartzJob quartzJob) throws SchedulerException {
        saveJob(quartzJob);
    }

    /**
     * 批量动态更新任务
     */
    public void updateJob(QuartzJob... quartzJobs) throws SchedulerException {
        saveJob(quartzJobs);
    }

    public void saveJob(QuartzJob quartzJob) throws SchedulerException {
        Assert.notNull(quartzJob, "job不能为空！");
        Assert.notNull(quartzJob.getJobClass(), "job class不能为空！");
        Assert.notNull(quartzJob.getKey(), "Key不能为空！");
        Assert.notNull(quartzJob.getKey().getName(), "Key名称不能为空！");

        JobDetail jobDetail = createJobDetail(quartzJob);

        Set<? extends Trigger> triggers = createTriggers(quartzJob);

        if (CollectionUtils.isEmpty(triggers)) {
            this.scheduler.addJob(jobDetail, true);
        } else {
            scheduler.scheduleJob(jobDetail, triggers, true);
        }
    }

    public void saveJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "jobs不能为空！");

        for (QuartzJob quartzJob : quartzJobs) {
            saveJob(quartzJob);
        }
    }

    /**
     * 暂停任务
     */
    public void pauseJob(QuartzJob quartzJob) throws SchedulerException {
        pauseJob(quartzJob.getKey());
    }

    /**
     * 批量暂停任务
     */
    public void pauseJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "jobs不能为空！");

        List<JobKey> jobKeys = Stream.of(quartzJobs).parallel().map(quartzJob -> quartzJob.getKey()).collect(Collectors.toList());

        pauseJob(jobKeys);
    }

    public void pauseJob(JobKey jobKey) throws SchedulerException {
        Assert.notNull(jobKey, "job不能为空！");
        Assert.notNull(jobKey.getName(), "job名称不能为空！");

        this.scheduler.pauseJob(jobKey);
    }

    public void pauseJob(List<JobKey> jobKeys) throws SchedulerException {
        Assert.notNull(jobKeys, "jobs不能为空！");

        for (JobKey jobKey : jobKeys) {
            pauseJob(jobKey);
        }
    }

    public void pauseJob(GroupMatcher<JobKey> matcher) throws SchedulerException {
        Assert.notNull(matcher, "matcher不能为空！");

        this.scheduler.pauseJobs(matcher);
    }

    /**
     * 继续任务
     */
    public void resumeJob(QuartzJob quartzJob) throws SchedulerException {
        resumeJob(quartzJob.getKey());
    }

    /**
     * 批量继续任务
     */
    public void resumeJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "jobs不能为空！");

        List<JobKey> jobKeys = Stream.of(quartzJobs).parallel().map(quartzJob -> quartzJob.getKey()).collect(Collectors.toList());

        resumeJob(jobKeys);
    }

    public void resumeJob(JobKey jobKey) throws SchedulerException {
        Assert.notNull(jobKey, "job不能为空！");
        Assert.notNull(jobKey.getName(), "job名称不能为空！");

        this.scheduler.resumeJob(jobKey);
    }

    public void resumeJob(List<JobKey> jobKeys) throws SchedulerException {
        Assert.notNull(jobKeys, "jobs不能为空！");

        for (JobKey jobKey : jobKeys) {
            resumeJob(jobKey);
        }
    }

    public void resumeJob(GroupMatcher<JobKey> matcher) throws SchedulerException {
        Assert.notNull(matcher, "matcher不能为空！");

        this.scheduler.resumeJobs(matcher);
    }

    public void triggerJob(QuartzTrigger quartzTrigger) throws SchedulerException {
        Assert.notNull(quartzTrigger, "trigger不能为空！");
        Assert.notNull(quartzTrigger.getJobKey(), "trigger对应的jobKey不能为空！");
        Assert.notNull(quartzTrigger.getJobKey().getName(), "trigger对应的jobKey名称不能为空！");
        Assert.notNull(quartzTrigger.getKey(), "triggerKey不能为空！");
        Assert.notNull(quartzTrigger.getKey().getName(), "triggerKey名称不能为空！");

        JobKey jobKey = quartzTrigger.getJobKey();

        TriggerKey triggerKey = quartzTrigger.getKey();

        JobDataMap jobDataMap = null;

        // 优先使用QuartzTrigger中的JobData
        if (!ObjectUtils.isEmpty(quartzTrigger.getJobData())) {
            jobDataMap = new JobDataMap(quartzTrigger.getJobData());
        } else {
            // 查找该Trigger是否在Scheduler已存在且有JobData
            if (checkExists(triggerKey)) {
                Trigger triggerInScheduler = getTrigger(triggerKey);
                jobDataMap = triggerInScheduler.getJobDataMap();
            }
        }

        triggerJob(jobKey, jobDataMap);
    }

    public void triggerJob(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers不能为空！");

        for (QuartzTrigger quartzTrigger : quartzTriggers) {
            triggerJob(quartzTrigger);
        }
    }

    public void triggerJob(JobKey jobKey) throws SchedulerException {
        triggerJob(jobKey, null);
    }

    public void triggerJob(JobKey jobKey, JobDataMap jobDataMap) throws SchedulerException {
        Assert.notNull(jobKey, "jobKey不能为空！");
        Assert.notNull(jobKey.getName(), "jobKey名称不能为空！");

        this.scheduler.triggerJob(jobKey, jobDataMap);
    }

    /**
     * 删除任务
     */
    public void deleteJob(QuartzJob quartzJob) throws SchedulerException {
        deleteJob(quartzJob.getKey());
    }

    /**
     * 批量删除任务
     */
    public void deleteJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "jobs不能为空！");

        List<JobKey> jobKeys = Stream.of(quartzJobs).parallel().map(quartzJob -> quartzJob.getKey()).collect(Collectors.toList());

        deleteJob(jobKeys);
    }

    public void deleteJob(JobKey jobKey) throws SchedulerException {
        Assert.notNull(jobKey, "jobKey不能为空！");
        Assert.notNull(jobKey.getName(), "jobKey名称不能为空！");

        this.scheduler.deleteJob(jobKey);
    }

    public void deleteJob(List<JobKey> jobKeys) throws SchedulerException {
        Assert.notNull(jobKeys, "jobKeys不能为空！");

        this.scheduler.deleteJobs(jobKeys);
    }

    public void addTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        Assert.notNull(quartzTrigger, "trigger不能为空！");
        Assert.notNull(quartzTrigger.getJobKey(), "trigger对应的jobKey不能为空！");
        Assert.notNull(quartzTrigger.getJobKey().getName(), "trigger对应的jobKey名称不能为空！");
        Assert.notNull(quartzTrigger.getKey(), "triggerKey不能为空！");
        Assert.notNull(quartzTrigger.getKey().getName(), "triggerKey名称不能为空！");

        Trigger trigger = createTrigger(quartzTrigger);

        this.scheduler.scheduleJob(trigger);
    }

    public void addTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers不能为空！");

        for (QuartzTrigger quartzTrigger : quartzTriggers) {
            addTrigger(quartzTrigger);
        }
    }

    public void updateTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        Assert.notNull(quartzTrigger, "trigger不能为空！");
        Assert.notNull(quartzTrigger.getJobKey(), "trigger对应的jobKey不能为空！");
        Assert.notNull(quartzTrigger.getJobKey().getName(), "trigger对应的jobKey名称不能为空！");
        Assert.notNull(quartzTrigger.getOriginalKey(), "原Key不能为空！");
        Assert.notNull(quartzTrigger.getOriginalKey().getName(), "原Key名称不能为空！");
        Assert.notNull(quartzTrigger.getKey(), "Key不能为空！");
        Assert.notNull(quartzTrigger.getKey().getName(), "Key名称不能为空！");

        TriggerKey originalKey = quartzTrigger.getOriginalKey();

        if (!checkExists(originalKey)) {
            throw new SchedulerException("该trigger在scheduler中不存在！");
        } else {
            JobKey jobKeyOriginal = getTrigger(originalKey).getJobKey();
            JobKey jobKey = quartzTrigger.getJobKey();

            if (!jobKeyOriginal.equals(jobKey)) {
                throw new SchedulerException("trigger不能更新其对应的job！");
            }
        }

        Trigger trigger = createTrigger(quartzTrigger);

        this.scheduler.rescheduleJob(originalKey, trigger);
    }

    public void updateTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers不能为空！");

        for (QuartzTrigger quartzTrigger : quartzTriggers) {
            updateTrigger(quartzTrigger);
        }
    }

    public void pauseTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        pauseTrigger(quartzTrigger.getKey());
    }

    public void pauseTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers不能为空！");

        List<TriggerKey> triggerKeys = Stream.of(quartzTriggers).parallel().map(quartzTrigger -> quartzTrigger.getKey()).collect(Collectors.toList());

        pauseTrigger(triggerKeys);
    }

    public void pauseTrigger(TriggerKey triggerKey) throws SchedulerException {
        Assert.notNull(triggerKey, "trigger不能为空！");
        Assert.notNull(triggerKey.getName(), "trigger名称不能为空！");

        this.scheduler.pauseTrigger(triggerKey);
    }

    public void pauseTrigger(List<TriggerKey> triggerKeys) throws SchedulerException {
        Assert.notNull(triggerKeys, "triggers不能为空！");

        for (TriggerKey triggerKey : triggerKeys) {
            pauseTrigger(triggerKey);
        }
    }

    public void pauseTrigger(GroupMatcher<TriggerKey> matcher) throws SchedulerException {
        Assert.notNull(matcher, "matcher不能为空！");

        this.scheduler.pauseTriggers(matcher);
    }

    /**
     * 暂停所有触发器
     */
    public void pauseAll() throws SchedulerException {
        this.scheduler.pauseAll();
    }

    public void resumeTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        resumeTrigger(quartzTrigger.getKey());
    }

    public void resumeTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers不能为空！");

        List<TriggerKey> triggerKeys = Stream.of(quartzTriggers).parallel().map(quartzTrigger -> quartzTrigger.getKey()).collect(Collectors.toList());

        resumeTrigger(triggerKeys);
    }

    public void resumeTrigger(TriggerKey triggerKey) throws SchedulerException {
        Assert.notNull(triggerKey, "trigger不能为空！");
        Assert.notNull(triggerKey.getName(), "trigger名称不能为空！");

        this.scheduler.resumeTrigger(triggerKey);
    }

    public void resumeTrigger(List<TriggerKey> triggerKeys) throws SchedulerException {
        Assert.notNull(triggerKeys, "triggers不能为空！");

        for (TriggerKey triggerKey : triggerKeys) {
            resumeTrigger(triggerKey);
        }
    }

    public void resumeTrigger(GroupMatcher<TriggerKey> matcher) throws SchedulerException {
        Assert.notNull(matcher, "matcher不能为空！");

        this.scheduler.resumeTriggers(matcher);
    }

    public void deleteTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        deleteTrigger(quartzTrigger.getKey());
    }

    public void deleteTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers不能为空！");

        List<TriggerKey> triggerKeys = Stream.of(quartzTriggers).parallel().map(quartzTrigger -> quartzTrigger.getKey()).collect(Collectors.toList());

        deleteTrigger(triggerKeys);
    }

    public void deleteTrigger(TriggerKey triggerKey) throws SchedulerException {
        Assert.notNull(triggerKey, "trigger不能为空！");
        Assert.notNull(triggerKey.getName(), "trigger名称不能为空！");

        this.scheduler.unscheduleJob(triggerKey);
    }

    public void deleteTrigger(List<TriggerKey> triggerKeys) throws SchedulerException {
        Assert.notNull(triggerKeys, "triggers不能为空！");

        this.scheduler.unscheduleJobs(triggerKeys);
    }

}
