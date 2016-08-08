package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.hibernate.utils.ListComp;
import fwp.alsaccount.utils.ListUtils;

public class TransGrpAppQueryDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(TransGrpAppQueryDivAction.class);


	private List<ListComp> transGroupTypeLst;
	private List<ListComp> providerLst;
	private List<ListComp> bankCdLst;
	

	public TransGrpAppQueryDivAction(){
	}
	
	public String input(){
		ListUtils lu = new ListUtils();
		try {
			providerLst = lu.getProviderList();
			transGroupTypeLst = lu.getSabhrsTransGroupTypeLst();
			bankCdLst = lu.getTransGrpBankCodeList();
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	public List<ListComp> getProviderLst() {
		return providerLst;
	}

	public void setProviderLst(List<ListComp> providerLst) {
		this.providerLst = providerLst;
	}

	public List<ListComp> getTransGroupTypeLst() {
		return transGroupTypeLst;
	}

	public void setTransGroupTypeLst(List<ListComp> transGroupTypeLst) {
		this.transGroupTypeLst = transGroupTypeLst;
	}

	/**
	 * @return the bankCdLst
	 */
	public List<ListComp> getBankCdLst() {
		return bankCdLst;
	}

	/**
	 * @param bankCdLst the bankCdLst to set
	 */
	public void setBankCdLst(List<ListComp> bankCdLst) {
		this.bankCdLst = bankCdLst;
	}

}
