package fwp.alsaccount.sabhrs.grid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.AlsProviderBankDetailsDTO;
import fwp.alsaccount.utils.HibHelpers;

public class IntProvTdtGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(IntProvTdtGridAction.class);

    private List<AlsProviderBankDetailsDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
	private Date 				bpeFrom;
    private Date 				bpeTo;
    private Date 				opaDate;
    private String				provNo;
    
    private Boolean 			search = false;


	public String buildgrid(){ 
		HibHelpers hh = new HibHelpers();
		String srchStr = buildQueryStr();
        try{
        	setModel(new ArrayList<AlsProviderBankDetailsDTO>());
        	if (search) {
        		model = hh.getInternalProviderTdt(srchStr);
    		}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsProviderBankDetails did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }
	
	private String buildQueryStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		StringBuilder srchStr = new StringBuilder();
		srchStr.append("SELECT api_provider_no||'_'||apbd_Billing_To||'_'||apbd_Seq_No gridKey, "
						+ "api_provider_no providerNo, "
						+ "(select api.api_business_nm from ALS.ALS_PROVIDER_INFO api WHERE api.api_provider_no = apbd.api_provider_no) providerName, "
						+ "apbd_deposit_date apbdDepositDate, "
						+ "apbd_amount_deposit apbdAmountDeposit, "
		    			+ "apbd_deposit_id apbdDepositId "
		    			+ "FROM ALS.ALS_PROVIDER_BANK_DETAILS apbd "
		    			+ "WHERE api_provider_no IN (SELECT api_provider_no FROM als.als_provider_info WHERE api_provider_class = 'I') ");
		
		if(bpeFrom != null){
			srchStr.append(" AND apbd_billing_to BETWEEN TO_DATE('"+sdf.format(bpeFrom)+"', 'dd-mm-yyyy') AND NVL(TO_DATE('"+sdf.format(bpeTo)+"', 'dd-mm-yyyy'), TO_DATE('"+sdf.format(bpeFrom)+"', 'dd-mm-yyyy')) ");
			search = true;
		}
		if(provNo != null && !"".equals(provNo)){
			srchStr.append("AND api_provider_no = "+provNo+" ");
			search = true;
		}
		if(opaDate != null ){
			srchStr.append("AND EXISTS (SELECT 1 "
				  +"FROM als.als_internal_remittance "
				  +"WHERE api_provider_no = TO_NUMBER(SUBSTR(atgs_group_identifier, 2, 6)) "
		          +"AND air_billing_to = TO_DATE(SUBSTR(atgs_group_identifier, 9, 10), 'YYYY/MM/DD') "
		          +"AND air_offln_payment_approved = 'Y' "
		          +"AND air_offln_payment_app_dt = "+opaDate+") ");
			search = true;
		}
		
		srchStr.append("ORDER BY api_provider_no, apbd_billing_to DESC");
		return srchStr.toString();
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

	public List<AlsProviderBankDetailsDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsProviderBankDetailsDTO> model) {
		this.model = model;
	}
	
	public Date getBpeFrom() {
		return bpeFrom;
	}

	public void setBpeFrom(Date bpeFrom) {
		this.bpeFrom = bpeFrom;
	}

	public Date getBpeTo() {
		return bpeTo;
	}

	public void setBpeTo(Date bpeTo) {
		this.bpeTo = bpeTo;
	}

	public Date getOpaDate() {
		return opaDate;
	}

	public void setOpaDate(Date opaDate) {
		this.opaDate = opaDate;
	}

	public String getProvNo() {
		return provNo;
	}

	public void setProvNo(String provNo) {
		this.provNo = provNo;
	}
}
