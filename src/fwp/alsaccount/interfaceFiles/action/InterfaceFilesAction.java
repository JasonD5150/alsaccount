package fwp.alsaccount.interfaceFiles.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.appservice.interfaceFiles.AlsInterfaceFilesAS;
import fwp.alsaccount.dao.interfaceFiles.AlsInterfaceFiles;
import fwp.alsaccount.utils.ListUtils;

/**
 * @author CFA077
 *
 */
public class InterfaceFilesAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1369376912335266430L;
	private List<ListComp> fileTypeList;
	private String fileNameListTxt;
	
	private Integer exportAifId;
	private String fileName;
	private InputStream inputStream;
	
	public InterfaceFilesAction(){
		super();
		ListUtils lu = new ListUtils();
		fileTypeList = lu.getIdGenCode("INTERFACE_FILE_TYPE");	
		AlsInterfaceFilesAS aifAS = new AlsInterfaceFilesAS();
		List<AlsInterfaceFiles> lst = aifAS.findAllByWhere(" WHERE aifOrigfileId is null ");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		fileNameListTxt = "";
		for(int i=0; i<lst.size()-1; i++){
			fileNameListTxt = fileNameListTxt + lst.get(i).getAifId() + ":" + lst.get(i).getAifFileName() + " - " + dateFormat.format(lst.get(i).getAifCreateDate()) + ";";
		}
		if(lst.size() > 0){
			fileNameListTxt = fileNameListTxt + lst.get(lst.size()-1).getAifId() + ":" + lst.get(lst.size()-1).getAifFileName() + " - " + dateFormat.format(lst.get(lst.size()-1).getAifCreateDate());
		}		
	}
	
	public String input(){
		return SUCCESS;
	}
	

	public String exportFile(){
		try{
			AlsInterfaceFilesAS aifAS = new AlsInterfaceFilesAS();
			AlsInterfaceFiles aif = aifAS.findById(exportAifId);
			fileName = aif.getAifFileName();
 	        inputStream = new ByteArrayInputStream(aif.getAifFile().getBytes());			
		}
		catch (Exception e) {
			e.printStackTrace();			
			addActionError("An error occured while creating the file: " + e.getMessage());
			return ERROR;
		}
		return "report";
	}
	public List<ListComp> getFileTypeList() {
		return fileTypeList;
	}

	public void setFileTypeList(List<ListComp> fileTypeList) {
		this.fileTypeList = fileTypeList;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Integer getExportAifId() {
		return exportAifId;
	}

	public void setExportAifId(Integer exportAifId) {
		this.exportAifId = exportAifId;
	}

	public String getFileNameListTxt() {
		return fileNameListTxt;
	}

	public void setFileNameListTxt(String fileNameListTxt) {
		this.fileNameListTxt = fileNameListTxt;
	}

}
