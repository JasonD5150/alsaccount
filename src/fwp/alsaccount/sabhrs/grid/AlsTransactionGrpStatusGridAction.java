package fwp.alsaccount.sabhrs.grid;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.dto.sabhrs.AlsTransactionGrpStatusDTO;
import fwp.alsaccount.utils.HibHelpers;

public class AlsTransactionGrpStatusGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsTransactionGrpStatusGridAction.class);

    private List<AlsTransactionGrpStatusDTO>    model;
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
		HibHelpers hh = new HibHelpers();
		Integer curBudgYear = Integer.parseInt(hh.getCurrentBudgetYear());
    	String srchStr = " WHERE atgsFileCreationDt IS NULL"
    				   + " AND  NVL(atgsSummaryStatus,'X')<>'N'"
    				   + "AND ((TO_CHAR(atgsWhenCreated,'YYYY')= "+curBudgYear+") "
    				   + "  or (TO_CHAR(atgsWhenCreated,'YYYY')= "+(curBudgYear+1)+")"
    				   + "  or (TO_CHAR(atgsWhenCreated,'YYYY')= "+(curBudgYear-1)+"))";
    	String orderStr = " ORDER BY idPk.atgTransactionCd,idPk.atgsGroupIdentifier";
    	
    	if(filters != null && !"".equals(filters)){
    		srchStr = buildStr(srchStr);
    	}
    	
    	AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
    	List<AlsTransactionGrpStatus> atgs = new ArrayList<AlsTransactionGrpStatus>();
    	
        try{
        	atgs = atgsAS.findAllByWhere(srchStr+orderStr);
        	
        	setModel(new ArrayList<AlsTransactionGrpStatusDTO>());
        	AlsTransactionGrpStatusDTO tmp;
        	
        	for(AlsTransactionGrpStatus a : atgs){
        		tmp = new AlsTransactionGrpStatusDTO();
        		tmp.setGridKey(a.getIdPk().getAtgTransactionCd()+"_"+a.getIdPk().getAtgsGroupIdentifier());
        		tmp.setIdPk(a.getIdPk());
        		tmp.setDesc(atgsAS.getTransDesc(a.getIdPk().getAtgTransactionCd()));
        		tmp.setAtgsWhenCreated(a.getAtgsWhenCreated());
        		tmp.setAtgsNetDrCr(a.getAtgsNetDrCr());
        		model.add(tmp);
        	}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsTransactionGroup did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }

	
	public String getJSON()
	{
		return buildgrid();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String buildStr(String where){
    	try {
            Hashtable<String,Object> jsonFilter = (Hashtable<String, Object>) (new gov.fwp.mt.RPC.FWPJsonRpc().new JsonParser(filters)).FromJson();
            String groupOp = (String) jsonFilter.get("groupOp");
            ArrayList rules = (ArrayList) jsonFilter.get("rules");

            int rulesCount = rules.size();
            String tmpCond = "";
            
    		for (int i = 0; i < rulesCount; i++) {
    			Hashtable<String,String> rule = (Hashtable<String, String>) rules.get(i);
    			
    			String tmp = rule.get("field");
    			
    			if (i == 0) {
    				tmpCond = "and (";
    			} else {
    				tmpCond = groupOp;
    			}
    			
    			if("atgsWhenCreated".equals(tmp)){
	        		tmp = "TO_CHAR("+tmp+",'YYYY')";
    			}
    			
    			
    	        if (rule.get("op").equalsIgnoreCase("eq")) {		    	  
    	        	where = where + " " +tmpCond+" " + tmp + " = '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("ne")) {
    	        	where = where + " " +tmpCond+" " + tmp + " <> '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("lt")) {
    	        	where = where + " " +tmpCond+" " + tmp + " < '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("gt")) {
    	        	where = where + " " +tmpCond+" " + tmp + " > '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("cn")) {
    	        	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"%')";
    		    } else if (rule.get("op").equalsIgnoreCase("bw")) {
    		    	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('" + rule.get("data")+"%')";
    	        } else if (rule.get("op").equalsIgnoreCase("ew")) {
    	        	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"')";
    	        }			
    		}
    		 where = where + ")";	
     
    		  }
    		  catch(Exception ex) {
    			  where = "Build String Error: " + ex;  
    	  }
          return where;
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

    public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}


	/**
	 * @return the model
	 */
	public List<AlsTransactionGrpStatusDTO> getModel() {
		return model;
	}


	/**
	 * @param model the model to set
	 */
	public void setModel(List<AlsTransactionGrpStatusDTO> model) {
		this.model = model;
	}
	
	

}
