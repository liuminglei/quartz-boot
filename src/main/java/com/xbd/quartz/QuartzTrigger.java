package com.xbd.quartz;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

/**
 * 定时任务触发器
 *
 * @author luas
 * @since 2.0
 */
public class QuartzTrigger implements Serializable {

    private static final long serialVersionUID = 446936256465096569L;

    /**
     * 定时任务触发器类别
     */
    public enum TriggerType {

        /**
         * SimpleTrigger
         */
        SIMPLE,

        /**
         * CronTrigger
         */
        CRON

    }

    private TriggerType type = TriggerType.SIMPLE;

    private JobKey jobKey;

    private TriggerKey originalKey;

    private TriggerKey key;

    private String description;

    private String calendarName;

    private Date startAt;

    private boolean startNow;

    private Date endAt;

    private Map<String, Object> jobData;

    /**
     * 重复次数，如永久重复，则设置该值为{@link org.quartz.SimpleTrigger#REPEAT_INDEFINITELY}
     */
    private int repeatCount;

    /**
     * 重复时间间隔，单位：毫秒
     */
    private long repeatInterval;

    private String cronExpression;

    /**
     * 任务错过触发时间执行策略
     * <p>
     * 通用策略
     * </p>
     * <ul>
     *     <li>
     *         MISFIRE_INSTRUCTION_SMART_POLICY<br>
     *             Quartz框架自动选择适合的策略
     *     </li>
     *     <li>
     *         MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY<br>
     *         以错过的第一个频率时间立刻开始执行，重做错过的所有频率周期后，当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
     *     </li>
     * </ul>
     * <p>
     * SimpleTrigger对应的策略
     * </p>
     * <ul>
     *    <li>
     *        MISFIRE_INSTRUCTION_FIRE_NOW<br>
     *        立即触发调度，且忽略已经MisFire的任务。此策略只适用于只执行一次的Trigger；如repeat count {@code >} 0，则等同于策略{@link org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT}
     *    </li>
     *    <li>
     *        MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT<br>
     *        将startTime设置当前时间，重新调度该任务，包括已经MissFire的任务，如果当前时间已经晚于 endTime，那么这个触发器将不会在被触发。
     *    </li>
     *    <li>
     *        MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT <br>
     *        与{@code MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT}策略一样，区别就是会忽略已经MissFire的任务
     *    </li>
     *    <li>
     *        MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT<br>
     *            同{@code MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT}，区别在于会忽略已经MissFire的任务，也就是说本来这个任务应该执行10次，但是已经错过了3次，那么这个任务就还会执行7次。
     *    </li>
     *    <li>
     *        MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT<br>
     *        在下一次调度时间点，重新调度该任务，包括已经MissFire的任务
     *    </li>
     * </ul>
     * <p>
     * CronTrigger对应的策略
     * </p>
     * <ul>
     *    <li>
     *        MISFIRE_INSTRUCTION_DO_NOTHING<br>
     *            不触发立即执行，等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
     *    </li>
     *    <li>
     *        MISFIRE_INSTRUCTION_FIRE_ONCE_NOW<br>
     *        以当前时间为触发频率立刻触发一次执行，然后按照Cron频率依次执行
     *    </li>
     * </ul>
     *
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_FIRE_NOW
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT
     *
     * @see org.quartz.CronTrigger#MISFIRE_INSTRUCTION_DO_NOTHING
     * @see org.quartz.CronTrigger#MISFIRE_INSTRUCTION_FIRE_ONCE_NOW
     */
    private int misfireInstruction = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;

    public TriggerType getType() {
        return type;
    }

    public void setType(TriggerType type) {
        this.type = type;
    }

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public TriggerKey getOriginalKey() {
        return originalKey;
    }

    public void setOriginalKey(TriggerKey originalKey) {
        this.originalKey = originalKey;
    }

    public TriggerKey getKey() {
        return key;
    }

    public void setKey(TriggerKey key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public boolean isStartNow() {
        return startNow;
    }

    public void setStartNow(boolean startNow) {
        this.startNow = startNow;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Map<String, Object> getJobData() {
        return jobData;
    }

    public void setJobData(Map<String, Object> jobData) {
        this.jobData = jobData;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(int misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }
}
