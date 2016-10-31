package fwp.alsaccount.sabhrs.action;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;


public class DepositSlipResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void execute(ActionInvocation invocation) throws Exception {

		DepositSlipAction action = (DepositSlipAction) invocation.getAction();
		HttpServletResponse response = ServletActionContext.getResponse();

		response.setContentType(action.getCustomContentType());
		response.getOutputStream().write(action.getDepositSlip());
		response.getOutputStream().flush();
	}

}
