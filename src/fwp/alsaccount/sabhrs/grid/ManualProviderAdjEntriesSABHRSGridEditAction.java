package fwp.alsaccount.sabhrs.grid;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesDAO;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesIdPk;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.hibernate.utils.DalUtils;
import fwp.alsaccount.utils.Utils;
import fwp.security.user.UserDTO;


public class ManualProviderAdjEntriesSABHRSGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;

	private AlsSabhrsEntriesIdPk idPk;

	private Integer 			provNo;
    private Integer 			iafaSeqNo;
    private Date				bpFrom;
    private Date				bpTo;
    
    private String aamBusinessUnit;
    private Integer asacReference;
    private String aamAccount;
    private String aamFund;
    private String aocOrg;
    private Integer asacProgram;
    private String asacSubclass;
    private Integer asacBudgetYear;
    private String asacProjectGrant;
    private Double aseAmt;
    private String asacSystemActivityTypeCd;
    private String asacTxnCd;
    private String aseDrCrCd;
    private Integer aseSeqNo;
    private String aseLineDescription;

	
    AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
    
    UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
	Timestamp date = new Timestamp(System.currentTimeMillis());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	
	public String execute() throws Exception{
		DalUtils dalUtils = new DalUtils();
		String errMsg="";	
		AlsSabhrsEntriesIdPk aseIdPk = new AlsSabhrsEntriesIdPk();
		AlsSabhrsEntries ase = null;
		AlsSabhrsEntriesDAO aseDAO = new AlsSabhrsEntriesDAO();
		try{
			
			if (oper.equalsIgnoreCase("edit") || oper.equalsIgnoreCase("del")) {
				String[] keys = id.split("_");
				aseIdPk.setAseWhenEntryPosted(Timestamp.valueOf(keys[0]));
				aseIdPk.setAseSeqNo(Integer.valueOf(keys[1]));
				aseIdPk.setAseDrCrCd(keys[2]);
				aseIdPk.setAseTxnCdSeqNo(Integer.valueOf(keys[3]));
				ase = aseAS.findById(aseIdPk);
			}
			
			if (oper.equalsIgnoreCase("add")) {	
				aseIdPk.setAseWhenEntryPosted(date);
				aseIdPk.setAseSeqNo(dalUtils.getNextValueFromSequence("ALS_SABHRS_ENTRIES_SEQ", aseDAO.getSession()));
				aseIdPk.setAseDrCrCd(idPk.getAseDrCrCd());
				aseIdPk.setAseTxnCdSeqNo(1);
				ase = new AlsSabhrsEntries();
				ase.setIdPk(aseIdPk);
				ase.setAamAccount(aamAccount);
				ase.setAamBusinessUnit(aamBusinessUnit);
				ase.setAamFund(aamFund);
				ase.setAsacSystemActivityTypeCd("Z");
				ase.setAsacTxnCd("9");
				ase.setAocOrg(aocOrg);
				ase.setApiProviderNo(provNo);
				ase.setAprBillingFrom(new Timestamp(bpFrom.getTime()));
				ase.setAprBillingTo(new Timestamp(bpTo.getTime()));
				ase.setAsacBudgetYear(asacBudgetYear);
				ase.setAsacProgram(asacProgram);
				ase.setAsacProjectGrant(asacProjectGrant);
				ase.setAsacReference(asacReference);
				ase.setAsacSubclass(asacSubclass);
				ase.setAseAmt(aseAmt);
				ase.setAseLineDescription(aseLineDescription);
				ase.setAtgsGroupIdentifier(Utils.createIntProvGroupIdentifier(provNo, sdf.format(bpTo), iafaSeqNo));
				ase.setAseNonAlsFlag("Y");
				ase.setAseWhenUploadedToSumm(null);
				ase.setAsesSeqNo(null);
				ase.setAseWhenLog(date);
				ase.setAseWhoLog(userInfo.getStateId());
				
				aseAS.save(ase);
			}else if(oper.equalsIgnoreCase("edit")){		
				ase.setAamAccount(aamAccount);
				ase.setAamBusinessUnit(aamBusinessUnit);
				ase.setAamFund(aamFund);
				ase.setAocOrg(aocOrg);
				ase.setAsacBudgetYear(asacBudgetYear);
				ase.setAsacProgram(asacProgram);
				ase.setAsacProjectGrant(asacProjectGrant);
				ase.setAsacReference(asacReference);
				ase.setAsacSubclass(asacSubclass);
				ase.setAseAmt(aseAmt);
				ase.setAseLineDescription(aseLineDescription);
				ase.setAseWhenLog(date);
				ase.setAseWhoLog(userInfo.getStateId());
				aseAS.save(ase);
			}else if(oper.equalsIgnoreCase("del")){
				if(ase != null){
					aseAS.delete(ase);
				}
			}else if (oper.equalsIgnoreCase("reverseAlsEntries")) {
				if(aseAS.getSabhrsRecordCnt(8, Utils.createIntProvGroupIdentifier(provNo, sdf.format(bpTo), iafaSeqNo), provNo, iafaSeqNo, bpFrom, bpTo) == 0){
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
		aseLst = aseAS.getManualProviderAdjEntriesRecords(provNo, iafaSeqNo, bpFrom, bpTo);
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
			tmp.setAtgsGroupIdentifier(Utils.createIntProvGroupIdentifier(provNo, sdf.format(bpTo), iafaSeqNo));
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

	public Integer getProvNo() {
		return provNo;
	}

	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}

	public Integer getIafaSeqNo() {
		return iafaSeqNo;
	}

	public void setIafaSeqNo(Integer iafaSeqNo) {
		this.iafaSeqNo = iafaSeqNo;
	}

	public Date getBpFrom() {
		return bpFrom;
	}

	public void setBpFrom(Date bpFrom) {
		this.bpFrom = bpFrom;
	}

	public Date getBpTo() {
		return bpTo;
	}

	public void setBpTo(Date bpTo) {
		this.bpTo = bpTo;
	}

	public String getAamBusinessUnit() {
		return aamBusinessUnit;
	}

	public void setAamBusinessUnit(String aamBusinessUnit) {
		this.aamBusinessUnit = aamBusinessUnit;
	}

	public Integer getAsacReference() {
		return asacReference;
	}

	public void setAsacReference(Integer asacReference) {
		this.asacReference = asacReference;
	}

	public String getAamAccount() {
		return aamAccount;
	}

	public void setAamAccount(String aamAccount) {
		this.aamAccount = aamAccount;
	}

	public String getAamFund() {
		return aamFund;
	}

	public void setAamFund(String aamFund) {
		this.aamFund = aamFund;
	}

	public String getAocOrg() {
		return aocOrg;
	}

	public void setAocOrg(String aocOrg) {
		this.aocOrg = aocOrg;
	}

	public Integer getAsacProgram() {
		return asacProgram;
	}

	public void setAsacProgram(Integer asacProgram) {
		this.asacProgram = asacProgram;
	}

	public String getAsacSubclass() {
		return asacSubclass;
	}

	public void setAsacSubclass(String asacSubclass) {
		this.asacSubclass = asacSubclass;
	}

	public Integer getAsacBudgetYear() {
		return asacBudgetYear;
	}

	public void setAsacBudgetYear(Integer asacBudgetYear) {
		this.asacBudgetYear = asacBudgetYear;
	}

	public String getAsacProjectGrant() {
		return asacProjectGrant;
	}

	public void setAsacProjectGrant(String asacProjectGrant) {
		this.asacProjectGrant = asacProjectGrant;
	}

	public Double getAseAmt() {
		return aseAmt;
	}

	public void setAseAmt(Double aseAmt) {
		this.aseAmt = aseAmt;
	}

	public String getAsacSystemActivityTypeCd() {
		return asacSystemActivityTypeCd;
	}

	public void setAsacSystemActivityTypeCd(String asacSystemActivityTypeCd) {
		this.asacSystemActivityTypeCd = asacSystemActivityTypeCd;
	}

	public String getAsacTxnCd() {
		return asacTxnCd;
	}

	public void setAsacTxnCd(String asacTxnCd) {
		this.asacTxnCd = asacTxnCd;
	}

	public String getAseDrCrCd() {
		return aseDrCrCd;
	}

	public void setAseDrCrCd(String aseDrCrCd) {
		this.aseDrCrCd = aseDrCrCd;
	}

	public Integer getAseSeqNo() {
		return aseSeqNo;
	}

	public void setAseSeqNo(Integer aseSeqNo) {
		this.aseSeqNo = aseSeqNo;
	}

	public String getAseLineDescription() {
		return aseLineDescription;
	}

	public void setAseLineDescription(String aseLineDescription) {
		this.aseLineDescription = aseLineDescription;
	}
	
	public AlsSabhrsEntriesIdPk getIdPk() {
		return idPk;
	}

	public void setIdPk(AlsSabhrsEntriesIdPk idPk) {
		this.idPk = idPk;
	}
}