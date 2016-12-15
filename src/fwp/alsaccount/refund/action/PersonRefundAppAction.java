package fwp.alsaccount.refund.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.utils.ListUtils;
import fwp.utils.FwpStringUtils;

public class PersonRefundAppAction extends ActionSupport{
	
	private static final long serialVersionUID = 5217638596755074369L;
	private static final Logger log = LoggerFactory.getLogger(PersonRefundAppAction.class);

	private List<ListComp> itemTypeLst;
	private List<ListComp> itemIndLst;
	private List<ListComp> reasonTypeCdLst;
	private List<ListComp> appTypeCdLst;
	private String paymentStatusLst;
	private String cancelActionLst;
	private String reconStatusLst;
	private String staledateStatusLst;
	private String paymentMethodLst;
	private String warrantTypeLst;
	
	public PersonRefundAppAction(){
	}
	
	public String input(){
		ListUtils lu = new ListUtils();
		try {
			itemTypeLst = lu.getItemTypeCd(null);
			itemIndLst = lu.getMiscCodes("ITEM INDICATOR", null, null, null, null, "amKey2", null, true, false);
			reasonTypeCdLst = lu.getMiscCodes("REFUND REASON", null, null, null, null, null, null, true, false);
			appTypeCdLst = lu.getAppTypeCd();
			//Warrant Status Grid Lists
			paymentStatusLst = FwpStringUtils.listCompListToString(lu.getMiscCodes("PAYMENT STATUS", null, null, null, null, null, null, false, false));
			cancelActionLst = FwpStringUtils.listCompListToString(lu.getMiscCodes("CANCEL ACTION", null, null, null, null, null, null, false, false));
			reconStatusLst = FwpStringUtils.listCompListToString(lu.getMiscCodes("RECON STATUS", null, null, null, null, null, null, false, false));
			staledateStatusLst = FwpStringUtils.listCompListToString(lu.getMiscCodes("STALEDATE STATUS", null, null, null, null, null, null, false, false));
			paymentMethodLst = FwpStringUtils.listCompListToString(lu.getMiscCodes("PAYMENT METHOD", null, null, null, null, null, null, false, false));
			warrantTypeLst = FwpStringUtils.listCompListToString(lu.getMiscCodes("WARRANT TYPE", null, null, null, null, null, null, false, false));
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		return SUCCESS;
	}

	public List<ListComp> getItemTypeLst() {
		return itemTypeLst;
	}

	public void setItemTypeLst(List<ListComp> itemTypeLst) {
		this.itemTypeLst = itemTypeLst;
	}

	public List<ListComp> getReasonTypeCdLst() {
		return reasonTypeCdLst;
	}

	public void setReasonTypeCdLst(List<ListComp> reasonTypeCdLst) {
		this.reasonTypeCdLst = reasonTypeCdLst;
	}

	public List<ListComp> getItemIndLst() {
		return itemIndLst;
	}

	public void setItemIndLst(List<ListComp> itemIndLst) {
		this.itemIndLst = itemIndLst;
	}

	public String getPaymentStatusLst() {
		return paymentStatusLst;
	}

	public void setPaymentStatusLst(String paymentStatusLst) {
		this.paymentStatusLst = paymentStatusLst;
	}

	public String getCancelActionLst() {
		return cancelActionLst;
	}

	public void setCancelActionLst(String cancelActionLst) {
		this.cancelActionLst = cancelActionLst;
	}

	public String getReconStatusLst() {
		return reconStatusLst;
	}

	public void setReconStatusLst(String reconStatusLst) {
		this.reconStatusLst = reconStatusLst;
	}

	public String getStaledateStatusLst() {
		return staledateStatusLst;
	}

	public void setStaledateStatusLst(String staledateStatusLst) {
		this.staledateStatusLst = staledateStatusLst;
	}

	public String getPaymentMethodLst() {
		return paymentMethodLst;
	}

	public void setPaymentMethodLst(String paymentMethodLst) {
		this.paymentMethodLst = paymentMethodLst;
	}

	public String getWarrantTypeLst() {
		return warrantTypeLst;
	}

	public void setWarrantTypeLst(String warrantTypeLst) {
		this.warrantTypeLst = warrantTypeLst;
	}

	public List<ListComp> getAppTypeCdLst() {
		return appTypeCdLst;
	}

	public void setAppTypeCdLst(List<ListComp> appTypeCdLst) {
		this.appTypeCdLst = appTypeCdLst;
	}

}
