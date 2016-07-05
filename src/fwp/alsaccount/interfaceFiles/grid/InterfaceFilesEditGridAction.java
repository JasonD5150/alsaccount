package fwp.alsaccount.interfaceFiles.grid;


import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.interfaceFiles.AlsInterfaceFilesAS;
import fwp.alsaccount.dao.interfaceFiles.AlsInterfaceFiles;
import fwp.alsaccount.utils.Utils;

/**
 * @author CFA077
 *
 */
public class InterfaceFilesEditGridAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1009831926868383903L;
	private String aifFile;
	private String aifNotes;
	private String aifSendFlag;
	
	private String id;

	public String execute(){
		Integer aifId = Integer.parseInt(id);
		
		Utils util = new Utils();
		String userId = Integer.toString(util.getPersonId(null));
		
		AlsInterfaceFilesAS aifAS = new AlsInterfaceFilesAS();
		AlsInterfaceFiles aif = aifAS.findById(aifId);
				
		Integer origId;
		if(aif.getAifOrigfileId() == null){
			origId = aifId;			
			
		}else{
			origId = aif.getAifOrigfileId();
		}
		// mark all other files with same parent as do not send (can send only one)
		if (aifSendFlag.equals("Y")){
			List<AlsInterfaceFiles> aifArry = aifAS.findAllByWhere(" WHERE (aifOrigfileId = '" + origId.toString() + "' or aifId = '" + origId.toString() + "') and aifSendFlag = 'Y'" );
			for(AlsInterfaceFiles aifElem : aifArry){
				aifElem.setAifSendFlag("N");
				aifElem.setAifModPersonid(userId);
				aifAS.save(aifElem);
			}
		}

		// if file text has changed, make a new record in the database. If only notes or the send flag changed, edit the current file
		if(aifFile.equals(aif.getAifFile())){					
			aif.setAifNotes(aifNotes);
			aif.setAifSendFlag(aifSendFlag);			
			aif.setAifModPersonid(userId);
			aifAS.save(aif);			

		}else{
			AlsInterfaceFiles aif_new = new AlsInterfaceFiles(null, userId, null, aif.getAifAwgcId(), aif.getAifFileName(), aif.getAifParentProcess(), 
					aif.getAifFileSent(), aifFile, aif.getAifProcessDate(), origId, aifNotes, aifSendFlag);		
			aifAS.save(aif_new);			
		}
		
		return SUCCESS;
	}

	
	public String getAifFile() {
		return aifFile;
	}
	public void setAifFile(String aifFile) {
		this.aifFile = aifFile;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAifNotes() {
		return aifNotes;
	}
	public void setAifNotes(String aifNotes) {
		this.aifNotes = aifNotes;
	}
	public String getAifSendFlag() {
		return aifSendFlag;
	}
	public void setAifSendFlag(String aifSendFlag) {
		this.aifSendFlag = aifSendFlag;
	}
}
