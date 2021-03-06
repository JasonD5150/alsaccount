package fwp.alsaccount.admin.grid;

import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.admin.dao.AlsSysActivityTypeCodes;
import fwp.alsaccount.appservice.admin.AlsSysActivityTypeCodesAS;
import fwp.alsaccount.utils.Utils;

public class AlsSysActivityTypeCodesGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsSysActivityTypeCodesGridAction.class);

    private List<AlsSysActivityTypeCodes>    typeCodeModel;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    private Integer budgYear;

    @SuppressWarnings("unchecked")
	public String buildgrid(){
    	String srchStr = " where 1=1 ";
    	String orderStr = " order by asacSystemActivityTypeCd";
    	
    	if(filters != null && !"".equals(filters)){
    		//srchStr = buildStr(srchStr);
    		srchStr = Utils.buildStr(srchStr, filters);
    		if(srchStr.contains("Build String Error:")){
    			addActionError(srchStr);
    		}
    	}
    	
    	AlsSysActivityTypeCodesAS asac = new AlsSysActivityTypeCodesAS();
    	
        try{
        	typeCodeModel = asac.findAllByWhere(srchStr+orderStr);
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsSysActivityTypeCodes did not load " + re.getMessage());
        }
        setRows(typeCodeModel.size());
        setRecords(typeCodeModel.size());

        setTotal(1);

	    return SUCCESS;
    }


	public String getJSON()
	{
		return buildgrid();
	}


    /**
     * @return the rows
     */
    public Integer getRows() {
        return rows;
    }
    /**
     * @param rows the rows to set
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }
    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }
    /**
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }
    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }
    /**
     * @return the records
     */
    public Integer getRecords() {
        return records;
    }
    /**
     * @param records the records to set
     */
    public void setRecords(Integer records) {
        this.records = records;
    }
    /**
     * @return the sord
     */
    public String getSord() {
        return sord;
    }
    /**
     * @param sord the sord to set
     */
    public void setSord(String sord) {
        this.sord = sord;
    }
    /**
     * @return the sidx
     */
    public String getSidx() {
        return sidx;
    }
    /**
     * @param sidx the sidx to set
     */
    public void setSidx(String sidx) {
        this.sidx = sidx;
    }
    /**
     * @return the filters
     */
    public String getFilters() {
        return filters;
    }
    /**
     * @param filters the filters to set
     */
    public void setFilters(String filters) {
        this.filters = filters;
    }
    /**
     * @return the loadonce
     */
    public boolean isLoadonce() {
        return loadonce;
    }
    /**
     * @param loadonce the loadonce to set
     */
    public void setLoadonce(boolean loadonce) {
        this.loadonce = loadonce;
    }

	public List<AlsSysActivityTypeCodes> getTypeCodeModel() {
		return typeCodeModel;
	}

	public void setTypeCodeModel(List<AlsSysActivityTypeCodes> typeCodeModel) {
		this.typeCodeModel = typeCodeModel;
	}

	public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}


 
}
