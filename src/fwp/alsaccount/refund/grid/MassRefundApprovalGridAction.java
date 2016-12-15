package fwp.alsaccount.refund.grid;

import com.opensymphony.xwork2.ActionSupport;
import fwp.alsaccount.appservice.refund.AlsRefundInfoAS;
import fwp.alsaccount.dao.refund.AlsPersonItemTemplLink;
import fwp.alsaccount.dto.refund.AlsPersonItemTemplLinkDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class MassRefundApprovalGridAction extends ActionSupport {
	private static final long serialVersionUID = -7314337529290307873L;

	private static final Logger log = LoggerFactory.getLogger(MassRefundApprovalGridAction.class);

	private Integer aptAppTypeCd;
	private List<AlsPersonItemTemplLinkDTO> model;
	private Integer applicationTypeCode;
	private String dispositionCode;
	private String refundReasonCode;


	public String buildgrid() throws Exception {
		model = new AlsRefundInfoAS().findMassRefundPersonItemList(aptAppTypeCd);
		return SUCCESS;
	}

	public Integer getAptAppTypeCd() {
		return aptAppTypeCd;
	}

	public void setAptAppTypeCd(Integer aptAppTypeCd) {
		this.aptAppTypeCd = aptAppTypeCd;
	}

	public List<AlsPersonItemTemplLinkDTO> getModel() {
		return model;
	}

	public Integer getApplicationTypeCode() {
		return applicationTypeCode;
	}

	public void setApplicationTypeCode(Integer applicationTypeCode) {
		this.applicationTypeCode = applicationTypeCode;
	}

	public String getDispositionCode() {
		return dispositionCode;
	}

	public void setDispositionCode(String dispositionCode) {
		this.dispositionCode = dispositionCode;
	}

	public String getRefundReasonCode() {
		return refundReasonCode;
	}

	public void setRefundReasonCode(String refundReasonCode) {
		this.refundReasonCode = refundReasonCode;
	}
}
