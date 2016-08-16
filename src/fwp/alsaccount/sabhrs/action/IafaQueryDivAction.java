package fwp.alsaccount.sabhrs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.ListUtils;

public class IafaQueryDivAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(IafaQueryDivAction.class);

	private List<ListComp> provLst;
	private List<ListComp> tribeCdLst;
	private List<ListComp> applicationTypeLst;
	private List<ListComp> itemIndicatorLst;
	private List<ListComp> itemTypeLst;
	private List<ListComp> itemStatusLst;
	private List<ListComp> itemCategoryCdLst;
	private List<ListComp> applicationDispositionLst;
	private List<ListComp> amountTypeCdLst;
	private List<ListComp> costPrerequisiteCdLst;
	private List<ListComp> processCategoryCdLst;
	private List<ListComp> processTypeCdLst;
	private List<ListComp> reasonTypeCdLst;

	private String budgYear;

	public IafaQueryDivAction(){
	}
	
	public String input(){
		ListUtils lu = new ListUtils();
		HibHelpers hh = new HibHelpers();
		if(budgYear == null || "".equals(budgYear)){
			budgYear = hh.getCurrentBudgetYear();
		}
		try {
			provLst = lu.getIafaQueryProviderList();
			tribeCdLst = lu.getTribeCdList();
			applicationTypeLst = lu.getIafaApplicationTypeList();
			itemIndicatorLst = lu.getMiscCodes("ITEM INDICATOR", null, null, null, null, null, null, true, true);
			itemStatusLst = lu.getMiscCodes("ITEM STATUS", null, null, null, null, null, null, true, true);
			itemCategoryCdLst = lu.getItemCategoryList();
			itemTypeLst = lu.getItemTypeCd(null);
			applicationDispositionLst = lu.getMiscCodes("APPLICATION DISPOSITION", null, null, null, null, null, null, true, true);
			amountTypeCdLst = lu.getMiscCodes("IAFA_AMOUNT_TYPE", null, null, null, null, null, null, true, true);
			costPrerequisiteCdLst = lu.getIafaPrerequisiteList();
			processCategoryCdLst = lu.getMiscCodes("PROCESS_CATEGORY", null, null, null, null, null, null, true, true);
			processTypeCdLst = lu.getMiscCodes("PROCESS_TYPE", null, null, null, null, null, null, true, true);
			reasonTypeCdLst = lu.getMiscCodes("PAE_REASON", null, null, null, null, null, null, true, true);
		
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}
	
	public List<ListComp> getTribeCdLst() {
		return tribeCdLst;
	}

	public void setTribeCdLst(List<ListComp> tribeCdLst) {
		this.tribeCdLst = tribeCdLst;
	}

	public List<ListComp> getApplicationTypeLst() {
		return applicationTypeLst;
	}

	public void setApplicationTypeLst(List<ListComp> applicationTypeLst) {
		this.applicationTypeLst = applicationTypeLst;
	}

	public List<ListComp> getItemIndicatorLst() {
		return itemIndicatorLst;
	}

	public void setItemIndicatorLst(List<ListComp> itemIndicatorLst) {
		this.itemIndicatorLst = itemIndicatorLst;
	}

	public List<ListComp> getItemStatusLst() {
		return itemStatusLst;
	}

	public void setItemStatusLst(List<ListComp> itemStatusLst) {
		this.itemStatusLst = itemStatusLst;
	}

	public List<ListComp> getItemCategoryCdLst() {
		return itemCategoryCdLst;
	}

	public void setItemCategoryCdLst(List<ListComp> itemCategoryCdLst) {
		this.itemCategoryCdLst = itemCategoryCdLst;
	}

	public List<ListComp> getApplicationDispositionLst() {
		return applicationDispositionLst;
	}

	public void setApplicationDispositionLst(
			List<ListComp> applicationDispositionLst) {
		this.applicationDispositionLst = applicationDispositionLst;
	}

	public List<ListComp> getAmountTypeCdLst() {
		return amountTypeCdLst;
	}

	public void setAmountTypeCdLst(List<ListComp> amountTypeCdLst) {
		this.amountTypeCdLst = amountTypeCdLst;
	}

	public List<ListComp> getCostPrerequisiteCdLst() {
		return costPrerequisiteCdLst;
	}

	public void setCostPrerequisiteCdLst(List<ListComp> costPrerequisiteCdLst) {
		this.costPrerequisiteCdLst = costPrerequisiteCdLst;
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

	public String getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(String budgYear) {
		this.budgYear = budgYear;
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
