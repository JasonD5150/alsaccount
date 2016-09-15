package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;

public class IafaSummaryQueryDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(IafaSummaryQueryDivAction.class);

	private List<ListComp> provLst;
	private List<ListComp> applicationTypeLst;
	private List<ListComp> itemTypeLst;
	private List<ListComp> amountTypeCdLst;
	private List<ListComp> processCategoryCdLst;
	private List<ListComp> processTypeCdLst;
	private List<ListComp> reasonTypeCdLst;

	public IafaSummaryQueryDivAction(){
	}

	public String input(){
		ListUtils lu = new ListUtils();
		try {
			provLst = lu.getIafaQueryProviderList();
			applicationTypeLst = lu.getJLRList();
			itemTypeLst = lu.getItemTypeCd(null);
			amountTypeCdLst = lu.getMiscCodes("IAFA_AMOUNT_TYPE", null, null, null, null, null, null, true, true);
			processCategoryCdLst = lu.getMiscCodes("PROCESS_CATEGORY", null, null, null, null, null, null, true, true);
			processTypeCdLst = lu.getIafaSummaryProcessTypeList();
			reasonTypeCdLst = lu.getMiscCodes("PAE_REASON", null, null, null, null, null, null, true, true);
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

	public List<ListComp> getProcessCategoryCdLst() {
		return processCategoryCdLst;
	}

	public void setProcessCategoryCdLst(List<ListComp> processCategoryCdLst) {
		this.processCategoryCdLst = processCategoryCdLst;
	}

	public List<ListComp> getProcessTypeCdLst() {
		return processTypeCdLst;
	}

	public void setProcessTypeCdLst(List<ListComp> processTypeCdLst) {
		this.processTypeCdLst = processTypeCdLst;
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

	public List<ListComp> getItemTypeLst() {
		return itemTypeLst;
	}

	public void setItemTypeLst(List<ListComp> itemTypeLst) {
		this.itemTypeLst = itemTypeLst;
	}

	public List<ListComp> getProvLst() {
		return provLst;
	}

	public void setProvLst(List<ListComp> provLst) {
		this.provLst = provLst;
	}
}
