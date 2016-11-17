package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;

public class ProvAdjEntPostingQueryDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(ProvAdjEntPostingQueryDivAction.class);

	private List<ListComp> provLst;
	private List<ListComp> applicationTypeLst;
	private List<ListComp> amountTypeCdLst;
	private List<ListComp> reasonTypeCdLst;
	private List<ListComp> tribeCdLst;

	public ProvAdjEntPostingQueryDivAction(){
	}

	public String input(){
		ListUtils lu = new ListUtils();
		try {
			provLst = lu.getActiveProviderList();
			applicationTypeLst = lu.getJLRList();
			amountTypeCdLst = lu.getMiscCodes("IAFA_AMOUNT_TYPE", null, null, null, null, null, null, true, false);
			reasonTypeCdLst = lu.getMiscCodes("PAE_REASON", null, null, null, null, "amDesc2", null, true, false);
			tribeCdLst =  lu.getTribeCdList();
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}

	public List<ListComp> getApplicationTypeLst() {
		return applicationTypeLst;
	}

	public void setApplicationTypeLst(List<ListComp> applicationTypeLst) {
		this.applicationTypeLst = applicationTypeLst;
	}

	public List<ListComp> getAmountTypeCdLst() {
		return amountTypeCdLst;
	}

	public void setAmountTypeCdLst(List<ListComp> amountTypeCdLst) {
		this.amountTypeCdLst = amountTypeCdLst;
	}

	public List<ListComp> getReasonTypeCdLst() {
		return reasonTypeCdLst;
	}

	public void setReasonTypeCdLst(List<ListComp> reasonTypeCdLst) {
		this.reasonTypeCdLst = reasonTypeCdLst;
	}

	public String execute(){
		return SUCCESS;
	}

	public List<ListComp> getProvLst() {
		return provLst;
	}

	public void setProvLst(List<ListComp> provLst) {
		this.provLst = provLst;
	}

	public List<ListComp> getTribeCdLst() {
		return tribeCdLst;
	}

	public void setTribeCdLst(List<ListComp> tribeCdLst) {
		this.tribeCdLst = tribeCdLst;
	}
	
	
}
