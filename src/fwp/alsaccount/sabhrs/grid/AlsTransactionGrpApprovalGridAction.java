package fwp.alsaccount.sabhrs.grid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.action.QueryParam;
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
    private Integer 			srchTransGrpType;
    private String				srchTranGrpId;
    private Integer				srchTranGrpCreated;
    private Date				srchAccDt;
    private String				srchSumAppStat;
    private Date				srchSumAppDt;
    private String				srchIntAppStat;
    private Date				srchIntAppDt;
    private Date				srchUpToSumDt;
    private String				srchBankCd;
    private String				srchBankRefNo;
    private String				srchIntFileNm;
	private String				srchDepId;
    private Integer				srchProviderNo;
    private Boolean				srchAll;
    
    private String 				userdata;
    
    private String srchStr = null;
    private List<Object> strParms = new ArrayList<Object>();
    private List<QueryParam> newStrParams = new ArrayList<QueryParam>();
    
	@SuppressWarnings("unchecked")
	public String buildgrid(){  
		HibHelpers hh = new HibHelpers();
		
    	AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
    	List<AlsTransactionGrpStatus> atgs = new ArrayList<AlsTransactionGrpStatus>();
    	
    	java.util.Date referenceDate = new java.util.Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(referenceDate); 
		c.add(Calendar.MONTH, -12);
    	
        try{
        	atgs = atgsAS.getTransactionGroupApprovalRecords(srchTransGrpType, srchTranGrpId, srchTranGrpCreated, srchAccDt, srchSumAppStat, srchSumAppDt, srchIntAppStat, srchIntAppDt, srchUpToSumDt, srchBankCd, srchBankRefNo, srchIntFileNm, srchDepId, srchProviderNo, srchAll, new Date(c.getTimeInMillis()));
        			
        	
        	if (atgs.size() > 10000) {
        		setUserdata("Please narrow search. The search grid is limited to 10000 rows. There were " + atgs.size() + " entries selected.");
        		atgs = new ArrayList<AlsTransactionGrpStatus>();
    		}else{
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
	
	
	
	public String getUserdata() {
		return userdata;
	}



	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

	public List<AlsTransactionGrpStatusDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsTransactionGrpStatusDTO> model) {
		this.model = model;
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

	public Integer getSrchTransGrpType() {
		return srchTransGrpType;
	}

	public void setSrchTransGrpType(Integer srchTransGrpType) {
		this.srchTransGrpType = srchTransGrpType;
	}

	public String getSrchTranGrpId() {
		return srchTranGrpId;
	}

	public void setSrchTranGrpId(String srchTranGrpId) {
		this.srchTranGrpId = srchTranGrpId;
	}

	public Integer getSrchTranGrpCreated() {
		return srchTranGrpCreated;
	}

	public void setSrchTranGrpCreated(Integer srchTranGrpCreated) {
		this.srchTranGrpCreated = srchTranGrpCreated;
	}

	public Date getSrchAccDt() {
		return srchAccDt;
	}

	public void setSrchAccDt(Date srchAccDt) {
		this.srchAccDt = srchAccDt;
	}

	public String getSrchSumAppStat() {
		return srchSumAppStat;
	}

	public void setSrchSumAppStat(String srchSumAppStat) {
		this.srchSumAppStat = srchSumAppStat;
	}

	public Date getSrchSumAppDt() {
		return srchSumAppDt;
	}

	public void setSrchSumAppDt(Date srchSumAppDt) {
		this.srchSumAppDt = srchSumAppDt;
	}

	public String getSrchIntAppStat() {
		return srchIntAppStat;
	}

	public void setSrchIntAppStat(String srchIntAppStat) {
		this.srchIntAppStat = srchIntAppStat;
	}

	public Date getSrchIntAppDt() {
		return srchIntAppDt;
	}

	public void setSrchIntAppDt(Date srchIntAppDt) {
		this.srchIntAppDt = srchIntAppDt;
	}

	public Date getSrchUpToSumDt() {
		return srchUpToSumDt;
	}

	public void setSrchUpToSumDt(Date srchUpToSumDt) {
		this.srchUpToSumDt = srchUpToSumDt;
	}

	public String getSrchBankCd() {
		return srchBankCd;
	}

	public void setSrchBankCd(String srchBankCd) {
		this.srchBankCd = srchBankCd;
	}

	public String getSrchBankRefNo() {
		return srchBankRefNo;
	}

	public void setSrchBankRefNo(String srchBankRefNo) {
		this.srchBankRefNo = srchBankRefNo;
	}

	public String getSrchIntFileNm() {
		return srchIntFileNm;
	}

	public void setSrchIntFileNm(String srchIntFileNm) {
		this.srchIntFileNm = srchIntFileNm;
	}

	public String getSrchDepId() {
		return srchDepId;
	}

	public void setSrchDepId(String srchDepId) {
		this.srchDepId = srchDepId;
	}

	public Integer getSrchProviderNo() {
		return srchProviderNo;
	}

	public void setSrchProviderNo(Integer srchProviderNo) {
		this.srchProviderNo = srchProviderNo;
	}

	public Boolean getSrchAll() {
		return srchAll;
	}

	public void setSrchAll(Boolean srchAll) {
		this.srchAll = srchAll;
	}
	
}
