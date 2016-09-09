package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.utils.HibHelpers;

public class SabhrsQueryGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(SabhrsQueryGridAction.class);

    private List<AlsSabhrsEntriesDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
    private Integer 			providerNo;
    private Integer 			seqNo;
    private Date 				bpFromDt;
    private Date 				bpToDt;
    private Date				fromDt;
    private Date 				toDt;
    private String 				sumAppStat;
    private String				intAppStat;
    private String				jlr;
    private String 				account;
    private String 				fund;
    private String 				org;
    private String 				subclass;
    private String 				tribeCd;
    private String 				txnGrpIdentifier;
    private Integer 			budgYear;
    private Integer				progYear;
    private String 				sysActTypeCd;
    private String 				transGrpType;
    
    private String 				userdata;

    
	public String buildgrid(){ 
    	HibHelpers hh = new HibHelpers();
        try{
        	model = hh.getSabhrsQueryRecords(providerNo, seqNo, bpFromDt, bpToDt, fromDt, 
							        		 toDt, sumAppStat, intAppStat, jlr, account,  
							        		 fund,  org,  subclass,  tribeCd, txnGrpIdentifier, 
							        		 budgYear, progYear,  sysActTypeCd, transGrpType);
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsSabhrsEntries did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());
        setTotal(1);

	    return SUCCESS;
    }

	public Integer getProviderNo() {
		return providerNo;
	}


	public void setProviderNo(Integer providerNo) {
		this.providerNo = providerNo;
	}


	public Integer getSeqNo() {
		return seqNo;
	}


	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}


	public Date getBpFromDt() {
		return bpFromDt;
	}


	public void setBpFromDt(Date bpFromDt) {
		this.bpFromDt = bpFromDt;
	}


	public Date getBpToDt() {
		return bpToDt;
	}


	public void setBpToDt(Date bpToDt) {
		this.bpToDt = bpToDt;
	}


	public Date getFromDt() {
		return fromDt;
	}


	public void setFromDt(Date fromDt) {
		this.fromDt = fromDt;
	}


	public Date getToDt() {
		return toDt;
	}


	public void setToDt(Date toDt) {
		this.toDt = toDt;
	}


	public String getSumAppStat() {
		return sumAppStat;
	}


	public void setSumAppStat(String sumAppStat) {
		this.sumAppStat = sumAppStat;
	}


	public String getIntAppStat() {
		return intAppStat;
	}


	public void setIntAppStat(String intAppStat) {
		this.intAppStat = intAppStat;
	}


	public String getJlr() {
		return jlr;
	}


	public void setJlr(String jlr) {
		this.jlr = jlr;
	}


	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getFund() {
		return fund;
	}


	public void setFund(String fund) {
		this.fund = fund;
	}


	public String getOrg() {
		return org;
	}


	public void setOrg(String org) {
		this.org = org;
	}


	public String getSubclass() {
		return subclass;
	}


	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}


	public String getTribeCd() {
		return tribeCd;
	}


	public void setTribeCd(String tribeCd) {
		this.tribeCd = tribeCd;
	}


	public Integer getProgYear() {
		return progYear;
	}


	public void setProgYear(Integer progYear) {
		this.progYear = progYear;
	}


	public String getSysActTypeCd() {
		return sysActTypeCd;
	}


	public void setSysActTypeCd(String sysActTypeCd) {
		this.sysActTypeCd = sysActTypeCd;
	}


	public String getTransGrpType() {
		return transGrpType;
	}


	public void setTransGrpType(String transGrpType) {
		this.transGrpType = transGrpType;
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

	public List<AlsSabhrsEntriesDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsSabhrsEntriesDTO> model) {
		this.model = model;
	}
    

    public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}

	public String getTxnGrpIdentifier() {
		return txnGrpIdentifier;
	}

	public void setTxnGrpIdentifier(String txnGrpIdentifier) {
		this.txnGrpIdentifier = txnGrpIdentifier;
	}
	
	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}
}
