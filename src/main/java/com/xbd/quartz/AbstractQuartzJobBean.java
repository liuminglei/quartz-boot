package com.xbd.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

/**
 * 默认定时任务QuartzJobBean
 *
 * @author luas
 * @since 1.0
 */
public abstract class AbstractQuartzJobBean extends QuartzJobBean {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public abstract String name();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("任务{}执行开始{}...", new Object[] { name(), LocalDateTime.now()});
		}

		executeInternalInternal(context);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("任务{}执行完成{}...", new Object[] { name(), LocalDateTime.now()});
		}
	}

	protected abstract void executeInternalInternal(JobExecutionContext context) throws JobExecutionException;

}
