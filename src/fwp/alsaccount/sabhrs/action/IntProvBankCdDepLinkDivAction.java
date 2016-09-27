package fwp.alsaccount.sabhrs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.ListUtils;
import fwp.utils.FwpStringUtils;

public class IntProvBankCdDepLinkDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(IntProvBankCdDepLinkDivAction.class);

	private Integer provNo;
	private String bankCdLst;
	
	public IntProvBankCdDepLinkDivAction(){
	}
	
	public String input(){
		ListUtils lu = new ListUtils();
		if(provNo != null){
			bankCdLst = FwpStringUtils.listCompListToString(lu.getProviderBankCodeList(provNo));
		}
		
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}
	
	public Integer getProvNo() {
		return provNo;
	}
	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}
	public String getBankCdLst() {
		return bankCdLst;
	}
	public void setBankCdLst(String bankCdLst) {
		this.bankCdLst = bankCdLst;
	}
}
