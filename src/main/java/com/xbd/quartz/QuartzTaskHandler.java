package com.xbd.quartz;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Calendar;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * 定时任务控制器
 *
 * @author luas
 * @since 1.0
 */
public abstract class QuartzTaskHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull
    protected Scheduler scheduler;

    /**
     * 动态添加任务
     *
     * @see QuartzTask
     * @param quartzTask 定时任务信息
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void addTask(QuartzTask quartzTask) throws SchedulerException;

    /**
     * 批量动态添加任务
     *
     * @see QuartzTask
     * @param quartzTasks 定时任务信息集合
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void addTask(QuartzTask... quartzTasks) throws SchedulerException;

    /**
     * 动态更新任务
     *
     * @see QuartzTask
     * @param quartzTask 定时任务信息
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void updateTask(QuartzTask quartzTask) throws SchedulerException;

    /**
     * 批量动态更新任务
     *
     * @see QuartzTask
     * @param quartzTasks 定时任务信息集合
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void updateTask(QuartzTask... quartzTasks) throws SchedulerException;

    /**
     * 暂停任务
     *
     * @see QuartzTask
     *
     * @param quartzTask 定时任务信息
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void pauseTask(QuartzTask quartzTask) throws SchedulerException;

    /**
     * 批量暂停任务
     *
     * @see QuartzTask
     * @param quartzTasks 定时任务信息集合
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void pauseTask(QuartzTask... quartzTasks) throws SchedulerException;

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void pauseTask() throws SchedulerException;

    /**
     * 继续任务
     *
     * @see QuartzTask
     * @param quartzTask 定时任务信息
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void resumeTask(QuartzTask quartzTask) throws SchedulerException;

    /**
     * 批量继续任务
     *
     * @see QuartzTask
     * @param quartzTasks 定时任务信息集合
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void resumeTask(QuartzTask... quartzTasks) throws SchedulerException;

    /**
     * 删除任务
     *
     * @see QuartzTask
     * @param quartzTask 定时任务信息
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void deleteTask(QuartzTask quartzTask) throws SchedulerException;

    /**
     * 批量删除任务
     *
     * @see QuartzTask
     * @param quartzTasks 定时任务信息集合
     * @throws SchedulerException
     * @since 1.0
     */
    @Deprecated
    public abstract void deleteTask(QuartzTask... quartzTasks) throws SchedulerException;

    /**
     * 根据定时任务信息组织CronScheduleBuilder
     *
     * @see CronScheduleBuilder
     * @see QuartzTask
     * @param quartzTask 定时任务信息
     * @since 1.0
     */
    @Deprecated
    public CronScheduleBuilder initCronScheduleBuilder(QuartzTask quartzTask) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTask.getCronExpression());

        // 以当前时间为触发频率立刻触发一次执行，然后按照Cron频率依次执行
        if (quartzTask.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW) {
            cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else if (quartzTask.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY) {
            // 以错过的第一个频率时间立刻开始执行，重做错过的所有频率周期后，当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
            cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (quartzTask.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING) {
            // 不触发立即执行，等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
            cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        }

        return cronScheduleBuilder;
    }

    /**
     * 动态添加任务
     * <p>
     *     将声明的{@code QuartzJob}添加到{@code Scheduler}中
     * </p>
     *
     * @param quartzJob 定时任务信息
     *
     * @since 2.0
     * @see QuartzJob 定时任务信息
     *
     * @throws SchedulerException 如果声明的定时任务不能被添加到{@code Scheduler}中，或者{@code Scheduler}内部异常
     */
    public abstract void addJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 动态添加任务
     * <p>
     *     将声明的{@code QuartzJob}添加到{@code Scheduler}中
     * </p>
     *
     * @param quartzJobs 定时任务信息列表
     *
     * @since 2.0
     * @see QuartzJob 定时任务信息
     * @see #addJob(QuartzJob)
     *
     * @throws SchedulerException 如果声明的定时任务不能被添加到{@code Scheduler}中，或者{@code Scheduler}内部异常
     */
    public abstract void addJob(QuartzJob... quartzJobs) throws SchedulerException;

    /**
     * 动态添加任务
     * <p>
     *     将声明的{@code QuartzJob}更新到{@code Scheduler}中，任务可以是正在运行的状态，也没问题
     * </p>
     *
     * @param quartzJob 定时任务信息
     *
     * @since 2.0
     * @see QuartzJob 定时任务信息
     * @see #addJob(QuartzJob)
     *
     * @throws SchedulerException 如果声明的定时任务不能被更新到{@code Scheduler}中，或者{@code Scheduler}内部异常
     */
    public abstract void updateJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 动态添加任务
     * <p>
     *     将声明的{@code QuartzJob}更新到{@code Scheduler}中，任务可以是正在运行的状态，也没问题
     * </p>
     *
     * @param quartzJobs 定时任务信息列表
     *
     * @since 2.0
     * @see QuartzJob 定时任务信息
     * @see #updateJob(QuartzJob)
     *
     * @throws SchedulerException 如果声明的定时任务不能被更新到{@code Scheduler}中，或者{@code Scheduler}内部异常
     */
    public abstract void updateJob(QuartzJob... quartzJobs) throws SchedulerException;

    public abstract void saveJob(QuartzJob quartzJob) throws SchedulerException;

    public abstract void saveJob(QuartzJob... quartzJobs) throws SchedulerException;

    /**
     * 暂停声明的{@code QuartzJob}任务
     *
     * @since 2.0
     * @see QuartzJob
     *
     * @param quartzJob 定时任务信息
     * @throws SchedulerException 如果声明的定时任务不能被暂停，或者{@code Scheduler}内部异常
     */
    public abstract void pauseJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 暂停声明的{@code QuartzJob}任务
     *
     * @since 2.0
     * @see QuartzJob
     * @see #pauseJob(QuartzJob)
     *
     * @param quartzJobs 定时任务信息列表
     * @throws SchedulerException 如果声明的定时任务不能被暂停，或者{@code Scheduler}内部异常
     */
    public abstract void pauseJob(QuartzJob... quartzJobs) throws SchedulerException;

    /**
     * 暂停声明的{@code JobKey}任务
     *
     * @since 2.0
     * @see JobKey
     *
     * @param jobKey 定时任务Key标识
     * @throws SchedulerException 如果声明的定时任务不能被暂停，或者{@code Scheduler}内部异常
     */
    public abstract void pauseJob(JobKey jobKey) throws SchedulerException;

    /**
     * 暂停声明的{@code JobKey}任务
     *
     * @since 2.0
     * @see JobKey
     *
     * @param jobKeys 定时任务Key标识列表
     * @throws SchedulerException 如果声明的定时任务不能被暂停，或者{@code Scheduler}内部异常
     */
    public abstract void pauseJob(List<JobKey> jobKeys) throws SchedulerException;

    /**
     * 暂停满足Group条件的任务
     *
     * @since 2.0
     * @see GroupMatcher<JobKey>
     *
     * @param matcher Group匹配条件
     * @throws SchedulerException 如果声明的定时任务不能被暂停，或者{@code Scheduler}内部异常
     */
    public abstract void pauseJob(GroupMatcher<JobKey> matcher) throws SchedulerException;

    /**
     * 继续任务
     *
     * <p>将已暂停的任务恢复运行状态</p>
     *
     * @since 2.0
     * @see QuartzJob
     *
     * @param quartzJob 定时任务信息
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void resumeJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 继续任务
     *
     * <p>将已暂停的任务恢复运行状态</p>
     *
     * @since 2.0
     * @see QuartzJob
     * @see #resumeJob(QuartzJob)
     *
     * @param quartzJobs 定时任务信息列表
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void resumeJob(QuartzJob... quartzJobs) throws SchedulerException;

    /**
     * 继续任务
     *
     * <p>将已暂停的任务恢复运行状态</p>
     *
     * @since 2.0
     * @see QuartzJob
     *
     * @param jobKey 定时任务Key
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void resumeJob(JobKey jobKey) throws SchedulerException;

    /**
     * 继续任务
     *
     * <p>将已暂停的任务恢复运行状态</p>
     *
     * @since 2.0
     * @see QuartzJob
     *
     * @param jobKeys 定时任务Key列表
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void resumeJob(List<JobKey> jobKeys) throws SchedulerException;

    /**
     * 继续任务
     *
     * <p>将已暂停的任务恢复运行状态</p>
     *
     * @since 2.0
     * @see GroupMatcher<JobKey>
     *
     * @param matcher Group匹配条件
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void resumeJob(GroupMatcher<JobKey> matcher) throws SchedulerException;

    /**
     * 触发任务
     *
     * <p>立即触发一次任务</p>
     *
     * @since 2.0
     *
     * @param quartzTrigger 任务Trigger
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void triggerJob(QuartzTrigger quartzTrigger) throws SchedulerException;

    /**
     * 触发任务
     *
     * <p>立即触发一次任务</p>
     *
     * @since 2.0
     *
     * @param quartzTriggers 任务Trigger列表
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void triggerJob(QuartzTrigger... quartzTriggers) throws SchedulerException;

    /**
     * 触发任务
     *
     * <p>立即触发一次任务</p>
     *
     * @since 2.0
     *
     * @param jobKey 任务key
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void triggerJob(JobKey jobKey) throws SchedulerException;

    /**
     * 触发任务
     *
     * <p>立即触发一次任务</p>
     *
     * @since 2.0
     *
     * @param jobKey 任务key
     * @param jobDataMap 任务Trigger的JobData
     * @throws SchedulerException 如果定时任务不能恢复，或者{@code Scheduler}内部异常
     */
    public abstract void triggerJob(JobKey jobKey, JobDataMap jobDataMap) throws SchedulerException;

    /**
     * 删除任务
     *
     * <p>
     *     将声明的{@code QuartzJob}从{@code Scheduler}中删除
     * </p>
     *
     * @since 2.0
     * @see QuartzJob
     *
     * @param quartzJob 定时任务信息
     * @throws SchedulerException 如果定时任务不能被删除，或者{@code Scheduler}内部异常
     */
    public abstract void deleteJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 删除任务
     *
     * <p>
     *     将声明的{@code QuartzJob}从{@code Scheduler}中删除
     * </p>
     *
     * @since 2.0
     * @see QuartzJob
     * @see #deleteJob(QuartzJob)
     *
     * @param quartzJobs 定时任务信息列表
     * @throws SchedulerException 如果定时任务不能被删除，或者{@code Scheduler}内部异常
     */
    public abstract void deleteJob(QuartzJob... quartzJobs) throws SchedulerException;

    /**
     * 删除任务
     *
     * <p>
     *     将声明的{@code QuartzJob}从{@code Scheduler}中删除
     * </p>
     *
     * @since 2.0
     * @see QuartzJob
     *
     * @param jobKey 定时任务Key
     * @throws SchedulerException 如果定时任务不能被删除，或者{@code Scheduler}内部异常
     */
    public abstract void deleteJob(JobKey jobKey) throws SchedulerException;

    /**
     * 删除任务
     *
     * <p>
     *     将声明的{@code QuartzJob}从{@code Scheduler}中删除
     * </p>
     *
     * @since 2.0
     * @see QuartzJob
     *
     * @param jobKeys 定时任务Key列表
     * @throws SchedulerException 如果定时任务不能被删除，或者{@code Scheduler}内部异常
     */
    public abstract void deleteJob(List<JobKey> jobKeys) throws SchedulerException;

    /**
     * 添加任务Trigger
     *
     * @param quartzTrigger 定时任务Trigger
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void addTrigger(QuartzTrigger quartzTrigger) throws SchedulerException;

    /**
     * 添加任务Trigger
     *
     * @param quartzTriggers 定时任务Trigger列表
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void addTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException;

    /**
     * 更新任务Trigger
     *
     * @param quartzTrigger 定时任务Trigger
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void updateTrigger(QuartzTrigger quartzTrigger) throws SchedulerException;

    /**
     * 更新任务Trigger
     *
     * @param quartzTriggers 定时任务Trigger列表
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void updateTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException;

    /**
     * 暂停任务Trigger
     *
     * @param quartzTrigger 定时任务Trigger
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void pauseTrigger(QuartzTrigger quartzTrigger) throws SchedulerException;

    /**
     * 暂停任务Trigger
     *
     * @param quartzTriggers 定时任务Trigger列表
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void pauseTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException;

    /**
     * 暂停任务Trigger
     *
     * @param triggerKey 定时任务TriggerKey
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void pauseTrigger(TriggerKey triggerKey) throws SchedulerException;

    /**
     * 暂停任务Trigger
     *
     * @param triggerKeys 定时任务TriggerKey列表
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void pauseTrigger(List<TriggerKey> triggerKeys) throws SchedulerException;

    /**
     * 暂停任务Trigger
     *
     * @param matcher 定时任务TriggerKey匹配条件
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void pauseTrigger(GroupMatcher<TriggerKey> matcher) throws SchedulerException;

    /**
     * 暂停任务Trigger
     * <p>
     *     将{@code Scheduler}中的所有任务暂停
     * </p>
     *
     * @see QuartzJob 定时任务信息
     *
     * @throws SchedulerException 如果定时任务不能被暂停，或者{@code Scheduler}内部异常
     */
    public abstract void pauseAll() throws SchedulerException;

    /**
     * 重启任务Trigger
     *
     * @param quartzTrigger 定时任务Trigger
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void resumeTrigger(QuartzTrigger quartzTrigger) throws SchedulerException;

    /**
     * 重启任务Trigger
     *
     * @param quartzTriggers 定时任务Trigger列表
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void resumeTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException;

    /**
     * 重启任务Trigger
     *
     * @param triggerKey 定时任务TriggerKey
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void resumeTrigger(TriggerKey triggerKey) throws SchedulerException;

    /**
     * 重启任务Trigger
     *
     * @param triggerKeys 定时任务TriggerKey列表
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void resumeTrigger(List<TriggerKey> triggerKeys) throws SchedulerException;

    /**
     * 重启任务Trigger
     *
     * @param matcher 定时任务TriggerKey匹配条件
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void resumeTrigger(GroupMatcher<TriggerKey> matcher) throws SchedulerException;

    /**
     * 删除任务Trigger
     *
     * @param quartzTrigger 定时任务Trigger
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void deleteTrigger(QuartzTrigger quartzTrigger) throws SchedulerException;

    /**
     * 删除任务Trigger
     *
     * @param quartzTriggers 定时任务Trigger列表
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void deleteTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException;

    /**
     * 删除任务Trigger
     *
     * @param triggerKey 定时任务TriggerKey
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void deleteTrigger(TriggerKey triggerKey) throws SchedulerException;

    /**
     * 删除任务Trigger
     *
     * @param triggerKeys 定时任务TriggerKey列表
     *
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public abstract void deleteTrigger(List<TriggerKey> triggerKeys) throws SchedulerException;

    /**
     * 创建JobDetail
     *
     * @param quartzJob {@link QuartzJob}
     * @return {@link JobDetail}
     * @since 2.0
     * @throws SchedulerException {@link QuartzJob}属性错误、{@code Scheduler}内部异常
     */
    protected JobDetail createJobDetail(QuartzJob quartzJob) throws SchedulerException {
        JobBuilder jobBuilder = JobBuilder
                .newJob(quartzJob.getJobClass())
                .withIdentity(quartzJob.getKey())
                .withDescription(quartzJob.getDescription())
                .storeDurably();

        if (quartzJob.getJobData() != null) {
            jobBuilder.usingJobData(new JobDataMap(quartzJob.getJobData()));
        }

        return jobBuilder.build();
    }

    /**
     * 创建Trigger
     *
     * @param quartzTrigger {@link QuartzTrigger}
     * @return {@link Trigger}
     * @since 2.0
     * @throws SchedulerException {@link QuartzTrigger}属性错误、{@code Scheduler}内部异常
     */
    protected Trigger createTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(quartzTrigger.getKey())
                .withSchedule(initCronScheduleBuilder(quartzTrigger));

        if (quartzTrigger.getJobKey() != null) {
            triggerBuilder.forJob(quartzTrigger.getJobKey());
        }

        if (quartzTrigger.getJobData() != null) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.putAll(quartzTrigger.getJobData());
            triggerBuilder.usingJobData(jobDataMap);
        }

        if (StringUtils.isNotEmpty(quartzTrigger.getCalendarName())) {
            Calendar calendarInScheduler = this.scheduler.getCalendar(quartzTrigger.getCalendarName());
            if (calendarInScheduler == null) {
                this.logger.error("Calendar{}尚未添加到Scheduler中！");

                throw new SchedulerException("Calendar " + quartzTrigger.getCalendarName() + "尚未添加到Scheduler中！");
            }

            triggerBuilder.modifiedByCalendar(quartzTrigger.getCalendarName());
        }

        if (quartzTrigger.getStartAt() != null) {
            triggerBuilder.startAt(quartzTrigger.getStartAt());
        }

        if (quartzTrigger.isStartNow()) {
            triggerBuilder.startNow();
        }

        if (quartzTrigger.getEndAt() != null) {
            triggerBuilder.endAt(quartzTrigger.getEndAt());
        }

        return triggerBuilder.build();
    }

    /**
     * 创建Trigger
     *
     * @param quartzJob {@link QuartzJob}
     * @return {@link Trigger}集合
     * @since 2.0
     * @throws SchedulerException {@link QuartzTrigger}属性错误、{@code Scheduler}内部异常
     */
    protected Set<Trigger> createTriggers(QuartzJob quartzJob) throws SchedulerException {
        if (quartzJob.getTriggers() == null) {
            return null;
        }

        Set<Trigger> triggers = new HashSet<>();

        for (QuartzTrigger quartzTrigger : quartzJob.getTriggers()) {
            Trigger trigger = createTrigger(quartzTrigger);

            triggers.add(trigger);
        }

        return triggers;
    }

    /**
     * 创建JobDetail及其Trigger集合Map
     *
     * @param quartzJob {@link QuartzJob}
     * @return {@link JobDetail}及其{@link Trigger}集合Map
     * @since 2.0
     * @throws SchedulerException {@link QuartzTrigger}属性错误、{@code Scheduler}内部异常
     */
    protected Map<JobDetail, Set<? extends Trigger>> createTriggersAndJobs(QuartzJob quartzJob) throws SchedulerException {
        if (ObjectUtils.isEmpty(quartzJob) || CollectionUtils.isEmpty(quartzJob.getTriggers())) {
            throw new SchedulerException(ObjectUtils.isEmpty(quartzJob) ? "job为空！" : "job对应的triggers为空！");
        }

        Map<JobDetail, Set<? extends Trigger>> triggersAndJobs = new HashMap<>();

        JobDetail jobDetail = createJobDetail(quartzJob);

        Set<? extends Trigger> triggers = createTriggers(quartzJob);

        triggersAndJobs.put(jobDetail, triggers);

        return triggersAndJobs;
    }

    /**
     * 创建JobDetail及其Trigger集合Map
     *
     * @param quartzJobs {@link QuartzJob}
     * @return {@link JobDetail}及其{@link Trigger}集合Map
     * @since 2.0
     * @throws SchedulerException {@link QuartzTrigger}属性错误、{@code Scheduler}内部异常
     */
    protected Map<JobDetail, Set<? extends Trigger>> createTriggersAndJobs(QuartzJob... quartzJobs) throws SchedulerException {
        if (ObjectUtils.isEmpty(quartzJobs)) {
            throw new SchedulerException("jobs为空！");
        }

        Map<JobDetail, Set<? extends Trigger>> triggersAndJobs = new HashMap<>();

        for (QuartzJob quartzJob : quartzJobs) {
            triggersAndJobs.putAll(createTriggersAndJobs(quartzJob));
        }

        return triggersAndJobs;
    }

    /**
     * 根据定时任务信息组织CronScheduleBuilder
     *
     * @see CronScheduleBuilder
     * @see QuartzJob
     * @see CronScheduleBuilder
     *
     * @param quartzTrigger 定时任务信息
     * @return CronScheduleBuilder 基于Cron表达式的{@link ScheduleBuilder}
     */
    protected ScheduleBuilder initCronScheduleBuilder(QuartzTrigger quartzTrigger) {
        ScheduleBuilder scheduleBuilder;
        if (QuartzTrigger.TriggerType.SIMPLE.equals(quartzTrigger.getType())) {
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(quartzTrigger.getRepeatInterval());

            if (quartzTrigger.getRepeatCount() == SimpleTrigger.REPEAT_INDEFINITELY) {
                simpleScheduleBuilder.repeatForever();
            } else {
                simpleScheduleBuilder.withRepeatCount(quartzTrigger.getRepeatCount());
            }

            if (quartzTrigger.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW) {
                simpleScheduleBuilder.withMisfireHandlingInstructionFireNow();
            } else if (quartzTrigger.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY) {
                simpleScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            } else if (quartzTrigger.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT) {
                simpleScheduleBuilder.withMisfireHandlingInstructionNextWithExistingCount();
            } else if (quartzTrigger.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT) {
                simpleScheduleBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
            } else if (quartzTrigger.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT) {
                simpleScheduleBuilder.withMisfireHandlingInstructionNowWithExistingCount();
            } else if (quartzTrigger.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT) {
                simpleScheduleBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
            }

            scheduleBuilder = simpleScheduleBuilder;
        } else {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTrigger.getCronExpression());

            // 以当前时间为触发频率立刻触发一次执行，然后按照Cron频率依次执行
            if (quartzTrigger.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW) {
                cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
            } else if (quartzTrigger.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY) {
                // 以错过的第一个频率时间立刻开始执行，重做错过的所有频率周期后，当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
                cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            } else if (quartzTrigger.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING) {
                // 不触发立即执行，等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
                cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            }

            scheduleBuilder = cronScheduleBuilder;
        }

        return scheduleBuilder;
    }

    /**
     * 根据任务名称、任务分组获取定时任务调度器中的该任务触发器
     *
     * @param name 触发器名称
     * @param group 触发器分组
     * @return CronTrigger 基于Cron表达式的定时任务触发器
     * @throws SchedulerException 名称、分组为空、触发器不存在、或者{@code Scheduler}内部异常
     */
    public Trigger getTrigger(String name, String group) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(new TriggerKey(name, group));

        return trigger;
    }

    /**
     * 根据任务Key获取定时任务调度器中的该任务触发器
     *
     * @param triggerKey 触发器Key
     * @return CronTrigger 基于Cron表达式的定时任务触发器
     * @since 2.0
     * @throws SchedulerException 名称、分组为空、触发器不存在、或者{@code Scheduler}内部异常
     */
    public Trigger getTrigger(TriggerKey triggerKey) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(triggerKey);

        return trigger;
    }

    /**
     * Get the current state of the identified <code>{@link Trigger}</code>.
     *
     * @since 2.0
     * @see Trigger.TriggerState
     */
    public Trigger.TriggerState getTriggerState(String name, String group)
            throws SchedulerException {
        return getTriggerState(new TriggerKey(name, group));
    }

    /**
     * Get the current state of the identified <code>{@link Trigger}</code>.
     *
     * @since 2.0
     * @see Trigger.TriggerState
     */
    public Trigger.TriggerState getTriggerState(QuartzTrigger quartzTrigger)
            throws SchedulerException {
        return getTriggerState(quartzTrigger.getKey());
    }

    /**
     * Get the current state of the identified <code>{@link Trigger}</code>.
     *
     * @since 2.0
     * @see Trigger.TriggerState
     */
    public Trigger.TriggerState getTriggerState(TriggerKey triggerKey)
            throws SchedulerException {
        Assert.notNull(triggerKey, "trigger不能为空！");
        Assert.notNull(triggerKey.getName(), "trigger名称不能为空！");

        return this.scheduler.getTriggerState(triggerKey);
    }

    /**
     * Add (register) the given <code>Calendar</code> to the Scheduler.
     *
     * @param updateTriggers whether or not to update existing triggers that
     * referenced the already existing calendar so that they are 'correct'
     * based on the new trigger.
     *
     * @since 2.0
     * @throws SchedulerException
     *           if there is an internal Scheduler error, or a Calendar with
     *           the same name already exists, and <code>replace</code> is
     *           <code>false</code>.
     */
    public void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers)
            throws SchedulerException {
        this.scheduler.addCalendar(calName, calendar, replace, updateTriggers);
    }

    /**
     * Delete the identified <code>Calendar</code> from the Scheduler.
     *
     * <p>
     * If removal of the <code>Calendar</code> would result in
     * <code>Trigger</code>s pointing to non-existent calendars, then a
     * <code>SchedulerException</code> will be thrown.
     * </p>
     *
     * @return true if the Calendar was found and deleted.
     * @since 2.0
     * @throws SchedulerException
     *           if there is an internal Scheduler error, or one or more
     *           triggers reference the calendar
     */
    public boolean deleteCalendar(String calName) throws SchedulerException {
        return this.scheduler.deleteCalendar(calName);
    }

    /**
     * Get the <code>{@link Calendar}</code> instance with the given name.
     *
     * @since 2.0
     */
    public Calendar getCalendar(String calName) throws SchedulerException {
        return this.scheduler.getCalendar(calName);
    }

    /**
     * Get the names of all registered <code>{@link Calendar}s</code>.
     *
     * @since 2.0
     */
    public List<String> getCalendarNames() throws SchedulerException {
        return this.scheduler.getCalendarNames();
    }

    /**
     * 获取当前<code>Scheduler</code>instance Id
     *
     * @return 当前<code>Scheduler</code>的instance Id
     * @since 2.0
     * @throws SchedulerException {@code Scheduler}内部异常
     */
    public String getSchedulerInstanceId() throws SchedulerException {
        return scheduler.getSchedulerInstanceId();
    }

    /**
     * Determine whether a {@link Job} with the given identifier already
     * exists within the scheduler.
     *
     * @param jobKey the identifier to check for
     * @return true if a Job exists with the given identifier
     * @since 2.0
     * @throws SchedulerException 内部错误
     */
    public boolean checkExists(JobKey jobKey) throws SchedulerException {
        return this.scheduler.checkExists(jobKey);
    }

    /**
     * Determine whether a {@link Trigger} with the given identifier already
     * exists within the scheduler.
     *
     * @param triggerKey the identifier to check for
     * @return true if a Trigger exists with the given identifier
     * @since 2.0
     * @throws SchedulerException 内部错误
     */
    public boolean checkExists(TriggerKey triggerKey) throws SchedulerException {
        return this.scheduler.checkExists(triggerKey);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
