package eops.hub.job.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import eops.common.EopsData;
import eops.common.exception.ApplicationException;
import eops.common.function.FunctionOperation;
import eops.common.util.EOPSConstants;
import eops.common.util.LogWriter;
import eops.hub.dao.OperationParamDAO;
import eops.hub.dao.QueryExecutorDAO;
import eops.hub.model.Operation;
import eops.hub.model.OperationParam;
import eops.hub.model.ScriptDefinition;
import eops.hub.model.Task;


/**
 * Operation is used to execute logic configured in Java Script in ScriptDefinition table.
 * One need to configure fully compilable and executable java script function in table.
 * Operation in turn will execute the function and set result into the FTD Hash.
 * If function require any value from at time of execution then it takes from FTD Hash.
 * The server side variable is the FTDHASH key followed by : for example :PROCESSID, :TXNREFNO, :ProductType.
 * Here ProductType is key in the FTDHASH.
 * 
 *  
 * @author 1406530
 *
 */
public class ScriptExecutorOperation implements FunctionOperation{
	@Override
	public EopsData execute(Operation operation, EopsData eopsData)
	throws ApplicationException {
		LogWriter  out  = LogWriter.getInstance("ScriptExecutorOperation","","ScriptExecutorLog");
		long startTime = System.currentTimeMillis();
		String processId = null;

		//Validation for FtdHash
		Task task = eopsData.getTask();
		Map ftdHash = eopsData.getFtdHash();
		List<String> errList = (eopsData.getException() == null) ? new ArrayList() : eopsData.getException() ;
		if(ftdHash == null) {
			String messgae="FtdHash is not avaible";
			errList.add(messgae);
			eopsData.setFrozen(true);
			eopsData.setException(errList);
			return eopsData;
		}

		QueryExecutorDAO qeDAO = new QueryExecutorDAO(eopsData.getJprovider());
		OperationParamDAO opDAO = new OperationParamDAO(eopsData.getJprovider());
		String ruleId = null; // ScriptId
		String funName = null;
		String functionExpression = null;
		String functionExpressionValue = null;
		String scriptGroupId = operation.getParam1();
		processId = eopsData.getProcessId();
		out.println("#ScriptExecutorOperation#execute#I#" + scriptGroupId + "#" + startTime);
		scriptGroupId = (scriptGroupId != null) ? scriptGroupId.trim(): "";
		List<OperationParam> paramList = opDAO.getOperationParam(operation.getOperationId(), processId, scriptGroupId);

		//Adding common attributes to FTD Hash
		ftdHash.put(EOPSConstants.COUNTRYCODE,eopsData.getCountryCode());
		ftdHash.put(EOPSConstants.PROCESSID, processId);
		ftdHash.put(EOPSConstants.STEPNAME, eopsData.getTask().getStepName());
		ftdHash.put(EOPSConstants.TXNREF, eopsData.getTxRefNo());
		for (OperationParam rule : paramList) {
			try {
				ruleId = rule.getParamValue(); // ScriptId
				funName = rule.getParam1(); // Funtion Name to execute
				List<ScriptDefinition> scriptDefinitionList = qeDAO.getScriptDefinitionList(ruleId);
				for (ScriptDefinition scriptDefinition : scriptDefinitionList) {
					functionExpression = getScript(ftdHash, scriptDefinition.getScriptExpression());	
					functionExpressionValue = getFunctionExpressionValue(functionExpression, funName);
					setValueToFTD(eopsData, functionExpressionValue);
				}
				out.println("#ScriptExecutorOperation#execute#" + ruleId + "#" + functionExpressionValue+ "#" + functionExpression);
			} catch(ApplicationException ex) {
				throw new ApplicationException("#ScriptExecutorException#Error#" + ruleId + "#" + functionExpressionValue);
			} catch(Exception ex) {
				out.println("#ScriptExecutorOperation#execute#" + ruleId + "#" + functionExpressionValue+ "#" + functionExpression);
				ex.printStackTrace();
			}
		}
		return eopsData;
	}
	
	/**
	 * Result of Script/Function execution is set to FTDHash.
	 * @param eopsData
	 * @param result
	 * @throws ApplicationException
	 */
	public void setValueToFTD(EopsData eopsData, String result) throws ApplicationException {
		LogWriter  out  = LogWriter.getInstance("ScriptExecutorOperation","","ScriptExecutorLog");
		if(result != null && result.length()>0) {
			Map<String, String> ftdHash = eopsData.getFtdHash();
			List<String> exception = null;
			List<String> warningMessage = null;
			String[] keyValues = result.split("::");
			for (String keyValue : keyValues) {
				String[] jointKeyValue = keyValue.split("~");
				String key = jointKeyValue[0];
				String value = jointKeyValue[1];
				if(EOPSConstants.SCRIPT_ERROR.equalsIgnoreCase(key) || EOPSConstants.SCRIPT_WARNING.equalsIgnoreCase(key)) {
					exception = eopsData.getException();
					if(exception == null ) {
						exception = new ArrayList<String>();
					}
					exception.add(value);
				} else if(EOPSConstants.SCRIPT_WARNING_LEVEL.equalsIgnoreCase(key)) {
					warningMessage = eopsData.getWarningMessage();
					if(warningMessage == null ) {
						warningMessage = new ArrayList<String>();
					}
					warningMessage.add(value);							
				} else {
					Object retVal = ftdHash.put(key, value);
					if (retVal != null ) {
						out.println("#ScriptExecutorOperation#setValueToFTD#Replace#" + key + "#" + retVal.toString() + "#" + value);
					}
				}

				if(EOPSConstants.SCRIPT_ERROR.equalsIgnoreCase(key)) {
					eopsData.setFrozen(true);
					eopsData.setException(exception);
					throw new ApplicationException("ScriptExecutorException - Error occured#" + value);
				}
			}
		}

	}
	/**
	 * Method is used to execute the Script defined/configured.
	 * @param functionExpression
	 * @param functionName
	 * @return
	 * @throws ApplicationException
	 * @throws NoSuchMethodException
	 */
	public String getFunctionExpressionValue(String functionExpression, String functionName)throws ApplicationException, NoSuchMethodException{
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");        
		Object result = null;
		try {
			if(engine instanceof Invocable){
				engine.eval(functionExpression);
				result = ((Invocable)engine).invokeFunction(functionName);
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return (result != null) ? result.toString() : "" ;
	}

	
	/**
	 * Script expression is replaced with Server Side FTDHash value for give place holder in the script.
	 * @param ftdHash
	 * @param rulExpression
	 * @return
	 * @throws ApplicationException
	 */
	public String getScript(Map ftdHash, String rulExpression)throws ApplicationException{
		long st=0;
		String fieldArr[]=null;
		String fieldKey=null;
		String fieldVal=null;
		String exprTemp="";
		try{
			st=System.currentTimeMillis();			
			fieldArr = rulExpression.split("[^\\w]");
			for (int i = 0; i < fieldArr.length; i++) {
				fieldKey = fieldArr[i];
				if(!fieldKey.matches("[0-9]+")&& fieldKey!= null && fieldKey.trim().length() > 0 ){
					if(ftdHash.containsKey(fieldKey)){
						fieldVal = (ftdHash.get(fieldKey) != null)? ftdHash.get(fieldKey).toString() : null;
						String toReplace = ":\\b" + fieldKey + "\\b"; //"^*.(:Replace)$*"
						if(fieldVal!=null && fieldVal.trim().length()>0){
							rulExpression = rulExpression.replaceAll(toReplace, fieldVal);
						}
					} else {
						//throw new ApplicationException("Inside getExpression#" + fieldKey +  "#fieldKey not available");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException("Exception in getExpression#" +  fieldKey + "#" + rulExpression+ "#exception#"+e.getMessage());
		}	
		return rulExpression;
	}
}
