package fwp.alsaccount.sabhrs.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.InternalProviderTdtDTO;
import fwp.alsaccount.utils.HibHelpers;



public class GetTdtPDFAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ByteArrayInputStream inputStream;
    private String  fileName;
	private String txIdentifier;
	private String transCd;
	private String depositIds;

	private String type;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
	SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/YYYY hh:mm:ss");
	DecimalFormat df = new DecimalFormat("#,###.00"); 
	
	public String input() {
		return SUCCESS;
	}
	
	public String execute() {
		String rtv = SUCCESS;
		if("S".equals(type)){
			single();
		}else if("M".equals(type)){
			multiple();
		}
		return rtv;
		
	}
	
	private String single(){
		String rtv = SUCCESS;
        
        try{             
			 ByteArrayOutputStream buffer = new ByteArrayOutputStream();
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
               rtv = ERROR;         
        }
        return rtv;
	}
	
	private String multiple(){
		String rtv = SUCCESS;
        HibHelpers hh = new HibHelpers();
        try{             
			 ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			 Document document = new Document(PageSize.LETTER);
			 PdfWriter writer = PdfWriter.getInstance(document, buffer);
			 document.open();
			 document.bottomMargin();
			 document.addAuthor("Montana Fish, Wildlife & Parks");
			 document.addCreationDate();
			 document.addTitle("Treasury Deposit Ticket");
			 fileName = "TreasuryDepositTicket.pdf";
			 XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			 StringBuilder builder = new StringBuilder();
			 
			 List<InternalProviderTdtDTO> deposits = hh.getDeposits(depositIds);
			 Date tmpBpe = null;
			 Double totAmt = 0.0;
			 for(InternalProviderTdtDTO tmp : deposits){
				 if(tmpBpe == null ){
					 tmpBpe = tmp.getBpe();
					 builder.append(buildHead(tmp));
				 }else if(tmp.getBpe().after(tmpBpe)){
					 builder.append(buildFoot(totAmt));
					 worker.parseXHtml(writer, document, new StringReader(builder.toString()));
					 
					 tmpBpe = tmp.getBpe();
					 totAmt = 0.0;
					 document.newPage();
					 
					 builder = new StringBuilder();
					 builder.append(buildHead(tmp));
				 }
				 totAmt += tmp.getDepAmt();
				 builder.append("<tr><td>"+(tmp.getCashInd()!=null?tmp.getCashInd():"N")+"</td><td>"+tmp.getBankCd()+"</td><td>"+tmp.getBankNm()+"</td><td>"+sdf.format(tmp.getDepDt())+"</td><td>$"+df.format(tmp.getDepAmt())+"</td><td>"+tmp.getDepId()+"</td></tr>");
			 }
			 builder.append(buildFoot(totAmt));
			 worker.parseXHtml(writer, document, new StringReader(builder.toString()));
			 
			 document.close();
			 inputStream = new ByteArrayInputStream(buffer.toByteArray());

        }catch (Exception e) {
               e.printStackTrace();
               addActionError("A error occurred when trying to create treasury deposit ticket.");
               rtv = ERROR;         
        }
        return rtv;
	}
	
	private String buildHead(InternalProviderTdtDTO intProvTDT){
		Date date = new Date();
		StringBuilder builder = new StringBuilder();
		
		builder = new StringBuilder("<html><head></head><body>");
		builder.append("<h1><font color='#003399'>Montana Fish Wildlife & Parks</font></h1>");
		builder.append("<h3><font color='#003399'>"+sdf2.format(date)+"</font></h3>");
		builder.append("<h3><font color='#003399'>Treasury Deposit Ticket</font></h3>");
		builder.append("<table>");
		builder.append("'<tr><td>Agency Business Unit: </td><td>"+intProvTDT.getBusinessUnit()+"</td></tr>");
		builder.append("<tr><td>Provider No: </td><td>"+intProvTDT.getProvNo()+"</td></tr>");
		builder.append("<tr><td>Provider Name: </td><td>"+intProvTDT.getBusinessNm()+"</td></tr>");
		builder.append("<tr><td>Billing Period End Date: </td><td>"+sdf.format(intProvTDT.getBpe())+"</td></tr>");
		builder.append("</table>");
		builder.append("<table cellpadding='10'>");
		builder.append("<thead><tr><th>Cash</th><th>Bank Code</th><th>Bank Name</th><th>Deposit Date</th><th>Amount</th><th>Deposit Id</th></tr></thead>");
		
		return builder.toString();
	}
	
	private String buildFoot(Double totAmt){
		StringBuilder builder = new StringBuilder();
		
		builder.append("<tfoot><tr><td></td><td></td><td>Total of Deposits:</td><td>$"+df.format(totAmt)+"</td><td></td></tr></tfoot>");
		builder.append("</table>");
		builder.append("</body></html>");
		
		return builder.toString();
	}
	
	

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDepositIds() {
		return depositIds;
	}

	public void setDepositIds(String depositIds) {
		this.depositIds = depositIds;
	}
	
}
