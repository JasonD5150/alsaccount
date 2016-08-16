package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.AlsTransactionGrpMassCopyDTO;
import fwp.alsaccount.utils.HibHelpers;

public class AlsTransactionGrpMassApprovalGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsTransactionGrpMassApprovalGridAction.class);

    private List<AlsTransactionGrpMassCopyDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    private Date				bpe;
    private Date 				opa;

	public String buildgrid(){  
		HibHelpers hh = new HibHelpers();
    	String queryStr = buildQueryStr();
        try{        	
        	setModel(new ArrayList<AlsTransactionGrpMassCopyDTO>());
        	model = hh.getTransGroupMassApprovalRecords(queryStr);
        	for(AlsTransactionGrpMassCopyDTO tmp : model){
        		tmp.setGridKey(tmp.getProviderNo()+"_"+tmp.getBpe());
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
	
	private String buildQueryStr(){
		StringBuilder srchStr = new StringBuilder();
		srchStr.append("SELECT SUBSTR(a.atgs_group_identifier, 2, 6) providerNo, "
							   + "TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10), 'YYYY/MM/DD') bpe, "
							   + "SUM(NVL(a.atgs_net_dr_cr, 0)) atgsNetDrCr,"
							   + "(SELECT api.api_business_nm FROM als_provider_info api WHERE api.api_provider_no = TRIM(SUBSTR(a.atgs_group_identifier, 2, 6))) providerName,"
							   + "(SELECT DECODE(NVL(Apr_Remitt_Per_Status,'N'),'D','Delinquent',"
							   												 + "'O','Off-line Payment Due',"
							   												 + "'OP','Off-line Payment Pending',"
							   												 + "'P','PAE Generated',"
							   												 + "'I','Investigation',"
							   												 + "'C','Collected Outside of ALS',"
							   												 + "'None')"
							   + "FROM ALS.Als_Provider_Remittance "
							   + "WHERE  Api_Provider_No = TO_NUMBER(SUBSTR(a.atgs_group_identifier, 2, 6))"
							   + "AND  Apr_Billing_To = TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10), 'YYYY/MM/DD')) remPerStat "
					    + "FROM ALS.Als_Transaction_Grp_status A "
					    + "WHERE a.atg_transaction_cd = 8 "
					    + "AND a.atgs_interface_status IS NULL "
					    + "AND LENGTH(a.atgs_group_identifier) = 22 ");
			if(bpe != null){
				srchStr.append("AND TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10),'YYYY/MM/DD') = TO_DATE('"+bpe+"','YYYY/MM/DD') ");
			}else{
				srchStr.append("AND SUBSTR(a.atgs_group_identifier, 9, 10) = SUBSTR(a.atgs_group_identifier, 9, 10) ");
			}
			srchStr.append("AND EXISTS (SELECT 1 "
					    		    + "FROM als.als_internal_remittance b "
					    		    + "WHERE b.api_provider_no = TO_NUMBER(SUBSTR(a.atgs_group_identifier, 2, 6)) "
					    		    + "AND b.air_billing_to = TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10), 'YYYY/MM/DD') "
					    		    + "AND b.air_offln_payment_approved = 'Y' "
					    		    + "AND b.air_offln_payment_app_dt = ");
			if(opa != null){
				srchStr.append(opa+") ");
			}else{
				srchStr.append("b.air_offln_payment_app_dt) ");
			}
			srchStr.append("GROUP BY "
					    + "SUBSTR(a.atgs_group_identifier, 2, 6), "
					    + "TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10), 'YYYY/MM/DD') "
					    + "ORDER BY 1, 2");
		return srchStr.toString();
	}
	
	public String getJSON()
	{
		return buildgrid();
	}

	public Date getBpe() {
		return bpe;
	}

	public void setBpe(Date bpe) {
		this.bpe = bpe;
	}

	public Date getOpa() {
		return opa;
	}

	public void setOpa(Date opa) {
		this.opa = opa;
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

	public List<AlsTransactionGrpMassCopyDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsTransactionGrpMassCopyDTO> model) {
		this.model = model;
	}

}
