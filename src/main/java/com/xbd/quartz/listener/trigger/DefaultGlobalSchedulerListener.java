package com.xbd.quartz.listener.trigger;

import com.xbd.quartz.listener.AbstractSchedulerListener;

import org.quartz.*;

import java.time.LocalDateTime;

/**
 * 默认全局SchedulerListener
 *
 * @author luas
 * @since 4.3
 */
public class DefaultGlobalSchedulerListener extends AbstractSchedulerListener {

	@Override
	public void jobScheduled(Trigger trigger) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 所属 Trigger {} 于 {} 执行调度。", new Object[] {
					trigger.getJobKey(), trigger.getKey(), LocalDateTime.now() });
		}
	}

	@Override
	public void jobUnscheduled(TriggerKey triggerKey) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Trigger {} 未执行调度。", new Object[] { triggerKey });
		}
	}

	@Override
	public void triggerFinalized(Trigger trigger) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 所属 Trigger {} 于 {} 调度期结束，不再调度。", new Object[] {
					trigger.getJobKey(), trigger.getKey(), LocalDateTime.now() });
		}
	}

	@Override
	public void triggerPaused(TriggerKey triggerKey) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Trigger {} 于 {} 暂停调度。",
					new Object[] { triggerKey, LocalDateTime.now() });
		}
	}

	@Override
	public void triggersPaused(String triggerGroup) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("TriggerGroup {} 于 {} 暂停调度。",
					new Object[] { triggerGroup, LocalDateTime.now() });
		}
	}

	@Override
	public void triggerResumed(TriggerKey triggerKey) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Trigger {} 于 {} 恢复调度。",
					new Object[] { triggerKey, LocalDateTime.now() });
		}
	}

	@Override
	public void triggersResumed(String triggerGroup) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("TriggerGroup {} 于 {} 恢复调度。",
					new Object[] { triggerGroup, LocalDateTime.now() });
		}
	}

	@Override
	public void jobAdded(JobDetail jobDetail) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 于 {} 添加到调度器中。",
					new Object[] { jobDetail, LocalDateTime.now() });
		}
	}

	@Override
	public void jobDeleted(JobKey jobKey) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 于 {} 删除。",
					new Object[] { jobKey, LocalDateTime.now() });
		}
	}

	@Override
	public void jobPaused(JobKey jobKey) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 于 {} 暂停调度。",
					new Object[] { jobKey, LocalDateTime.now() });
		}
	}

	@Override
	public void jobsPaused(String jobGroup) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("JobGroup {} 于 {} 暂停调度。",
					new Object[] { jobGroup, LocalDateTime.now() });
		}
	}

	@Override
	public void jobResumed(JobKey jobKey) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Job {} 于 {} 恢复调度。",
					new Object[] { jobKey, LocalDateTime.now() });
		}
	}

	@Override
	public void jobsResumed(String jobGroup) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("JobGroup {} 于 {} 恢复调度。",
					new Object[] { jobGroup, LocalDateTime.now() });
		}
	}

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("调度器 于 {} 发生错误，错误信息 {}，异常 {}。",
					new Object[] { LocalDateTime.now(), msg, cause });
		}
	}

	@Override
	public void schedulerInStandbyMode() {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("调度器 于 {} 处于待命状态。", new Object[] { LocalDateTime.now() });
		}
	}

	@Override
	public void schedulerStarted() {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("调度器 于 {} 启动。", new Object[] { LocalDateTime.now() });
		}
	}

	@Override
	public void schedulerStarting() {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("调度器启动中。", new Object[] { LocalDateTime.now() });
		}
	}

	@Override
	public void schedulerShutdown() {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("调度器 于 {} 关闭。", new Object[] { LocalDateTime.now() });
		}
	}

	@Override
	public void schedulerShuttingdown() {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("调度器关闭中。", new Object[] { LocalDateTime.now() });
		}
	}

	@Override
	public void schedulingDataCleared() {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("调度器于 {} 数据清空。", new Object[] { LocalDateTime.now() });
		}
	}

}
