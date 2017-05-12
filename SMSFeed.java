package eops.hub.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import eops.hub.model.pk.SMSFeedPK;
@Entity
@Table(name="SMSFEED")
public class SMSFeed implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SMSFEED_SEQ")
	@SequenceGenerator(name="SMSFEED_SEQ", sequenceName="SMSFEED_SEQ", allocationSize=1)
	private long feedSeq;
	@Column(name="FEEDHOURSEQ")
	private int feedHourSeq; 
	@Column(name="CREATEDDATE")
	private Date createDate;
	@Column(name="PROCESSID")
	private String processId;	
	@Column(name="DATA")
	private String data;
	@Column(name="ISDOWNLOADED")
	private String isDownLoad;
	@Column(name="USERID")
	private String userId;
	@Column(name="DOWNLOADDATE")
	private Timestamp downLoadDate;
	
	public SMSFeed() {}
	
	
	public SMSFeed(SMSFeedPK smsFeedpk) {
		this.feedHourSeq = smsFeedpk.getFeedHourSeq();
		this.createDate = smsFeedpk.getCreateDate();
		this.processId = smsFeedpk.getProcessId();
	}


	public int getFeedHourSeq() {
		return feedHourSeq;
	}
	public void setFeedHourSeq(int feedHourSeq) {
		this.feedHourSeq = feedHourSeq;
	}

	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getIsDownLoad() {
		return isDownLoad;
	}
	public void setIsDownLoad(String isDownLoad) {
		this.isDownLoad = isDownLoad;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}



	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Timestamp getDownLoadDate() {
		return downLoadDate;
	}


	public void setDownLoadDate(Timestamp downLoadDate) {
		this.downLoadDate = downLoadDate;
	}


	public long getFeedSeq() {
		return feedSeq;
	}


	public void setFeedSeq(long feedSeq) {
		this.feedSeq = feedSeq;
	}
	
	
	
}
