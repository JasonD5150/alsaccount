package fwp.alsaccount.refund.action;

import com.opensymphony.xwork2.ActionSupport;
import fwp.ListComp;
import fwp.als.appservice.draw.AlsPreappTypeAS;
import fwp.als.hibernate.draw.dao.AlsPreappType;
import fwp.alsaccount.appservice.refund.AlsRefundInfoAS;
import fwp.alsaccount.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		return ActionSupport.SUCCESS;
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

}
