package fwp.alsaccount.interfaceFiles.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.interfaceFiles.AlsInterfaceFilesAS;
import fwp.alsaccount.dao.interfaceFiles.AlsInterfaceFiles;
/**
 * @author CFA077
 *
 */
public class InterfaceFilesGridAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 351318520875756026L;
	private static final Logger log = LoggerFactory.getLogger(InterfaceFilesGridAction.class);
	
	private List<AlsInterfaceFiles> gridModel;
	
	private AlsInterfaceFiles iFile;
	private Date createDateFrom;
	private Date createDateTo;
	private Date processDateFrom;
	private Date processDateTo;
	
	public String buildgrid(){
		try{
			if(iFile.getAifAwgcId() != null && iFile.getAifAwgcId() == -1){
				iFile.setAifAwgcId(null);
			}
			if(iFile.getAifFileName() != null){
				iFile.setAifFileName(iFile.getAifFileName().trim().toUpperCase());
			}

			if(createDateFrom == null && createDateTo == null && processDateFrom == null && processDateTo == null &&
					iFile.getAifAwgcId() == null && iFile.getAifFileName().isEmpty() && iFile.getAifFileSent().isEmpty()){
				gridModel = new ArrayList<AlsInterfaceFiles>();
			}else{
				AlsInterfaceFilesAS aifAS = new AlsInterfaceFilesAS();
				gridModel = aifAS.searchFiles(iFile, createDateFrom, createDateTo, processDateFrom, processDateTo);
			}
			
		}catch (Exception ex){
			log.debug("AlsInterfaceFiles did not load " + ex.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getJSON() {
		return buildgrid();
	}

	public List<AlsInterfaceFiles> getGridModel() {
		return gridModel;
	}

	public void setGridModel(List<AlsInterfaceFiles> gridModel) {
		this.gridModel = gridModel;
	}

	public AlsInterfaceFiles getiFile() {
		return iFile;
	}

	public void setiFile(AlsInterfaceFiles iFile) {
		this.iFile = iFile;
	}

	public Date getCreateDateFrom() {
		return createDateFrom;
	}

	public void setCreateDateFrom(Date createDateFrom) {
		this.createDateFrom = createDateFrom;
	}

	public Date getCreateDateTo() {
		return createDateTo;
	}

	public void setCreateDateTo(Date createDateTo) {
		this.createDateTo = createDateTo;
	}

	public Date getProcessDateFrom() {
		return processDateFrom;
	}

	public void setProcessDateFrom(Date processDateFrom) {
		this.processDateFrom = processDateFrom;
	}

	public Date getProcessDateTo() {
		return processDateTo;
	}

	public void setProcessDateTo(Date processDateTo) {
		this.processDateTo = processDateTo;
	}

}
