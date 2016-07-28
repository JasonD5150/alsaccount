package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.utils.HibHelpers;

public class AlsSabhrsQueryGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsSabhrsQueryGridAction.class);

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
    
    private Boolean 			search = false;
    
	public String buildgrid(){ 
    	HibHelpers hh = new HibHelpers();
    	String queryStr = buildQueryStr();
        try{
        	setModel(new ArrayList<AlsSabhrsEntriesDTO>());
        	if (search) {
        		model = hh.getSabhrsQueryRecords(queryStr);
    		}
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

	private String buildQueryStr(){
		StringBuilder srchStr = new StringBuilder();
		/*SELECT*/
		srchStr.append("SELECT ase.ase_When_Entry_Posted aseWhenEntryPosted, "
				+ "ase.ase_Seq_No aseSeqNo, "
				+ "ase.ase_Dr_Cr_Cd aseDrCrCd, "
				+ "ase.ase_Txn_Cd_Seq_No aseTxnCdSeqNo, "
				+ "ase.asac_Budget_Year asacBudgetYear, "
				+ "ase.asac_System_Activity_Type_Cd asacSystemActivityTypeCd, "
				+ "ase.asac_Txn_Cd asacTxnCd, " 
				+ "ase.aam_Account aamAccount, "
				+ "ase.aam_Business_Unit aamBusinessUnit, "
				+ "ase.aam_Fund aamFund, "
				+ "ase.aoc_Org aocOrg, "
				+ "ase.asac_Program asacProgram, "
				+ "ase.asac_Subclass asacSubclass, "
				+ "ase.asac_Project_Grant asacProjectGrant, "
				+ "(SELECT DISTINCT AM_VAL_DESC||SUBSTR(ase.asac_Reference,-2) rtn "
							+ "FROM ALS.ALS_MISC "
							+ "WHERE AM_KEY1 ='JOURNAL_LINE_REFERENCE' "
							+ "AND LTRIM(AM_PAR_VAL,'0')=LTRIM(SUBSTR(LPAD(ase.asac_Reference,5,'0'),1,3),'0') "
							+ "AND ROWNUM <2) jlr, "
				+ "ase.ase_Amt aseAmt, "
				+ "ase.ase_Allow_Upload_To_Summary aseAllowUploadToSummary, "
				+ "ase.ase_When_Uploaded_To_Summary upToSummDt, "
				+ "ase.ases_Seq_No asesSeqNo, "
				+ "ase.api_Provider_No apiProviderNo, "
				+ "ase.apr_Billing_From bpFromDt, "
				+ "ase.apr_Billing_To bpToDt, "
				+ "ase.aiafa_Seq_No aiafaSeqNo, "
				+ "ase.ase_Who_Log aseWhoLog, "
				+ "ase.ase_When_Log aseWhenLog, "
				+ "ase.ase_When_Uploaded_To_Summ aseWhenUploadedToSumm, "
				+ "ase.atg_Transaction_Cd atgTransactionCd, "
				+ "ase.atgs_Group_Identifier atgsGroupIdentifier, "
				+ "ase.ase_Non_Als_Flag aseNonAlsFlag, "
				+ "ase.ase_Line_Description aseLineDescription, "
				+ "ase.ati_Tribe_Cd atiTribeCd, "
				+ "ase.anat_Cd anatCd, "
				+ "TO_CHAR(ase.ase_When_Entry_Posted, 'yyyy-mm-dd hh:mm:ss')||'_'||ase.ase_Seq_No||'_'||ase.ase_Dr_Cr_Cd||'_'||ase.ase_Txn_Cd_Seq_No gridKey, "
				+ "(SELECT ATGS_SUMMARY_STATUS rtn "
					+ "FROM ALS.ALS_TRANSACTION_GRP_STATUS "
					+ "WHERE ATG_TRANSACTION_CD = ase.atg_Transaction_Cd "
					+ "AND ATGS_GROUP_IDENTIFIER = ase.atgs_Group_Identifier) sumStat, "
				+ "(SELECT ATGS_INTERFACE_STATUS rtn "
					+ "FROM ALS.ALS_TRANSACTION_GRP_STATUS "
					+ "WHERE ATG_TRANSACTION_CD = ase.atg_Transaction_Cd "
					+ "AND ATGS_GROUP_IDENTIFIER = ase.atgs_Group_Identifier) intStat ");
		
		/*FROM*/
		srchStr.append("FROM ALS.ALS_SABHRS_ENTRIES ase ");
		if((sumAppStat != null && !"-1".equals(sumAppStat)) || (intAppStat != null && !"-1".equals(intAppStat))){
			srchStr.append(", ALS.ALS_TRANSACTION_GRP_STATUS atgs ");
		}
		/*WHERE*/
    	srchStr.append("WHERE 1=1 ");
    	
    	if(fromDt != null){
    		srchStr.append("AND ase.ase_When_Entry_Posted >= TO_DATE('"+fromDt+"','YYYY-MM-DD') ");
    		search = true;
    	}
    	if(toDt != null){
    		srchStr.append("AND ase.ase_When_Entry_Posted <= TO_DATE('"+toDt+"','YYYY-MM-DD') ");
    		search = true;
    	}
    	if(bpFromDt != null){
    		srchStr.append("AND ase.apr_Billing_From = TO_DATE('"+bpFromDt+"','YYYY-MM-DD') ");
    		search = true;
    	}
    	if(bpToDt != null){
    		srchStr.append("AND ase.apr_Billing_To = TO_DATE('"+bpToDt+"','YYYY-MM-DD') ");
    		search = true;
    	}
    	if(providerNo != null){
    		srchStr.append("AND ase.api_Provider_No = "+providerNo+" ");
    		search = true;
    	}
    	if(seqNo != null && providerNo != null && bpFromDt != null && bpToDt != null){
    		srchStr.append("AND ase.aiafa_Seq_No = "+seqNo+" ");
    		search = true;
    	}
    	if(jlr != null && !"".equals(jlr)){
    		srchStr.append("AND ase.ASAC_REFERENCE = (SELECT AM_PAR_VAL||SUBSTR('"+jlr+"',-2) "
    										   + "FROM ALS.ALS_MISC WHERE AM_KEY1 = 'JOURNAL_LINE_REFERENCE' "
    										   + "AND LPAD(AM_VAL_DESC,28,'0') = SUBSTR(LPAD('"+jlr+"',30,'0'),1,28) "
    										   + "AND ROWNUM <2) ");
    		search = true;
    	}
    	if(account != null && !"".equals(account)){
    		srchStr.append("AND ase.aam_Account = '"+account+"' ");
    		search = true;
    	}
    	if(fund != null && !"".equals(fund)){
    		srchStr.append("AND ase.aam_Fund = "+fund+" ");
    		search = true;
    	}
    	if(org != null && !"".equals(org)){
    		srchStr.append("AND ase.aoc_Org = "+org+" ");
    		search = true;
    	}
    	if(subclass != null && !"".equals(subclass)){
    		srchStr.append("AND ase.asac_Subclass = "+subclass+" ");
    		search = true;
    	}
    	if(tribeCd != null && !"".equals(tribeCd)){
    		srchStr.append("AND ase.ati_Tribe_Cd = '"+tribeCd+"' ");
    		search = true;
    	}
    	if(txnGrpIdentifier != null && !"".equals(txnGrpIdentifier)){
    		srchStr.append("AND ase.atgs_Group_Identifier = '"+txnGrpIdentifier+"' ");
    		search = true;
    	}
    	if(budgYear != null && !"".equals(budgYear)){
    		srchStr.append("AND ase.asac_Budget_Year = "+budgYear+" ");
    		search = true;
    	}
    	if(progYear != null && !"".equals(progYear)){
    		srchStr.append("AND ase.asac_Program = "+progYear+" ");
    		search = true;
    	}
    	if(sysActTypeCd != null && !"".equals(sysActTypeCd)){
    		srchStr.append("AND ase.asac_System_Activity_Type_Cd||asac_Txn_Cd = '"+sysActTypeCd.toUpperCase()+"' ");
    		search = true;
    	}
    	if(transGrpType != null && !"".equals(transGrpType)){
    		srchStr.append("AND ase.atg_Transaction_Cd = "+transGrpType+" ");
    		search = true;
    	}
    	if((sumAppStat != null && !"-1".equals(sumAppStat)) || (intAppStat != null && !"-1".equals(intAppStat))){
    		srchStr.append("AND ase.atg_transaction_cd = atgs.atg_transaction_cd "
    					 + "AND ase.atgs_group_identifier = atgs.atgs_group_identifier ");
    		search = true;
    	}
    	if(sumAppStat != null && !"-1".equals(sumAppStat)){
    		srchStr.append("AND atgs.atgs_summary_status = '"+sumAppStat+"' ");
    		search = true;
    	}
    	if(intAppStat != null && !"-1".equals(intAppStat)){
    		srchStr.append("AND atgs.atgs_interface_status = '"+intAppStat+"' ");
    		search = true;
    	}
		return srchStr.toString();
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

}
