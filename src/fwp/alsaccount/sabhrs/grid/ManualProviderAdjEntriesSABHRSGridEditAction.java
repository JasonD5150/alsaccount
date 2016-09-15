package fwp.alsaccount.sabhrs.grid;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesDAO;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesIdPk;
import fwp.alsaccount.hibernate.utils.DalUtils;
import fwp.security.user.UserDTO;


public class ManualProviderAdjEntriesSABHRSGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;
	private Integer 			selProvNo;
    private Integer 			selIafaSeqNo;
    private Date				selBpFromDt;
    private Date				selBpToDt;
    private Integer 			transCd;
    private String 				groupId;
	
    AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
    
    UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
	Timestamp date = new Timestamp(System.currentTimeMillis());
	
	public String execute() throws Exception{
		

		String errMsg="";	
		
		try{
			
			if (oper.equalsIgnoreCase("edit") || oper.equalsIgnoreCase("del")) {

			}
			
			if (oper.equalsIgnoreCase("add")) {	
				
			}else if(oper.equalsIgnoreCase("edit")){		
				
			}else if(oper.equalsIgnoreCase("del")){
				
			}else if (oper.equalsIgnoreCase("reverseAlsEntries")) {
				if(aseAS.getSabhrsRecordCnt(transCd, groupId, selProvNo, selIafaSeqNo, selBpFromDt, selBpToDt) == 0){
					dupSabhrsEntries();
				}else{
					addActionError("Cannot post mutiple reversal entires. Reversal of the Current ALS Entry has been already posted.");
					return "error_json";
				}
			}
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first.";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Parent record not found.";
			  } else if (ex.toString().contains("ORA-00001")){
				  errMsg += "Unable to add this record due to duplicate.";
			  }	else {
				  errMsg += " " + ex.toString();
			  }
		    addActionError(errMsg);
		   return "error_json";
		}	
		return SUCCESS;
	}

	private String dupSabhrsEntries(){
		List<AlsSabhrsEntries> aseLst = new ArrayList<AlsSabhrsEntries>();
		AlsSabhrsEntries tmp = null;
		AlsSabhrsEntriesIdPk tmpIdPk = null;
		Timestamp ts = Timestamp.valueOf("1900-01-01 12:00:00");
		Long incr = 1000l;
		
		DalUtils dalUtils = new DalUtils();
		AlsSabhrsEntriesDAO aseDAO = new AlsSabhrsEntriesDAO();
		aseLst = aseAS.getManualProviderAdjEntriesRecords(transCd, null, selProvNo, selIafaSeqNo, selBpFromDt, selBpToDt);
		for(AlsSabhrsEntries ase : aseLst){
			tmp = new AlsSabhrsEntries();
			tmpIdPk = new AlsSabhrsEntriesIdPk();
			
			tmp.setAseWhenLog(date);
			tmpIdPk.setAseSeqNo(dalUtils.getNextValueFromSequence("ALS_SABHRS_ENTRIES_SEQ", aseDAO.getSession()));
			tmpIdPk.setAseDrCrCd(ase.getIdPk().getAseDrCrCd());
			tmpIdPk.setAseTxnCdSeqNo(ase.getIdPk().getAseTxnCdSeqNo());
			tmpIdPk.setAseWhenEntryPosted(date);
			tmp.setIdPk(tmpIdPk);
			tmp.setAsacBudgetYear(ase.getAsacBudgetYear());
			tmp.setAsacSystemActivityTypeCd("Z");
			tmp.setAsacTxnCd("9");
			tmp.setAamAccount(ase.getAamAccount());
			tmp.setAamBusinessUnit(ase.getAamBusinessUnit());
			tmp.setAamFund(ase.getAamFund());
			tmp.setAocOrg(ase.getAocOrg());
			tmp.setAsacProgram(ase.getAsacProgram());
			tmp.setAsacSubclass(ase.getAsacSubclass());
			tmp.setAsacProjectGrant(ase.getAsacProjectGrant());
			tmp.setAsacReference(ase.getAsacReference());
			tmp.setAseAmt(ase.getAseAmt());
			tmp.setAseAllowUploadToSummary(ase.getAseAllowUploadToSummary());
			tmp.setAseWhenUploadedToSumm(null);
			tmp.setAsesSeqNo(null);
			tmp.setApiProviderNo(ase.getApiProviderNo());
			tmp.setAprBillingFrom(ase.getAprBillingFrom());
			tmp.setAprBillingTo(ase.getAprBillingTo());
			tmp.setAiafaSeqNo(ase.getAiafaSeqNo());
			if("Y".equals(ase.getAseAllowUploadToSummary())){
				tmp.setAseWhenUploadedToSumm(ts);
				ts.setTime(ts.getTime()+incr);
			}else{
				tmp.setAseWhenUploadedToSumm(null);
			}
			tmp.setAtgTransactionCd(ase.getAtgTransactionCd());
			tmp.setAtgsGroupIdentifier(groupId);
			tmp.setAseNonAlsFlag("Y");
			tmp.setAseLineDescription("REVERSAL-Z9");
			tmp.setAtiTribeCd(ase.getAtiTribeCd());
			
			tmp.setAseWhoLog(userInfo.getStateId());
			aseAS.save(tmp);
		}
	
		return SUCCESS;
	}
	public String getOper() {
		return oper;
	}
	
	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSelProvNo() {
		return selProvNo;
	}

	public void setSelProvNo(Integer selProvNo) {
		this.selProvNo = selProvNo;
	}

	public Integer getSelIafaSeqNo() {
		return selIafaSeqNo;
	}

	public void setSelIafaSeqNo(Integer selIafaSeqNo) {
		this.selIafaSeqNo = selIafaSeqNo;
	}

	public Date getSelBpFromDt() {
		return selBpFromDt;
	}

	public void setSelBpFromDt(Date selBpFromDt) {
		this.selBpFromDt = selBpFromDt;
	}

	public Date getSelBpToDt() {
		return selBpToDt;
	}

	public void setSelBpToDt(Date selBpToDt) {
		this.selBpToDt = selBpToDt;
	}

	public Integer getTransCd() {
		return transCd;
	}

	public void setTransCd(Integer transCd) {
		this.transCd = transCd;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	
}
