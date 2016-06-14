package fwp.alsaccount.admin.grid;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.admin.appservice.AlsActivityAccountLinkageAS;
import fwp.alsaccount.admin.dto.AlsActivityAccountLinkageDTO;
import fwp.alsaccount.hibernate.dao.AlsActivityAccountLinkage;
import fwp.alsaccount.utils.Utils;

public class AlsActivityAccountLinkGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsActivityAccountLinkGridAction.class);

    private List<AlsActivityAccountLinkage>    model;
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
    	String srchStr = " where idPk.asacBudgetYear = "+budgYear;
    	String orderStr = " order by idPk.asacSystemActivityTypeCd,cast(idPk.asacTxnCd as int) ASC, idPk.aaalDrCrCd DESC";
    	
    	if(filters != null){
    		srchStr = buildStr(srchStr);
    	}
    	
    	AlsActivityAccountLinkageAS appSer = new AlsActivityAccountLinkageAS();
    	List<AlsActivityAccountLinkage> aaal;
        try{
        	
        	aaal = appSer.findAllByWhere(srchStr+orderStr);
        	
        	setModel(new ArrayList<AlsActivityAccountLinkage>());
        	AlsActivityAccountLinkageDTO tmp;
        	
        	for(AlsActivityAccountLinkage aaa : aaal){
        		tmp = new AlsActivityAccountLinkageDTO();
        		tmp.setIdPk(aaa.getIdPk());
        		tmp.setSysActTypeTransCd(aaa.getIdPk().getAsacSystemActivityTypeCd()+" "+aaa.getIdPk().getAsacTxnCd());
        		tmp.setAaalAccountingCdFlag(aaa.getAaalAccountingCdFlag());
        		tmp.setAaalReference(aaa.getAaalReference());
        		tmp.setAaalWhenLog(aaa.getAaalWhenLog());
        		tmp.setAaalWhoLog(aaa.getAaalWhoLog());
        		tmp.setAamAccount(aaa.getAamAccount());
        		tmp.setAamFund(aaa.getAamFund());
        		tmp.setAocOrg(aaa.getAocOrg());
        		tmp.setAsacSubclass(aaa.getAsacSubclass());
        		tmp.setGridKey(aaa.getIdPk().getAsacBudgetYear()+"_"+aaa.getIdPk().getAsacSystemActivityTypeCd()+"_"+aaa.getIdPk().getAsacTxnCd()+"_"+aaa.getIdPk().getAaalDrCrCd());
        		model.add(tmp);
        	}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsSysActivityAccountLinkage did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }

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
    			
    			if(rule.get("data").equalsIgnoreCase("yes")){
    				rule.put("data", "Y");
    			}else if(rule.get("data").equalsIgnoreCase("no")){
    				rule.put("data", "N");
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
    	        	if("sysActTypeTransCd".equals(tmp)){
    	        		String[] dataSplit = rule.get("data").split("");
    	        		String transCd = "";
    	        		for (int g = 0;g < dataSplit.length; g++){
    	        		    if(dataSplit[g].matches("[A-Za-z]")){
    	        		    	where = where + " and (asac_system_activity_type_cd = '"+dataSplit[g].toUpperCase()+"')";
    	        		    } else if (dataSplit[g].matches("[0-9]")){
    	        		    	transCd += dataSplit[g];
    	        		    }
    	        		} 
    	        		if(transCd != null){
    	        			where = where + " and (asac_txn_cd = "+transCd;
    	        		}
    	        	}else{
    	        		where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"%')";
    	        	}
    		    } else if (rule.get("op").equalsIgnoreCase("bw")) {
    		    	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('" + rule.get("data")+"%')";
    	        } else if (rule.get("op").equalsIgnoreCase("ew")) {
    	        	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"')";
    	        }			
    		}
    		where = where + ")";	
     
    		  }
    		  catch(Exception ex) {
    			  addActionError("Loading Gen Codes had an error: " + ex);		  
    	  }
          return where;
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

	public List<AlsActivityAccountLinkage> getModel() {
		return model;
	}

	public void setModel(List<AlsActivityAccountLinkage> model) {
		this.model = model;
	}

	public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}

 
}
