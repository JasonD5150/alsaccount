package fwp.alsaccount.refund.action;

import com.opensymphony.xwork2.ActionSupport;
import fwp.ListComp;
import fwp.alsaccount.appservice.refund.AlsRefundInfoAS;
import fwp.alsaccount.dto.refund.AlsPersonItemTemplLinkDTO;
import fwp.alsaccount.utils.ListUtils;
import fwp.security.user.UserDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jljdavidson on 12/15/16.
 */
public class MassRefundApproval extends ActionSupport {
	private static final long serialVersionUID = 2934760737704835921L;
	private static final Logger log = LoggerFactory.getLogger(MassRefundApproval.class);

	private List<ListComp> preappTypeList;
	private List<ListComp> dispositionList;
	private List<ListComp> refundReasonList;
	private List<AlsPersonItemTemplLinkDTO> selectedRefundList = new ArrayList<>();
	private Integer applicationTypeCode;
	private String dispositionCode;
	private String refundReasonCode;

	private void buildLists() throws Exception {
		AlsRefundInfoAS alsRefundInfoAS = new AlsRefundInfoAS();
		ListUtils lu = new ListUtils();
		preappTypeList = alsRefundInfoAS.findMassRefundPreAppTypes();
		dispositionList = lu.getMiscCodes("APPLICATION DISPOSITION", null, "1", null, null, null, null, false, true);
		refundReasonList = lu.getMiscCodes("REFUND REASON", null, "1", null, null, null, null, false, true);
	}
	@Override
	public String input() throws Exception {
		buildLists();
		//TODO which roles are needed for this page?
		return ActionSupport.SUCCESS;
	}

	@Override
	public String execute() throws Exception {
		if (!isValid()) {
			return ActionSupport.ERROR;
		} else {
			Integer result = new AlsRefundInfoAS().saveMassApproval(selectedRefundList, applicationTypeCode, refundReasonCode, user());
			if (result == 0) {
				addActionError(" Data not available for Mass Refund Approval for the given criteria.");
				return ActionSupport.ERROR;
			}
		}
		return ActionSupport.SUCCESS;
	}

	private String user() {
		UserDTO userInfo = (UserDTO) SecurityUtils.getSubject().getSession().getAttribute("userInfo");

		return String.valueOf(userInfo.getStateId());

	}
	private Boolean isValid() throws Exception {
		ListUtils lu = new ListUtils();
		List<ListComp> mispayList = lu.getMiscCodes("REFUND REASON", "APPLICATION MISPAY", null, null, null, null, null, false, false);
		List<ListComp> smithRiverPrivateList = lu.getMiscCodes("SMITH RIVER", "SMITH RIVER PRIVATE", null, null, null, null, null, false, false);
		List<ListComp> smithRiverOutfitterList = lu.getMiscCodes("SMITH RIVER", "SMITH RIVER OUTFITTER", null, null, null, null, null, false, false);
		List<ListComp> licenseYear = lu.getMiscCodes("LICENSE_YEAR", null, null, null, null, null, null, false, false);

		if (mispayList == null || mispayList.isEmpty()) {
			addActionError("Refund Reason Application Mispay is not configured in the misc table.");
		}
		if (smithRiverPrivateList == null || smithRiverPrivateList.isEmpty()) {
			addActionError("Smith River / Private is not configured in the misc table.");
		}
		if (smithRiverOutfitterList == null || smithRiverOutfitterList.isEmpty()) {
			addActionError("Smith River / Outfitter is not configured in the misc table.");
		}
		if (licenseYear == null || licenseYear.isEmpty()) {
			addActionError("License Year is not configured in the misc table.");
		}

		if (applicationTypeCode == null || applicationTypeCode <= 0) {
			addActionError("Application Type is required.");
		}
		if (StringUtils.isEmpty(dispositionCode)) {
			addActionError("Disposition Code is required.");
		}
		if (StringUtils.isEmpty(refundReasonCode)) {
			addActionError("Refund Reason Code is required.");
		}

		if (StringUtils.isNotBlank(dispositionCode) && StringUtils.equals(refundReasonCode, mispayList.get(0).getItemVal())) {
			addActionError("Disposition should not be selected for Application Mispay refunds.");
		}

		if (selectedRefundList.isEmpty()) {
			addActionError("At least 1 item must be selected.");
		}

		if (!StringUtils.equals(dispositionCode, "REFUND/PENDING")) {
			addActionError("Refund can only be approved for Disposition REFUND/PENDING.");
		}

		if (StringUtils.equals(refundReasonCode, "WITHDRAW")) {
			addActionError("Refund cannot be approved for Refund Reason WITHDRAW.");
		}

		return hasActionErrors();
	}

	public List<ListComp> getPreappTypeList() {
		return preappTypeList;
	}

	public List<ListComp> getDispositionList() {
		return dispositionList;
	}

	public List<ListComp> getRefundReasonList() {
		return refundReasonList;
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
