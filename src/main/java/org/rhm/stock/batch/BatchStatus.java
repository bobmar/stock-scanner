package org.rhm.stock.batch;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;

public class BatchStatus {
	private String batchJobId = null;
	private String jobClass = null;
	private Boolean success = false;
	private String completionMsg = null;
	private LocalDateTime statusDate = null;
	private LocalDateTime startDate = null;
	private LocalDateTime finishDate = null;
	
	public BatchStatus(Class cls) {
		this.batchJobId = UUID.randomUUID().toString();
		this.jobClass = cls.getName();
		this.statusDate = LocalDateTime.now();
		this.startDate = LocalDateTime.now();
	}
	
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getCompletionMsg() {
		return completionMsg;
	}
	public void setCompletionMsg(String completionMsg) {
		this.completionMsg = completionMsg;
	}
	public LocalDateTime getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(LocalDateTime statusDate) {
		this.statusDate = statusDate;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(LocalDateTime finishDate) {
		this.finishDate = finishDate;
	}
	@Id
	public String getBatchJobId() {
		return batchJobId;
	}

	public void setBatchJobId(String batchJobId) {
		this.batchJobId = batchJobId;
	}
}
