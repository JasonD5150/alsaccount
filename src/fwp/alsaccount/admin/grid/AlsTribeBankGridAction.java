package fwp.alsaccount.admin.grid;


import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.admin.dao.AlsTribeInfo;
import fwp.alsaccount.appservice.admin.AlsTribeInfoAS;


public class AlsTribeBankGridAction extends ActionSupport{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8209282152623896627L;

	private static final Logger    log              = LoggerFactory.getLogger(AlsTribeBankGridAction.class);

    private List<AlsTribeInfo>    model;
   

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

    	AlsTribeInfoAS abcAS = new AlsTribeInfoAS();
    	
        try{
        	model = abcAS.findAll();
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsTribeInfo did not load " + re.getMessage());
        }

	    return SUCCESS;
    }

	/**
	 * @return
	 */
	public List<AlsTribeInfo> getModel() {
		return model;
	}

	/**
	 * @param model
	 */
	public void setModel(List<AlsTribeInfo> model) {
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