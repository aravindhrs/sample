package eops.hub.model.pk;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class SMSFeedPK implements Serializable  {
	
	int feedHourSeq; 
	Date createDate;
	String processId;

	
	public SMSFeedPK(){}
	public SMSFeedPK(int feedHourSeq,String processId, Date createDate){
		this.feedHourSeq = feedHourSeq;
		this.createDate = createDate;
		this.processId = processId;
	}
	
	
	
	public int getFeedHourSeq() {
		return feedHourSeq;
	}
	public void setFeedHourSeq(int feedHourSeq) {
		this.feedHourSeq = feedHourSeq;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + feedHourSeq;
		result = prime * result
				+ ((processId == null) ? 0 : processId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SMSFeedPK other = (SMSFeedPK) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (feedHourSeq != other.feedHourSeq)
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		return true;
	}


}
