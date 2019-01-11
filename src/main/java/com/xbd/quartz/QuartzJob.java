package com.xbd.quartz;

import org.quartz.JobKey;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  定时任务实体，包含任务名称、任务分组、任务描述、任务开始时间、是否立即开始、
 *  任务结束时间、任务目标类、任务目标类SpringBean对象、任务目标类执行方法、cron表达式等属性
 *
 * @author luas
 * @since 2.0
 */
public class QuartzJob implements Serializable {

    private static final long serialVersionUID = -2116518270025568628L;

    private JobKey key;

    private String description;

    private Class<? extends DefaultQuartzJobBean> jobClass;

    private Map<String, Object> jobData;

    private boolean durability = false;

    private boolean shouldRecover = false;

    private Set<QuartzTrigger> triggers;

    public void addTrigger(QuartzTrigger quartzTrigger) {
        if (quartzTrigger != null) {
            if (this.triggers == null) {
                this.triggers = new HashSet<>();
            }

            this.triggers.add(quartzTrigger);
        }
    }

    public JobKey getKey() {
        return key;
    }

    public void setKey(JobKey key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<? extends DefaultQuartzJobBean> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<? extends DefaultQuartzJobBean> jobClass) {
        this.jobClass = jobClass;
    }

    public Map<String, Object> getJobData() {
        return jobData;
    }

    public void setJobData(Map<String, Object> jobData) {
        this.jobData = jobData;
    }

    public boolean isDurability() {
        return durability;
    }

    public void setDurability(boolean durability) {
        this.durability = durability;
    }

    public boolean isShouldRecover() {
        return shouldRecover;
    }

    public void setShouldRecover(boolean shouldRecover) {
        this.shouldRecover = shouldRecover;
    }

    public Set<QuartzTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(Set<QuartzTrigger> triggers) {
        this.triggers = triggers;
    }
}
