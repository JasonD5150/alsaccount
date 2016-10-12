package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsProviderBankDetailsAS;
import fwp.alsaccount.dao.sabhrs.AlsProviderBankDetails;
import fwp.alsaccount.dao.sabhrs.AlsProviderBankDetailsIdPk;
import fwp.alsaccount.utils.HibHelpers;
import fwp.security.user.UserDTO;
import fwp.utils.FwpDateUtils;


public class InternalProviderBankCdDepLinkGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private AlsProviderBankDetailsIdPk idPk;
	private String id;
	private String abcBankCd;
	private String bankName;
	private Double apbdAmountDeposit;
	private Date depositDate;
	private Date BillingFrom;
	private Date apbdBillingTo;
	private Date deadlineDate;
	private Double amtDue;
	private String apbdDepositId;
	private Integer provNo;
	private Integer apbdSeqNo;
	private String apbdCashInd;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	public String execute() throws Exception{		
		UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		String errMsg="";			
		
		HibHelpers hh = new HibHelpers();
		AlsProviderBankDetailsAS appSer = new AlsProviderBankDetailsAS();
		AlsProviderBankDetails tmp = null;
		try{
			String curBudgYear = hh.getCurrentBudgetYear();
			if(oper.equalsIgnoreCase("edit") || oper.equalsIgnoreCase("del")){
				String[] keys = id.split("_");
				AlsProviderBankDetailsIdPk tmpIdPk = new AlsProviderBankDetailsIdPk();
				tmpIdPk.setApiProviderNo(Integer.parseInt(keys[2]));
				tmpIdPk.setApbdSeqNo(Integer.parseInt(keys[1]));
				tmpIdPk.setApbdBillingTo(new Timestamp(sdf.parse(keys[0]).getTime()));
				
				tmp = appSer.findById(tmpIdPk);
			}
			if (oper.equalsIgnoreCase("add")&&validation()) {
				StringBuilder trimmedProvNo = new StringBuilder(provNo.toString());
				trimmedProvNo.deleteCharAt(2);
				AlsProviderBankDetailsIdPk tmpIdPk = new AlsProviderBankDetailsIdPk(provNo,new Timestamp(apbdBillingTo.getTime()),hh.getProvBankDetailsNextSeqNo(provNo, BillingFrom, apbdBillingTo));
				tmp = new AlsProviderBankDetails();
				tmp.setIdPk(tmpIdPk);
				tmp.setApbdBillingFrom(new Timestamp(BillingFrom.getTime()));
				tmp.setAbcBankCd(abcBankCd);
				tmp.setApbdAmountDeposit(apbdAmountDeposit);
				tmp.setApbdDepositDate(new Timestamp(depositDate.getTime()));
				tmp.setApbdDepositId(curBudgYear.substring(2, 4)+trimmedProvNo+String.format("%3s",hh.getAlsDepIdSeq(curBudgYear, "IP")).replace(" ", "0"));
				tmp.setApbdCashInd(apbdCashInd);
				
				tmp.setCreatePersonid(userInfo.getUserId());
				tmp.setApbdWhoLog(userInfo.getStateId());
				tmp.setApbdWhenLog(date);
				
				appSer.save(tmp);
			} else if((oper.equalsIgnoreCase("edit")&&validation())){				
				tmp.setApbdDepositDate(new Timestamp(depositDate.getTime()));
				tmp.setApbdCashInd(apbdCashInd);
				tmp.setApbdAmountDeposit(apbdAmountDeposit);
				tmp.setAbcBankCd(abcBankCd);
				 
				tmp.setModDate(date);
				tmp.setModPersonid(userInfo.getUserId());
				appSer.save(tmp);
			}else if (oper.equalsIgnoreCase("del")){
				String[] keys = id.split("_");
				if(hh.getDepositProviderDate(Integer.valueOf(keys[2]),  new java.sql.Date(sdf.parse(keys[0]).getTime()))){
					addActionError("Cannot delete record, Deposit already approved for this billing period.");
					return "error_json";
				}else{
					appSer.delete(tmp);
				}
			}else{
				return "error_json";
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

	private boolean validation(){
		HibHelpers hh = new HibHelpers();
		if(!hh.isValidBPTo(provNo, sdf.format(apbdBillingTo))){
			addActionError("Invalid Billing Period End Date, List available.");
		}
		if("".equals(apbdAmountDeposit) || apbdAmountDeposit <= 0){
			addActionError("Amount Deposited cannot be less than or equal to zero.");
		}
		if(depositDate.before(BillingFrom)){
			addActionError("Deposit Date cannot be less than current billing period.");
		}
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		cal.setTime(depositDate);
		int depositYear = cal.get(Calendar.YEAR);
		if(depositYear < year){
			addActionError("The Deposit Date entered is less than the current year.");
		}
		
		if(hh.getDepositApprovalFlag(provNo, sdf.format(apbdBillingTo), sdf.format(BillingFrom))){
			addActionError("Cannot add record, Deposit already approved for this billing period.");
		}
		/*Deposit dates need to be able to be changed by alsaccount_user after completed by provider*/
		/*if(hh.getDepositProviderDate(provNo, apbdBillingTo)){
			addActionError("Cannot add record, Deposit already approved for this billing period.");
		}*/
		
		if(getActionErrors().size() > 0){
			return false;
		}else{
			return true;
		}
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

	public String getAbcBankCd() {
		return abcBankCd;
	}

	public void setAbcBankCd(String abcBankCd) {
		this.abcBankCd = abcBankCd;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Double getApbdAmountDeposit() {
		return apbdAmountDeposit;
	}

	public void setApbdAmountDeposit(Double apbdAmountDeposit) {
		this.apbdAmountDeposit = apbdAmountDeposit;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	

	public Date getApbdBillingTo() {
		return apbdBillingTo;
	}

	public void setApbdBillingTo(Date apbdBillingTo) {
		this.apbdBillingTo = apbdBillingTo;
	}

	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public Double getAmtDue() {
		return amtDue;
	}

	public void setAmtDue(Double amtDue) {
		this.amtDue = amtDue;
	}

	public String getApbdDepositId() {
		return apbdDepositId;
	}

	public void setApbdDepositId(String apbdDepositId) {
		this.apbdDepositId = apbdDepositId;
	}

	public Integer getProvNo() {
		return provNo;
	}

	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}

	public AlsProviderBankDetailsIdPk getIdPk() {
		return idPk;
	}

	public void setIdPk(AlsProviderBankDetailsIdPk idPk) {
		this.idPk = idPk;
	}

	public Integer getApbdSeqNo() {
		return apbdSeqNo;
	}

	public void setApbdSeqNo(Integer apbdSeqNo) {
		this.apbdSeqNo = apbdSeqNo;
	}

	/**
	 * @return the billingFrom
	 */
	public Date getBillingFrom() {
		return BillingFrom;
	}

	/**
	 * @param billingFrom the billingFrom to set
	 */
	public void setBillingFrom(Date billingFrom) {
		BillingFrom = billingFrom;
	}

	public String getApbdCashInd() {
		return apbdCashInd;
	}

	public void setApbdCashInd(String apbdCashInd) {
		this.apbdCashInd = apbdCashInd;
	}
	
	
}
