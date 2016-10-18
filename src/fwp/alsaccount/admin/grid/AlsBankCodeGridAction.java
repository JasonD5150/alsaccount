package fwp.alsaccount.admin.grid;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;



import fwp.alsaccount.appservice.admin.AlsBankCodeAS;
import fwp.alsaccount.appservice.admin.AlsBankCodeExtAS;
import fwp.alsaccount.dao.admin.AlsBankCode;
import fwp.alsaccount.extended.admin.AlsBankCodeEXT;
import fwp.security.user.UserDTO;
import fwp.security.utils.FwpUserUtils;
import fwp.ssr.appservice.SsrPersAS;
import fwp.ssr.hibernate.dao.SsrPers;


public class AlsBankCodeGridAction extends ActionSupport{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8209282152623896627L;

	private static final Logger    log              = LoggerFactory.getLogger(AlsBankCodeGridAction.class);

    private List<AlsBankCodeEXT>    model;
   

	private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;


	@SuppressWarnings("unchecked")
	public String buildgrid(){    	
    	AlsBankCodeAS abcAS = new AlsBankCodeAS();
    	AlsBankCodeExtAS abceAS = new AlsBankCodeExtAS();
    	SsrPersAS spAS = new SsrPersAS();
    	
    	
        try{
        	
    		
        	List<AlsBankCodeEXT> oldBankCode = new ArrayList<AlsBankCodeEXT>();
        	List<AlsBankCodeEXT> newBankCodeList = new ArrayList<AlsBankCodeEXT>();
        	//oldBankCode = abcAS.findAll();
        	
        	for (AlsBankCode oldBankCodeIterator : oldBankCode)
			{
        		AlsBankCodeEXT newBankCode = abceAS.convertAlsBankCode(oldBankCodeIterator);
        		
        		if (oldBankCodeIterator.getAbcCreatePersonid() == null){
        			newBankCode.setUpdatedUsername("UNKNOWN");
        			
        		}
        		else if (StringUtils.isNumericSpace(oldBankCodeIterator.getAbcCreatePersonid())){
        			
        			SsrPers sp = new SsrPers();
        			String userIdStr = oldBankCodeIterator.getAbcCreatePersonid();
        			Integer userIdInt = Integer.parseInt(userIdStr);
        			sp = spAS.findById(userIdInt);
        			newBankCode.setUpdatedUsername(sp.getSpFirstName() + " " + sp.getSpLastName());
        			
        			
        		}
        		else {
        			newBankCode.setUpdatedUsername(newBankCode.getAbcCreatePersonid());
        			
        		}
        		newBankCodeList.add(newBankCode);
        		
        		
			}
        	setModel(newBankCodeList);
        	
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsBankCode did not load " + re.getMessage());
            System.out.println(re.getMessage());
 			return ERROR;
        }
        return SUCCESS;
    }



	/**
	 * @return
	 */
	public List<AlsBankCodeEXT> getModel() {
		return model;
	}



	/**
	 * @param model
	 */
	public void setModel(List<AlsBankCodeEXT> model) {
		this.model = model;
	}



	/**
	 * @return
	 */
	public Integer getRows() {
		return rows;
	}

	/**
	 * @param rows
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * @return
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * @param page
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * @return
	 */
	public Integer getTotal() {
		return total;
	}

	/**
	 * @param total
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}

	/**
	 * @return
	 */
	public Integer getRecords() {
		return records;
	}

	/**
	 * @param records
	 */
	public void setRecords(Integer records) {
		this.records = records;
	}

	/**
	 * @return
	 */
	public String getSord() {
		return sord;
	}

	/**
	 * @param sord
	 */
	public void setSord(String sord) {
		this.sord = sord;
	}

	/**
	 * @return
	 */
	public String getSidx() {
		return sidx;
	}

	/**
	 * @param sidx
	 */
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	/**
	 * @return
	 */
	public String getFilters() {
		return filters;
	}

	/**
	 * @param filters
	 */
	public void setFilters(String filters) {
		this.filters = filters;
	}

	/**
	 * @return
	 */
	public boolean isLoadonce() {
		return loadonce;
	}

	/**
	 * @param loadonce
	 */
	public void setLoadonce(boolean loadonce) {
		this.loadonce = loadonce;
	}

	
	
	
}