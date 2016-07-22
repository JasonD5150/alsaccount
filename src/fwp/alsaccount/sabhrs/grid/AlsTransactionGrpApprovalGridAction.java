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

public class AlsTransactionGrpApprovalGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsTransactionGrpApprovalGridAction.class);

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
    private String srchYear;
    private String srchDisapproval;
    private String srchProvider;
    private String srchSumDt;
    private String srchIntDt;
    private String srchDepositId;
    private String srchBankCd;
    private String srchBankReferenceNo;
    private String srchWhenUploadedToSabhrs;
    private String srchFileName;
    private String srchGrpIntentifier;

	@SuppressWarnings("unchecked")
	public String buildgrid(){  
		HibHelpers hh = new HibHelpers();
		Integer curBudgYear = Integer.parseInt(hh.getCurrentBudgetYear());
		
		String srchStr = " where 1=1 ";
		String orderStr = " ORDER BY idPk.atgTransactionCd,idPk.atgsGroupIdentifier";
		
		if(filters != null && !"".equals(filters)){
    		srchStr = buildStr(srchStr);
    	}else{
    		srchStr += " AND TO_CHAR(atgsWhenCreated,'YYYY') = "+curBudgYear +" AND atgsInterfaceStatus NOT IN ('N','D')";
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
        		
        		tmp.setAtgsAccountingDt(a.getAtgsAccountingDt());
        		
        		tmp.setAtgsSummaryStatus(a.getAtgsSummaryStatus());
        		tmp.setAtgsSummaryApprovedBy(a.getAtgsSummaryApprovedBy());
        		tmp.setAtgsSummaryDt(a.getAtgsSummaryDt());
        		tmp.setAtgsInterfaceStatus(a.getAtgsInterfaceStatus());
        		tmp.setAtgsInterfaceApprovedBy(a.getAtgsInterfaceApprovedBy());
        		tmp.setAtgsInterfaceDt(a.getAtgsInterfaceDt());
        		
        		tmp.setAtgsWhenUploadToSummary(a.getAtgsWhenUploadToSummary());
        		tmp.setAbcBankCd(a.getAbcBankCd());
        		tmp.setAtgsBankReferenceNo(a.getAtgsBankReferenceNo());
        		
        		tmp.setAtgsArGlFlag(a.getAtgsArGlFlag());
        		tmp.setAtgsFileCreationDt(a.getAtgsFileCreationDt());
        		tmp.setAtgsFileName(a.getAtgsFileName());
        		
        		tmp.setAtgsNonAlsFlag(a.getAtgsNonAlsFlag());
        		tmp.setAtgsNetDrCr(a.getAtgsNetDrCr());
        		tmp.setAtgsDepositId(a.getAtgsDepositId());
        		
        		tmp.setAtgsRemarks(a.getAtgsRemarks());
        		tmp.setAtgsWhenUploadedToSabhrs(a.getAtgsWhenUploadedToSabhrs());
        		
        		tmp.setBudgYear(hh.getTransGrpBudgYear(tmp.getIdPk().getAtgTransactionCd(), tmp.getIdPk().getAtgsGroupIdentifier()));
        		tmp.setProgramYear(hh.getTransGrpProgYear(tmp.getIdPk().getAtgTransactionCd(), tmp.getIdPk().getAtgsGroupIdentifier()));
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String buildStr(String where){
		HibHelpers hh = new HibHelpers();
    	try {
            Hashtable<String,Object> jsonFilter = (Hashtable<String, Object>) (new gov.fwp.mt.RPC.FWPJsonRpc().new JsonParser(filters)).FromJson();
            String groupOp = (String) jsonFilter.get("groupOp");
            ArrayList rules = (ArrayList) jsonFilter.get("rules");

            int rulesCount = rules.size();
            String tmpCond = "";
            
            Boolean searchCreated = true;
            Boolean searchIntStatus = true;
            Integer curBudgYear = Integer.parseInt(hh.getCurrentBudgetYear());
            
    		for (int i = 0; i < rulesCount; i++) {
    			Hashtable<String,String> rule = (Hashtable<String, String>) rules.get(i);
    			
    			String tmp = rule.get("field");
    			if("atgsWhenCreated".equals(tmp)){
    				searchCreated = false;
    			}else if("atgsInterfaceStatus".equals(tmp)){
    				searchIntStatus = false;
    			}else if("idPk.atgsGroupIdentifier".equals(tmp)){
    				searchCreated = false;
    				searchIntStatus = false;
    			}
    				
    			if("provider".equals(tmp)){
    				tmp = "TRIM(TRIM(LEADING 0 FROM substr(idPk.atgsGroupIdentifier,3,6))) ";
    			}else if("atgsWhenCreated".equals(tmp)){
    				tmp = "EXTRACT (YEAR FROM atgsWhenCreated) ";
    			}
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
    	        	if("null".equals(rule.get("data"))){
    	        		where = where + " " +tmpCond+" " + tmp + " IS NULL ";
    	        	}else{
    	        		where = where + " " +tmpCond+" " + tmp + " = '" + rule.get("data")+"'";
    	        	}
    	        	
    	        } else if (rule.get("op").equalsIgnoreCase("ne")) {
    	        	if("null".equals(rule.get("data"))){
    	        		where = where + " " +tmpCond+" " + tmp + " IS NOT NULL ";
    	        	}else{
    	        		where = where + " " +tmpCond+" " + tmp + " <> '" + rule.get("data")+"'";
    	        	}
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
    		 if(searchCreated){
    			 where = where + " AND TO_CHAR(atgsWhenCreated,'YYYY') = "+curBudgYear;
    		 }
    		 if(searchIntStatus){
    			 where = where + " AND atgsInterfaceStatus NOT IN ('N','D')";
    		 }
    		  }
    		  catch(Exception ex) {
    			  where = "Build String Error: " + ex;  
    	  }
    	
    	
        return where;
    }

	
	public String getSrchIntDt() {
		return srchIntDt;
	}


	public void setSrchIntDt(String srchIntDt) {
		this.srchIntDt = srchIntDt;
	}


	public String getSrchYear() {
		return srchYear;
	}


	public void setSrchYear(String srchYear) {
		this.srchYear = srchYear;
	}


	public String getSrchDisapproval() {
		return srchDisapproval;
	}


	public void setSrchDisapproval(String srchDisapproval) {
		this.srchDisapproval = srchDisapproval;
	}


	public String getJSON()
	{
		return buildgrid();
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

    public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}

	public List<AlsTransactionGrpStatusDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsTransactionGrpStatusDTO> model) {
		this.model = model;
	}

	public String getSrchProvider() {
		return srchProvider;
	}

	public void setSrchProvider(String srchProvider) {
		this.srchProvider = srchProvider;
	}

	public String getSrchSumDt() {
		return srchSumDt;
	}

	public void setSrchSumDt(String srchSumDt) {
		this.srchSumDt = srchSumDt;
	}
	
	public String getSrchDepositId() {
		return srchDepositId;
	}

	public void setSrchDepositId(String srchDepositId) {
		this.srchDepositId = srchDepositId;
	}

	public String getSrchBankCd() {
		return srchBankCd;
	}

	public void setSrchBankCd(String srchBankCd) {
		this.srchBankCd = srchBankCd;
	}

	public String getSrchBankReferenceNo() {
		return srchBankReferenceNo;
	}

	public void setSrchBankReferenceNo(String srchBankReferenceNo) {
		this.srchBankReferenceNo = srchBankReferenceNo;
	}

	public String getSrchWhenUploadedToSabhrs() {
		return srchWhenUploadedToSabhrs;
	}

	public void setSrchWhenUploadedToSabhrs(String srchWhenUploadedToSabhrs) {
		this.srchWhenUploadedToSabhrs = srchWhenUploadedToSabhrs;
	}

	public String getSrchFileName() {
		return srchFileName;
	}

	public void setSrchFileName(String srchFileName) {
		this.srchFileName = srchFileName;
	}
	
	public String getSrchGrpIntentifier() {
		return srchGrpIntentifier;
	}

	public void setSrchGrpIntentifier(String srchGrpIntentifier) {
		this.srchGrpIntentifier = srchGrpIntentifier;
	}

}
