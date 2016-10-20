package fwp.alsaccount.sabhrs.action;

import com.opensymphony.xwork2.ActionSupport;


public class DepositSlipDlgAction extends ActionSupport {

	private static final long serialVersionUID = -7443755454289955788L;

	private String apbdId;
	private String remittanceId;
    
	public String input() {
		return SUCCESS;
	}
	
	public String execute() {
		return SUCCESS;
	}

	public String getApbdId() {
		return apbdId;
	}

	public void setApbdId(String apbdId) {
		this.apbdId = apbdId;
	}

	public String getRemittanceId() {
		return remittanceId;
	}

	public void setRemittanceId(String remittanceId) {
		this.remittanceId = remittanceId;
	}
}
