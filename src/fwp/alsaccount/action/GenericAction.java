package fwp.alsaccount.action;

import com.opensymphony.xwork2.ActionSupport;


public class GenericAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String levelName;
    private String privRole;

	/**
	 * 
	 */
	public GenericAction(){
		
	}
	
	public String input(){
		
		//System.out.println("privRole G " + privRole );
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * @param levelName the levelName to set
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	/**
	 * @return the privRole
	 */
	public String getPrivRole() {
		return privRole;
	}

	/**
	 * @param privRole the privRole to set
	 */
	public void setPrivRole(String privRole) {
		this.privRole = privRole;
	}

}
