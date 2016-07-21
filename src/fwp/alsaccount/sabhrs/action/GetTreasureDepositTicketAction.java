package fwp.alsaccount.sabhrs.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.utils.HibHelpers;



public class GetTreasureDepositTicketAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ByteArrayInputStream inputStream;
    private String  fileName;
	private String txIdentifier;
	private String transCd;
	
	public String input() {
		return SUCCESS;
	}
	
	public String execute() {
		String rtv = SUCCESS;
        
        try{             
			 ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			 Date created = new Date();
			 Document document = new Document(PageSize.LETTER);
			 PdfWriter writer = PdfWriter.getInstance(document, buffer);
			 document.open();
			 document.bottomMargin();
			 document.addAuthor("Montana Fish, Wildlife & Parks");
			 document.addCreationDate();
			 document.addTitle("Treasury Deposit Ticket");
			 fileName = "TreasuryDepositTicket.pdf";
			 XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			 StringBuilder builder = new StringBuilder("<html><head></head><body>");
			 
			 HibHelpers hh = new HibHelpers();
			 String hold = hh.getTreasuryDepositTicket(txIdentifier, transCd).toString();
			 builder.append(hold);
			
			 builder.append("</body></html>");
			 worker.parseXHtml(writer, document, new StringReader(builder.toString())); 
			 document.close();
			 inputStream = new ByteArrayInputStream(buffer.toByteArray());

        }catch (Exception e) {
               e.printStackTrace();
               addActionError("A error occurred when trying to create treasury deposit ticket.");
               rtv = ERROR;         }
        return rtv;
	}

	/**
	 * @return the inputStream
	 */
	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getTxIdentifier() {
		return txIdentifier;
	}

	public void setTxIdentifier(String txIdentifier) {
		this.txIdentifier = txIdentifier;
	}

	public String getTransCd() {
		return transCd;
	}

	public void setTransCd(String transCd) {
		this.transCd = transCd;
	}
	
}
