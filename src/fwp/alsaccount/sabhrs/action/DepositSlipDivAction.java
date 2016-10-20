package fwp.alsaccount.sabhrs.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.inventory.dao.AlsInternalRemittance;
import fwp.als.hibernate.inventory.dao.AlsInternalRemittanceIdPk;
import fwp.alsaccount.appservice.sabhrs.AlsInternalRemittanceAS;
import fwp.alsaccount.appservice.sabhrs.AlsProviderBankDepositSlipAS;
import fwp.alsaccount.dao.sabhrs.AlsProviderBankDepositSlip;
import fwp.alsaccount.utils.Utils;
import fwp.ssr.hibernate.utils.FwpHelperUtils;



public class DepositSlipDivAction extends ActionSupport implements ServletResponseAware {

	private static final long serialVersionUID = -7443755454289955788L;
	HttpServletResponse response;
	
	private String remittanceId;
	private Integer apbdsId;
	private String apbdId;
    private String apbdsFileType;
    private String apbdsFileName;
    private File file;
    private AlsProviderBankDepositSlip apbds;
    private Integer apbdsImageId;
    private Boolean canEdit = false;
    private Boolean provCom = true;
    private Boolean remApp = true;;

    
	public String input() throws ParseException {
		
		AlsProviderBankDepositSlipAS apbdsAS = new AlsProviderBankDepositSlipAS();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		String[] keys = apbdId.split("_");
		Integer provNo = Integer.parseInt(keys[2]);
		Integer seqNo = Integer.parseInt(keys[1]);
		Timestamp bpTo = new Timestamp(sdf.parse(keys[0]).getTime());
		List<AlsProviderBankDepositSlip> apbdsLst = apbdsAS.findByApbdId(provNo, bpTo, seqNo);
		if(!apbdsLst.isEmpty()){
			apbds = apbdsLst.get(0);
			apbdsImageId = apbds.getApbdsId();
			apbdsId = apbds.getApbdsId();
		}else{
			apbds = null;
		}
		
		setRoles();
		return SUCCESS;
	}
	
	public String execute() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String errMsg = "";
		clearActionErrors();
		clearMessages();
		AlsProviderBankDepositSlipAS apbdsAS = new AlsProviderBankDepositSlipAS();
		
		FileInputStream fileInputStream = null;
		try {
			Integer personId = FwpHelperUtils.getPersonId();
			//Create a byte array
			byte[] bFile = null;
			if (file != null) {
				if(apbdsId == null){
					apbds = new AlsProviderBankDepositSlip();
					//Adding
					//initialize the byte array to the length of the uploaded file
					bFile = new byte[(int) file.length()];		
	
					//Create a new FileInputStream and open a connection to the uploaded file.
					fileInputStream = new FileInputStream(file);
					//convert file into array of bytes
					fileInputStream.read(bFile);
					
					//Set the filename, file mime type and image on the object.
					String[] keys = apbdId.split("_");
					Integer provNo = Integer.parseInt(keys[2]);
					Integer seqNo = Integer.parseInt(keys[1]);
					Timestamp bpTo = new Timestamp(sdf.parse(keys[0]).getTime());
					apbds.setApbdsId(apbdsAS.getNextSeqNo());
					apbds.setApbdsFileName(apbdsFileName);
					apbds.setApbdsFileType(apbdsFileType);
					apbds.setApbdsData(bFile);
					apbds.setApiProviderNo(provNo);
					apbds.setApbdBillingTo(bpTo);
					apbds.setApbdSeqNo(seqNo);
					apbds.setApbdsCreatePersonId(personId);
					apbdsAS.save(apbds);
					addActionMessage("File Successfully Added.");
				}else{
					apbds = apbdsAS.findById(apbdsId);
					//Editing
			
					//initialize the byte array to the length of the uploaded file
					bFile = new byte[(int) file.length()];		
	
					//Create a new FileInputStream and open a connection to the uploaded file.
					fileInputStream = new FileInputStream(file);
					//convert file into array of bytes
					fileInputStream.read(bFile);
					
					//Set the filename, file mime type and image on the object.
					apbds.setApbdsFileName(apbdsFileName);
					apbds.setApbdsFileType(apbdsFileType);
					apbds.setApbdsData(bFile);
					apbds.setApbdsModPersonid(personId);
					apbdsAS.save(apbds);
					addActionMessage("File Successfully Updated.");
				}
			}else {
				throw new Exception("You must select a file to upload.");
			}
			apbdsImageId = apbds.getApbdsId();
			apbdsId = apbds.getApbdsId();
		} catch(Exception ex) {
			if (ex.toString().contains("ORA-02292")) {
				errMsg += "Record has child record(s) which would need to be deleted frist.";
			} else if (ex.toString().contains("ORA-02291")) {
				errMsg += "Cannot save the record with violation of constraint.";
			} else if (ex.toString().contains("ORA-00001")) {
				errMsg += "Unable to add this record due to duplicate";
			} else {
				errMsg += " " + ex.getMessage();
			}
			  
		    addActionError(errMsg);
	        return ERROR;
		} finally{
			if(fileInputStream != null)
				try { fileInputStream.close();} catch (IOException e) {e.printStackTrace(); }
		}
		return SUCCESS;
	}
	
	void setRoles() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		AlsInternalRemittanceAS airAS = new AlsInternalRemittanceAS();
		AlsInternalRemittanceIdPk airIdPk = new AlsInternalRemittanceIdPk();
		AlsInternalRemittance air = null;	
		
		Integer provNo = Integer.parseInt(remittanceId.split("_")[2]);
		Timestamp bpFrom = new Timestamp(sdf.parse(remittanceId.split("_")[0]).getTime());
		Timestamp bpTo = new Timestamp(sdf.parse(remittanceId.split("_")[1]).getTime());
		airIdPk.setApiProviderNo(provNo);
		airIdPk.setAirBillingFrom(bpFrom);
		airIdPk.setAirBillingTo(bpTo);
		air = airAS.findById(airIdPk);
		Utils utils = new Utils();
		if (utils.hasAnyRole("ALSACCOUNT_INTPROV")) {
			canEdit = true;
		}
		if (air == null || air.getAirCompleteProvider() == null) {
			provCom = false;
		}
		if (air == null || air.getAirOfflnPaymentAppDt() == null) {
			remApp = false;
		}
	}
	
	public String delete() {
		AlsProviderBankDepositSlipAS apbdsAS = new AlsProviderBankDepositSlipAS();
		AlsProviderBankDepositSlip	apbds = apbdsAS.findById(apbdsId);
		apbdsAS.delete(apbds);
		return "delete";
	}
	
	public String download() throws Exception {
		try{

		} catch (Exception e){
			this.addActionError("Error retrieving data.");
		} finally {
			response.getOutputStream().flush();
		}
		
		return null;
	}
	
	//Struts specific upload method used by the default upload intercepter
	public void setFileContentType(String contentType) {
        this.apbdsFileType = contentType;
    }
	
    public void setFileFileName(String filename) {
        this.apbdsFileName = filename;
    }
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Integer getApbdsId() {
		return apbdsId;
	}

	public void setApbdsId(Integer apbdsId) {
		this.apbdsId = apbdsId;
	}

	public String getApbdId() {
		return apbdId;
	}

	public void setApbdId(String apbdId) {
		this.apbdId = apbdId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public AlsProviderBankDepositSlip getApbds() {
		return apbds;
	}

	public void setApbds(AlsProviderBankDepositSlip apbds) {
		this.apbds = apbds;
	}

	public Integer getApbdsImageId() {
		return apbdsImageId;
	}

	public void setApbdsImageId(Integer apbdsImageId) {
		this.apbdsImageId = apbdsImageId;
	}

	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
	}

	public String getRemittanceId() {
		return remittanceId;
	}

	public void setRemittanceId(String remittanceId) {
		this.remittanceId = remittanceId;
	}

	public Boolean getProvCom() {
		return provCom;
	}

	public void setProvCom(Boolean provCom) {
		this.provCom = provCom;
	}

	public Boolean getRemApp() {
		return remApp;
	}

	public void setRemApp(Boolean remApp) {
		this.remApp = remApp;
	}

	
	
}
