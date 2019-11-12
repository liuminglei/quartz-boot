package com.xbd.quartz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.utils.Key;

/**
 * {@code QuartzJob} to {@link org.quartz.JobDetail}
 * @author luas
 * @since 2.0
 */
public class QuartzJobBuilder {

    private JobKey key;

    private String description;

    private Class<? extends AbstractQuartzJobBean> jobClass;

    private Map<String, Object> jobData = new HashMap<>();

    private Set<QuartzTrigger> triggers = new HashSet<>();

    private QuartzJobBuilder() {

    }

    public static QuartzJobBuilder newJob() {
        return new QuartzJobBuilder();
    }

    public static QuartzJobBuilder newJob(Class<? extends AbstractQuartzJobBean> jobClass) {
        QuartzJobBuilder builder = new QuartzJobBuilder();
        builder.forJob(jobClass);
        return builder;
    }

    public QuartzJob build() {

        QuartzJob job = new QuartzJob();

        job.setJobClass(jobClass);
        job.setDescription(description);

        if (key == null) {
            key = new JobKey(Key.createUniqueName(null), null);
        }

        job.setKey(key);

        if(!jobData.isEmpty()) {
            job.setJobData(jobData);
        }

        if (!this.triggers.isEmpty()) {
            job.setTriggers(this.triggers);
        }

        return job;
    }

    public QuartzJobBuilder withIdentity(JobKey jobKey) {
        this.key = jobKey;
        return this;
    }

    public QuartzJobBuilder withIdentity(String name) {
        key = new JobKey(name, null);
        return this;
    }

    public QuartzJobBuilder withIdentity(String name, String group) {
        key = new JobKey(name, group);
        return this;
    }

    public QuartzJobBuilder withDescription(String jobDescription) {
        this.description = jobDescription;
        return this;
    }

    public QuartzJobBuilder forJob(Class<? extends AbstractQuartzJobBean> jobClazz) {
        this.jobClass = jobClazz;
        return this;
    }

    public QuartzJobBuilder setTrigger(Set<QuartzTrigger> newTriggers) {
        this.triggers.forEach(newTriggers::add);
        this.triggers = newTriggers;
        return this;
    }

    public QuartzJobBuilder withTrigger(QuartzTrigger trigger) {
        this.triggers.add(trigger);
        return this;
    }

    public QuartzJobBuilder setJobData(Map<String, Object> newJobData) {
        this.jobData.forEach(newJobData::put);
        this.jobData = newJobData;
        return this;
    }

    public QuartzJobBuilder usingJobData(String key, String value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzJobBuilder usingJobData(String key, Integer value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzJobBuilder usingJobData(String key, Long value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzJobBuilder usingJobData(String key, Double value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzJobBuilder usingJobData(String key, Float value) {
        this.jobData.put(key, value);
        return this;
    }

    public QuartzJobBuilder usingJobData(String key, Boolean value) {
        this.jobData.put(key, value);
        return this;
    }

}
