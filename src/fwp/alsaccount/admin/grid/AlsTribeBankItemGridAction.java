package fwp.alsaccount.admin.grid;


import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.admin.AlsTribeItemDTO;
import fwp.alsaccount.utils.HibHelpers;



public class AlsTribeBankItemGridAction extends ActionSupport{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8209282152623896627L;

	private static final Logger    log              = LoggerFactory.getLogger(AlsTribeItemDTO.class);

    private List<AlsTribeItemDTO>    model;
    
    private String tribeID;
   

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
		
		HibHelpers hibHelper = new HibHelpers();
		
		
    	/*String srchStr = " where 1=1";*/
    	/*String orderStr = " order by atiTribeCd";
    	
    	if(filters != null && !"".equals(filters)){
    		//srchStr = buildStr(srchStr);
    		srchStr = Utils.buildStr(srchStr, filters);*/
    		/*if(srchStr.contains("Build String Error:")){
    			addActionError(srchStr);
    		}*/
    	/*}
    	*/
    	/*AlsTribeItemInfoAS abcAS = new AlsTribeItemInfoAS();*/
    	
        try{
        	//model = hibHelper.findTribeBankItems(tribeID);
        	
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsTribeItem did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }
	
public String buildgridList(){    	
		
		HibHelpers hibHelper = new HibHelpers();
		
		
    	/*String srchStr = " where 1=1";*/
    	/*String orderStr = " order by atiTribeCd";
    	
    	if(filters != null && !"".equals(filters)){
    		//srchStr = buildStr(srchStr);
    		srchStr = Utils.buildStr(srchStr, filters);*/
    		/*if(srchStr.contains("Build String Error:")){
    			addActionError(srchStr);
    		}*/
    	/*}
    	*/
    	/*AlsTribeItemInfoAS abcAS = new AlsTribeItemInfoAS();*/
    	
        try{
        	//model = hibHelper.findTribeBankItems(tribeID);
        	
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsTribeItem did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }

	public List<AlsTribeItemDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsTribeItemDTO> model) {
		this.model = model;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getRecords() {
		return records;
	}

	public void setRecords(Integer records) {
		this.records = records;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public boolean isLoadonce() {
		return loadonce;
	}

	public void setLoadonce(boolean loadonce) {
		this.loadonce = loadonce;
	}

	public String getTribeID() {
		return tribeID;
	}

	public void setTribeID(String tribeID) {
		this.tribeID = tribeID;
	}

	
	
	
}