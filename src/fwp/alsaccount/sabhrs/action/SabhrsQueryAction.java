package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.hibernate.utils.ListComp;
import fwp.alsaccount.utils.ListUtils;

public class SabhrsQueryAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(SabhrsQueryAction.class);

	private List<ListComp> providerLst;
	private List<ListComp> jlrLst;
	/*private List<ListComp> accountLst;*/
	private List<ListComp> transGroupTypeLst;


	public SabhrsQueryAction(){
	}
	
	public String input(){
		ListUtils lu = new ListUtils();
		try {
			providerLst = lu.getProviderList();
			jlrLst = lu.getJLRList();
			/*accountLst = lu.getSabhrsQueryAccountLst();*/
			transGroupTypeLst = lu.getSabhrsTransGroupTypeLst();
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

	public List<ListComp> getJlrLst() {
		return jlrLst;
	}

	public void setJlrLst(List<ListComp> jlrLst) {
		this.jlrLst = jlrLst;
	}

	/*public List<ListComp> getAccountLst() {
		return accountLst;
	}

	public void setAccountLst(List<ListComp> accountLst) {
		this.accountLst = accountLst;
	}*/
	
	public List<ListComp> getTransGroupTypeLst() {
		return transGroupTypeLst;
	}

	public void setTransGroupTypeLst(List<ListComp> transGroupTypeLst) {
		this.transGroupTypeLst = transGroupTypeLst;
	}

}
