package fwp.alsaccount.sabhrs.grid;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsNonAlsTemplateAS;
import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.admin.AlsNonAlsTemplate;
import fwp.alsaccount.dao.admin.AlsNonAlsTemplateIdPk;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesIdPk;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.utils.HibHelpers;
import fwp.security.user.UserDTO;


public class GenAlsSabhrsEntriesGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;
	private AlsSabhrsEntriesIdPk idPk = new AlsSabhrsEntriesIdPk();
	private Integer asacBudgetYear;
	private String asacReference;
	private String jlr;
	private String aamAccount;
	private String aamFund;
	private String aocOrg;
	private Integer asacProgram;
	private String asacSubclass;
	private String aamBusinessUnit;
	private String asacProjectGrant;
	private Double aseAmt;
	private String asacSystemActivityTypeCd;
	private Integer asacTxnCd;
	private String aseDrCrCd;
	private Integer aseSeqNo;
	private String aseLineDescription;
	
	private String templates;
	private Integer transGrp;
	private String transIdentifier;
	private String provNo;
	private String bpTo;
	
	
	
	public String execute() throws Exception{
		AlsNonAlsTemplateAS anatAS = new AlsNonAlsTemplateAS();
		AlsNonAlsTemplateIdPk anatIdPk = null;
		AlsNonAlsTemplate anat = new AlsNonAlsTemplate();
		
		AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
		AlsSabhrsEntriesIdPk aseIdPk = new AlsSabhrsEntriesIdPk();
		AlsSabhrsEntries ase = null;
		
		AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
		String where = " WHERE idPk.atgsGroupIdentifier = '"+transIdentifier+"'";
		List<AlsTransactionGrpStatus> atgsLst = atgsAS.findAllByWhere(where);
		AlsTransactionGrpStatus atgs = null;
		
		HibHelpers hh = new HibHelpers();
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		Integer curBudgYear = Integer.parseInt(hh.getCurrentBudgetYear());
		
		String errMsg="";	
		
		String tmpCd = "";
		Double tmpAmt = 0.0;
		try{
			if (oper.equalsIgnoreCase("edit") || oper.equalsIgnoreCase("del")) {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String[] keys = this.id.split("_");
				aseIdPk.setAseWhenEntryPosted(new Timestamp(formatter.parse(keys[0]).getTime()));
				aseIdPk.setAseSeqNo(Integer.parseInt(keys[1]));
				aseIdPk.setAseDrCrCd(keys[2]);
				aseIdPk.setAseTxnCdSeqNo(Integer.parseInt(keys[3]));

				ase = aseAS.findById(aseIdPk);
			}
			
			if (oper.equalsIgnoreCase("add")) {
				if(aamAccount == "06" && ("".equals(asacSubclass)||asacSubclass == null)){
					addActionError("Expense account requries subclass.");
				}
			
				if (this.hasActionErrors()) {
					return "error_json";
				}
				ase = new AlsSabhrsEntries();
		    	aseIdPk = idPk;
		    	aseIdPk.setAseWhenEntryPosted(date);
		    	aseIdPk.setAseSeqNo(aseAS.getNextSeqNo());
		    	aseIdPk.setAseTxnCdSeqNo(1);
		    	ase.setIdPk(aseIdPk);
		    	
		    	ase.setAseAmt(aseAmt);
		    	ase.setAseAllowUploadToSummary("Y");
		    	ase.setAamAccount(aamAccount);
		    	ase.setAamFund(aamFund);
		    	ase.setAocOrg(aocOrg);
		    	ase.setAsacSubclass(asacSubclass);
		    	ase.setAsacProjectGrant(asacProjectGrant);
		    	ase.setAseLineDescription(aseLineDescription);
		    	ase.setAsacSystemActivityTypeCd("Z");
		    	ase.setAsacTxnCd("9");
		    	ase.setAsacBudgetYear(asacBudgetYear);
		    	ase.setAsacProgram(asacProgram);
		    	ase.setAamBusinessUnit(aamBusinessUnit);
		    	ase.setAseWhenUploadedToSumm(getUploadedToSumDt());
		    	ase.setAsacReference(aseAS.getDescReference(jlr));

		    	ase.setAtgsGroupIdentifier(transIdentifier);
		    	ase.setAtgTransactionCd(transGrp);
		    	ase.setAseNonAlsFlag("Y");
		    	//TODO need to remove this logic when the triggers and correct audit columns are added to the db	
				ase.setAseWhoLog(userInfo.getStateId().toString());
				ase.setAseWhenLog(date);
				//********************************************************************
				aseAS.save(ase);
				
				/*UPDATE ALS_TRANSACTION_GRP_STATUS*/
				if(!atgsLst.isEmpty()){
					atgs = atgsLst.get(0);
					if("C".equals(ase.getIdPk().getAseDrCrCd())){
						atgs.setAtgsNetDrCr(atgs.getAtgsNetDrCr()-aseAmt);
					}else if("D".equals(ase.getIdPk().getAseDrCrCd())){
						atgs.setAtgsNetDrCr(atgs.getAtgsNetDrCr()+aseAmt);
					}
					atgsAS.save(atgs);
				}
			} else if (oper.equalsIgnoreCase("addTemplates")) {
				String[] templates = this.templates.split(",");
				for(int i=0;i<templates.length;i++){
			    	String[] values = templates[i].split("-");
			    	tmpCd = values[0];
			    	tmpAmt = Double.valueOf(values[1]);
			    	
			    	anatIdPk = new AlsNonAlsTemplateIdPk();
			    	anatIdPk.setAnatBudgetYear(curBudgYear);
			    	anatIdPk.setAnatCd(tmpCd);
			    	anat = anatAS.findById(anatIdPk);
			    	
			    	ase = new AlsSabhrsEntries();
			    	aseIdPk = new AlsSabhrsEntriesIdPk();
			    	aseIdPk.setAseWhenEntryPosted(date);
			    	aseIdPk.setAseDrCrCd("C");
			    	aseIdPk.setAseSeqNo(aseAS.getNextSeqNo());
			    	aseIdPk.setAseTxnCdSeqNo(1);
			    	ase.setIdPk(aseIdPk);
			    	ase.setAseAmt(tmpAmt);
			    	ase.setAseAllowUploadToSummary("Y");
			    	ase.setAamAccount(anat.getAnatCrAccount());
			    	ase.setAamFund(anat.getAnatFund());
			    	ase.setAocOrg(anat.getAnatCrOrg());
			    	ase.setAsacSubclass(anat.getAnatCrSubclass());
			    	ase.setAsacProjectGrant(anat.getAnatCrProjectGrant());
			    	ase.setAseLineDescription(anat.getAnatCrLineDesc());
			    	ase.setAsacSystemActivityTypeCd("Z");
			    	ase.setAsacTxnCd("9");
			    	ase.setAsacBudgetYear(curBudgYear);
			    	ase.setAsacProgram(curBudgYear);
			    	ase.setAamBusinessUnit(hh.getBusinessUnit());
			    	ase.setAseWhenUploadedToSumm(getUploadedToSumDt());
			    	ase.setAnatCd(tmpCd);
			    	ase.setAtgsGroupIdentifier(transIdentifier);
			    	ase.setAtgTransactionCd(transGrp);
			    	ase.setAseNonAlsFlag("Y");
			    	//TODO need to remove this logic when the triggers and correct audit columns are added to the db	
					ase.setAseWhoLog(userInfo.getStateId().toString());
					ase.setAseWhenLog(date);
					//********************************************************************
					aseAS.save(ase);

			    	aseIdPk.setAseDrCrCd("D");
			    	aseIdPk.setAseSeqNo(aseAS.getNextSeqNo());
			    	ase.setIdPk(aseIdPk);
			    	ase.setAamAccount(anat.getAnatDrAccount());
			    	ase.setAocOrg(anat.getAnatDrOrg());
			    	ase.setAsacSubclass(anat.getAnatDrSubclass());
			    	ase.setAsacProjectGrant(anat.getAnatDrProjectGrant());
			    	ase.setAseLineDescription(anat.getAnatDrLineDesc());

					aseAS.save(ase);
				}
			}else if(oper.equalsIgnoreCase("edit")){
				/*UPDATE ALS_TRANSACTION_GRP_STATUS*/
				if(!atgsLst.isEmpty()){
					atgs = atgsLst.get(0);
					if("C".equals(ase.getIdPk().getAseDrCrCd())){
						atgs.setAtgsNetDrCr(atgs.getAtgsNetDrCr()+ase.getAseAmt()-aseAmt);
					}else if("D".equals(ase.getIdPk().getAseDrCrCd())){
						atgs.setAtgsNetDrCr(atgs.getAtgsNetDrCr()-ase.getAseAmt()+aseAmt);
					}
					atgsAS.save(atgs);
				}
				
				ase.setAamAccount(aamAccount);
				ase.setAamFund(aamFund);
				ase.setAocOrg(aocOrg);
				ase.setAsacBudgetYear(asacBudgetYear);
				ase.setAsacProgram(asacProgram);
				ase.setAsacSubclass(asacSubclass);

				ase.setAsacProjectGrant(asacProjectGrant);
				ase.setAseAmt(aseAmt);
				
				ase.setAseLineDescription(aseLineDescription);
				if("".equals(jlr)){
					ase.setAsacReference(null);
				}else{
					ase.setAsacReference(aseAS.getDescReference(jlr));
				}
				
				
				//TODO need to remove this logic when the triggers and correct audit columns are added to the db	
				ase.setAseWhoLog(userInfo.getStateId().toString());
				ase.setAseWhenLog(date);
				//********************************************************************
				aseAS.save(ase);
				
				
			}else if(oper.equalsIgnoreCase("del")){
				aseAS.delete(ase);
				/*UPDATE ALS_TRANSACTION_GRP_STATUS*/
				if(!atgsLst.isEmpty()){
					atgs = atgsLst.get(0);
					if("C".equals(ase.getIdPk().getAseDrCrCd())){
						atgs.setAtgsNetDrCr(atgs.getAtgsNetDrCr()+ase.getAseAmt());
					}else if("D".equals(ase.getIdPk().getAseDrCrCd())){
						atgs.setAtgsNetDrCr(atgs.getAtgsNetDrCr()-ase.getAseAmt());
					}
					atgsAS.save(atgs);
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
	
	public Timestamp getUploadedToSumDt() throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date tmpDate = dateFormat.parse("01/01/1900 00:00:00");
		long time = tmpDate.getTime();
		Timestamp rtn = new Timestamp(time);
		return rtn;
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

	public Integer getAsacBudgetYear() {
		return asacBudgetYear;
	}

	public void setAsacBudgetYear(Integer asacBudgetYear) {
		this.asacBudgetYear = asacBudgetYear;
	}

	public String getAsacReference() {
		return asacReference;
	}

	public void setAsacReference(String asacReference) {
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

	public String getAamBusinessUnit() {
		return aamBusinessUnit;
	}

	public void setAamBusinessUnit(String aamBusinessUnit) {
		this.aamBusinessUnit = aamBusinessUnit;
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

	public Integer getAsacTxnCd() {
		return asacTxnCd;
	}

	public void setAsacTxnCd(Integer asacTxnCd) {
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
	
	public String getTemplates() {
		return templates;
	}

	public void setTemplates(String templates) {
		this.templates = templates;
	}

	public Integer getTransGrp() {
		return transGrp;
	}

	public void setTransGrp(Integer transGrp) {
		this.transGrp = transGrp;
	}

	public String getTransIdentifier() {
		return transIdentifier;
	}

	public void setTransIdentifier(String transIdentifier) {
		this.transIdentifier = transIdentifier;
	}
	
	public String getProvNo() {
		return provNo;
	}

	public void setProvNo(String provNo) {
		this.provNo = provNo;
	}

	public String getBpTo() {
		return bpTo;
	}

	public void setBpTo(String bpTo) {
		this.bpTo = bpTo;
	}

	public String getJlr() {
		return jlr;
	}

	public void setJlr(String jlr) {
		this.jlr = jlr;
	}
	
	
}
