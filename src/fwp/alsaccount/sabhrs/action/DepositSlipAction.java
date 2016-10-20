package fwp.alsaccount.sabhrs.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsProviderBankDepositSlipAS;
import fwp.alsaccount.dao.sabhrs.AlsProviderBankDepositSlip;

public class DepositSlipAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	byte[] imageInByte = null;
	String imageId;

	private HttpServletRequest servletRequest;

	public String execute() {
		return SUCCESS;
	}

	public byte[] getDepositSlip() {
		AlsProviderBankDepositSlipAS apbdsAS = new AlsProviderBankDepositSlipAS();
		AlsProviderBankDepositSlip apbds = apbdsAS.findById(Integer.parseInt(imageId));
		imageInByte = apbds.getApbdsData();

		return imageInByte;
	}

	public String getCustomContentType() {
		return "application/pdf";
	}

	public String getCustomContentDisposition() {
		return "tmp.pdf";
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.servletRequest = request;

	}
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

}