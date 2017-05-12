/**
 * TAG			: //Reject Reason
 * Description	: Reject reason is a feature that allows the user to provide a predefined reasons for the rejection of the application.
 * 				  To implement this feature, One combo box need to be added with list of reject reasons from reason list table.
 * Modified By 	: Don Bosco Rayappan - 1507420.
 * Modified On	: 10-Feb-2015
 *  * @modification
 * TAG 			: //StepLevelCache
 * Description 	: Adding steplevel caching in stepdefinition
 * Modified by 	: Umamaheswari Subramaniyan
 * 
 */
package eops.hub.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import eops.hub.model.pk.StepDefinitionPK;

@Entity
@Table(name = "StepDefinition")
@IdClass(StepDefinitionPK.class)
@Cacheable
public class StepDefinition  implements Serializable{

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	protected String name;
	@Id
	protected String companyCode;
	@Id
	protected String processId;
    @Column(name="ABR_GROUP")
	protected String abrGroup;

    protected String chronologicalOrder;
    protected String tinstance;
    protected String isReferral;
    protected String appStatusRule;
    protected String statRequired;
    
	public String getStatRequired() {
		return statRequired;
	}

	public void setStatRequired(String statRequired) {
		this.statRequired = statRequired;
	}
    
    private String dynaScreenId;
    
//	private String dynaTemplate;
//	
//	
//	
//	
//	public String getDynaTemplate() {
//		return dynaTemplate;
//	}
//	public void setDynaTemplate(String dynaTemplate) {
//		this.dynaTemplate = dynaTemplate;
//	}
    
	public String getDynaScreenId() {
		return dynaScreenId;
	}

	public void setDynaScreenId(String dynaScreenId) {
		this.dynaScreenId = dynaScreenId;
	}
    //ConfigCache: Start
    protected String suId;
    
	public String getSuId() {
		return suId;
	}

	public void setSuId(String suId) {
		this.suId = suId;
	}
	//ConfigCache: End
	public String getChronologicalOrder() {
		return chronologicalOrder;
	}

	public void setChronologicalOrder(String chronologicalOrder) {
		this.chronologicalOrder = chronologicalOrder;
	}
	//StepLevelCache - start
	protected String enableCache;

	public String getEnableCache() {
		return enableCache;
	}

	public void setEnableCache(String enableCache) {
		this.enableCache = enableCache;
	}
	//StepLevelCache - end

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "companyCode", referencedColumnName = "companyCode", insertable=false, updatable=false),
			@JoinColumn(name = "processId", referencedColumnName = "processId", insertable=false, updatable=false) })
	protected ProcessDefinition processDefinition;

	protected String allowUpload; // boolean
	protected String stepType;
	protected String stepMode;
	protected String category;
	protected String label;

	@OneToMany(mappedBy = "stepDefinition", 
			cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE },
	fetch=FetchType.LAZY)
	@OrderBy("displayOrder ASC")
	private List<StepFieldMapping> stepFields;

	@OneToMany(mappedBy = "stepDefinition",
			cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE },
			fetch=FetchType.LAZY)
	@OrderBy("displayOrder ASC")
	protected List<StepDocMapping> docMappings;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name = "PriorityRuleMapping", joinColumns = {
			@JoinColumn(name = "companyCode", referencedColumnName = "companyCode", insertable=false, updatable=false),
			@JoinColumn(name = "processId", referencedColumnName = "processId", insertable=false, updatable=false),
			@JoinColumn(name = "stepName", referencedColumnName = "name", insertable=false, updatable=false) }, 
			inverseJoinColumns = {			
				@JoinColumn(name = "CompanyCode", referencedColumnName = "CompanyCode", insertable=false, updatable=false),
				@JoinColumn(name = "ProcessId", referencedColumnName = "ProcessId", insertable=false, updatable=false),
				@JoinColumn(name = "RuleId", referencedColumnName = "RuleId", insertable=false, updatable=false)
			})
	protected List<RuleDefinition> priorityRules;
	
	@OneToMany(mappedBy = "stepDefinition",
			cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE },
			fetch=FetchType.LAZY)
	protected List<StepCheckListMapping> checkListMappings;
	
	@OneToMany(mappedBy = "stepDefinition",
			cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE },
			fetch=FetchType.LAZY)
	protected List<StepReferralListMapping> referralListMappings;

	//Reject Reason:: Added relationship persistence bag for reject reason : START
	@OneToMany(mappedBy = "stepDefinition",
			cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE },
			fetch=FetchType.EAGER)
	protected List<StepReasonMapping> reasonListMapping;
	//Reject Reason:: Added relationship persistence bag for reject reason : END
	@OneToMany(mappedBy = "stepDefinition",
			cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE },
			fetch=FetchType.LAZY)
	@OrderBy("displayOrder ASC")
	protected List<StepResponseMapping> responseListMappings;
	
	@OneToMany(mappedBy="stepDefinition",cascade={CascadeType.REFRESH})
	private List <StepRuleMapping> stepRuleMapping;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "companyCode", referencedColumnName = "companyCode", insertable=false, updatable=false),
		@JoinColumn(name = "processId", referencedColumnName = "processId", insertable=false, updatable=false),
		@JoinColumn(name = "uiTemplateName", referencedColumnName = "name", insertable=false, updatable=false)})
	protected TemplateDefinition templateDefinition;

	protected int TAT;
	
	protected int displayOrder;
	
	protected int maxLockedTime;
	
	protected String dapId;
	protected String autocomment;
	protected String autoMovementFlag;
	protected String notificationFlag;
	protected String escalationFlag;
	protected String multiScreen;
	protected String cntBrowse;
	
	// SMS Download change : start
	protected String triger;
	
	public String getTriger() {
		return triger;
	}

	public void setTriger(String triger) {
		this.triger = triger;
	}
	// SMS Download change : end
	public String getCntBrowse() {
		return cntBrowse;
	}

	public void setCntBrowse(String cntBrowse) {
		this.cntBrowse = cntBrowse;
	}
	
	public String getMultiScreen() {
		return multiScreen;
	}

	public void setMultiScreen(String multiScreen) {
		this.multiScreen = multiScreen;
	}

	public String getAutoMovementFlag() {
		return autoMovementFlag;
	}

	public void setAutoMovementFlag(String autoMovementFlag) {
		this.autoMovementFlag = autoMovementFlag;
	}

	public String getNotificationFlag() {
		return notificationFlag;
	}

	public void setNotificationFlag(String notificationFlag) {
		this.notificationFlag = notificationFlag;
	}

	public String getEscalationFlag() {
		return escalationFlag;
	}

	public void setEscalationFlag(String escalationFlag) {
		this.escalationFlag = escalationFlag;
	}

	public String getAutocomment() {
		return autocomment;
	}

	public void setAutocomment(String autocomment) {
		this.autocomment = autocomment;
	}

	
	public String getDapId() {
		return dapId;
	}

	public void setDapId(String dapId) {
		this.dapId = dapId;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public TemplateDefinition getTemplateDefinition() {
		return templateDefinition;
	}

	public void setTemplateDefinition(TemplateDefinition templateDefinition) {
		this.templateDefinition = templateDefinition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getAllowUpload() {
		return allowUpload;
	}

	public void setAllowUpload(String allowUpload) {
		this.allowUpload = allowUpload;
	}
	
	@Transient
	public boolean isAllowUpload() {
		if (allowUpload.equalsIgnoreCase("Y"))
			return true;
		else
			return false;
	}
	@Transient
	public void setAllowUpload(boolean allowUpload) {
		if (allowUpload)
			this.allowUpload = "Y";
		else
			this.allowUpload = "N";
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public String getStepMode() {
		return stepMode;
	}

	public void setStepMode(String stepMode) {
		this.stepMode = stepMode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public List<StepFieldMapping> getStepFields() {
		return stepFields;
	}

	public void setStepFields(List<StepFieldMapping> stepFields) {
		this.stepFields = stepFields;
	}
	
	/**
	 * @return the docMappings
	 */
	
	public List<StepDocMapping> getDocMappings() {
		return docMappings;
	}

	/**
	 * @param docMappings the docMappings to set
	 */
	public void setDocMappings(List<StepDocMapping> docMappings) {
		this.docMappings = docMappings;
	}

	/**
	 * @return the priorityRules
	 */
	public List<RuleDefinition> getPriorityRules() {
		return priorityRules;
	}

	/**
	 * @param priorityRules the priorityRules to set
	 */
	public void setPriorityRules(List<RuleDefinition> priorityRules) {
		this.priorityRules = priorityRules;
	}

	public List<StepReferralListMapping> getReferralListMappings() {
		return referralListMappings;
	}

	public void setReferralListMappings(List<StepReferralListMapping> referralListMappings) {
		this.referralListMappings = referralListMappings;
	}
	
	public List<StepCheckListMapping> getCheckListMappings() {
		return checkListMappings;
	}

	public void setCheckListMappings(List<StepCheckListMapping> checkListMappings) {
		this.checkListMappings = checkListMappings;
	}

	/**
	 * @return the stepRuleMapping
	 */
	public List<StepRuleMapping> getStepRuleMapping() {
		return stepRuleMapping;
	}

	/**
	 * @param stepRuleMapping the stepRuleMapping to set
	 */
	public void setStepRuleMapping(List<StepRuleMapping> stepRuleMapping) {
		this.stepRuleMapping = stepRuleMapping;
	}

	public int getTAT() {
		return TAT;
	}

	public void setTAT(int tAT) {
		TAT = tAT;
	}

	public List<StepResponseMapping> getResponseListMappings() {
		return responseListMappings;
	}

	public void setResponseListMappings(
			List<StepResponseMapping> responseListMappings) {
		this.responseListMappings = responseListMappings;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the maxLockedTime
	 */
	public int getMaxLockedTime() {
		return maxLockedTime;
	}

	/**
	 * @param maxLockedTime the maxLockedTime to set
	 */
	public void setMaxLockedTime(int maxLockedTime) {
		this.maxLockedTime = maxLockedTime;
	}

	public String getAbrGroup() {
		return abrGroup;
	}

	public void setAbrGroup(String abrGroup) {
		this.abrGroup = abrGroup;
	}

	@OneToMany(mappedBy = "stepDefinition",cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE },	fetch=FetchType.LAZY)
	private List<GridTemplateWidgetMapping> gridTemplateList;

	public List<GridTemplateWidgetMapping> getGridTemplateList() {
		return gridTemplateList;
	}

	public void setGridTemplateList(List<GridTemplateWidgetMapping> gridTemplateList) {
		this.gridTemplateList = gridTemplateList;
	}
	public String getTinstance() {
		return tinstance;
	}

	public void setTinstance(String tinstance) {
		this.tinstance = tinstance;
	}
	public String getIsReferral() {
		return isReferral;
	}

	public void setIsReferral(String isReferral) {
		this.isReferral = isReferral;
	}
	public String getAppStatusRule() {
		return appStatusRule;
	}

	public void setAppStatusRule(String appStatusRule) {
		this.appStatusRule = appStatusRule;
	}

	//Reject Reason:: Added relationship persistence bag for reject reason : START
	/**
	 * @return the reasonListMapping
	 */
	public List<StepReasonMapping> getReasonListMapping() {
		return reasonListMapping;
	}

	/**
	 * @param reasonListMappings the reasonListMappings to set
	 */
	public void setReasonListMapping(List<StepReasonMapping> reasonListMapping) {
		this.reasonListMapping = reasonListMapping;
	}
	//Reject Reason:: Added relationship persistence bag for reject reason : END

//	public String toString() {
//		StringBuffer sbResult = new StringBuffer();
//		try {
//			Class cls = this.getClass();
//			Field fieldlist[] = cls.getDeclaredFields();
//			for (int i = 0; i < fieldlist.length; i++) {
//				Field fld = fieldlist[i];
//				sbResult.append(fld.getName() + " = ");
//				sbResult.append(fld.get(this) + " ;");
//			}
//
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//				return sbResult.toString();
//	}

//	GRID_HIDE : Start
	protected String gridhide;

	public String getGridhide() {
		return gridhide;
	}

	public void setGridhide(String gridhide) {
		this.gridhide = gridhide;
	}
	
	
//	GRID_HIDE : End
}
