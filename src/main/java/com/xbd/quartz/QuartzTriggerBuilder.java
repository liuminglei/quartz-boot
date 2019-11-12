package com.xbd.quartz;

import org.quartz.*;
import org.quartz.utils.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code QuartzTrigger} to {@link Trigger}
 * @author luas
 * @since 2.0
 */
public class QuartzTriggerBuilder {

    private QuartzTrigger.TriggerType type = QuartzTrigger.TriggerType.SIMPLE;

    private JobKey jobKey;

    private TriggerKey originalKey;

    private TriggerKey key;

    private String description;

    private Map<String, Object> jobData = new HashMap<>();

    private String calendarName;

    /**
     * 重复次数，如永久重复，则设置该值为{@link SimpleTrigger#REPEAT_INDEFINITELY}
     */
    private int repeatCount;

    /**
     * 重复时间间隔，单位：毫秒
     */
    private long interval;

    private String cronExpression;

    private int misfireInstruction = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;

    private Date startAt;

    private boolean startNow;

    private Date endAt;

    private QuartzTriggerBuilder() {

    }

    public static QuartzTriggerBuilder newTrigger() {
        return new QuartzTriggerBuilder();
    }

    public QuartzTrigger build() {
        QuartzTrigger quartzTrigger = new QuartzTrigger();

        quartzTrigger.setType(this.type);
        quartzTrigger.setJobKey(this.jobKey);
        quartzTrigger.setOriginalKey(this.originalKey);

        if (key == null) {
            key = new TriggerKey(Key.createUniqueName(null), null);
        }

        quartzTrigger.setKey(this.key);

        quartzTrigger.setDescription(this.description);
        quartzTrigger.setCalendarName(this.calendarName);
        quartzTrigger.setStartAt(this.startAt);
        quartzTrigger.setStartNow(this.startNow);
        quartzTrigger.setEndAt(this.endAt);

        if (!this.jobData.isEmpty()) {
            quartzTrigger.setJobData(this.jobData);
        }

        quartzTrigger.setRepeatCount(this.repeatCount);
        quartzTrigger.setRepeatInterval(this.interval);
        quartzTrigger.setCronExpression(this.cronExpression);
        quartzTrigger.setMisfireInstruction(this.misfireInstruction);

        return quartzTrigger;
    }

    public QuartzTriggerBuilder ofType(QuartzTrigger.TriggerType type) {
        this.type = type;
        return this;
    }

    public QuartzTriggerBuilder forJob(JobKey triggerOfJobKey) {
        this.jobKey = triggerOfJobKey;
        return this;
    }

    public QuartzTriggerBuilder forJob(String jobName) {
        this.jobKey = new JobKey(jobName, null);
        return this;
    }

    public QuartzTriggerBuilder forJob(String jobName, String jobGroup) {
        this.jobKey = new JobKey(jobName, jobGroup);
        return this;
    }

    public QuartzTriggerBuilder withOriginalIdentity(TriggerKey originalTriggerKey) {
        this.originalKey = originalTriggerKey;
        return this;
    }

    public QuartzTriggerBuilder withOriginalIdentity(String originalName) {
        this.originalKey = new TriggerKey(originalName, null);
        return this;
    }

    public QuartzTriggerBuilder withOriginalIdentity(String originalName, String originalGroup) {
        this.originalKey = new TriggerKey(originalName, originalGroup);
        return this;
    }

    public QuartzTriggerBuilder withIdentity(TriggerKey triggerKey) {
        this.key = triggerKey;
        return this;
    }

    public QuartzTriggerBuilder withIdentity(String name) {
        this.key = new TriggerKey(name, null);
        return this;
    }

    public QuartzTriggerBuilder withIdentity(String name, String group) {
        this.key = new TriggerKey(name, group);
        return this;
    }

    public QuartzTriggerBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public QuartzTriggerBuilder withCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }

    public QuartzTriggerBuilder withRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    public QuartzTriggerBuilder repeatForever() {
        this.repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
        return this;
    }

    public QuartzTriggerBuilder withIntervalInSeconds(long intervalInSeconds) {
        this.interval = intervalInSeconds;
        return this;
    }

    public QuartzTriggerBuilder withIntervalInMinutes(int intervalInMinutes) {
        this.interval = intervalInMinutes * DateBuilder.MILLISECONDS_IN_MINUTE;
        return this;
    }

    public QuartzTriggerBuilder withIntervalInHours(int intervalInHours) {
        this.interval = intervalInHours * DateBuilder.MILLISECONDS_IN_HOUR;
        return this;
    }

    public QuartzTriggerBuilder withCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public QuartzTriggerBuilder withMisfireInstruction(int misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
        return this;
    }

    public QuartzTriggerBuilder startAt(Date startAt) {
        this.startAt = startAt;
        return this;
    }

    public QuartzTriggerBuilder startNow(boolean startNow) {
        this.startNow = startNow;
        return this;
    }

    public QuartzTriggerBuilder endAt(Date endAt) {
        this.endAt = endAt;
        return this;
    }

    public QuartzTriggerBuilder setJobData(Map<String, Object> newJobData) {
        this.jobData.forEach(newJobData::put);
        this.jobData = newJobData;
        return this;
    }

    public QuartzTriggerBuilder usingJobData(String key, String value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzTriggerBuilder usingJobData(String key, Integer value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzTriggerBuilder usingJobData(String key, Long value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzTriggerBuilder usingJobData(String key, Double value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzTriggerBuilder usingJobData(String key, Float value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzTriggerBuilder usingJobData(String key, Boolean value) {
        this.jobData.put(key, value);
        return this;
    }

}
