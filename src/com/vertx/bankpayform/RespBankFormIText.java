package com.vertx.bankpayform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.vertx.bankpayform.util.ChineseNumberChar;
import com.vertx.bankpayform.util.ValueConverter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;

/**
 * using vert.x
 * handle json then response pdf by iText 
 * @author pjhsiao
 *
 */
public class RespBankFormIText extends AbstractVerticle  {
	private final static Map<String,String> bankcodesMap;
	private final static String fileName = "twbankform.pdf";
	private final static String fontType= "KAIU.TTF";
	static{
		bankcodesMap = new HashMap<String, String>();
		bankcodesMap.put("1409","014099"); 
		bankcodesMap.put("1399","013991"); 
		bankcodesMap.put("1453","014531"); 
		bankcodesMap.put("1452","014529"); 
		bankcodesMap.put("1450","014501"); 
		bankcodesMap.put("1400","014001"); 
		bankcodesMap.put("1576","015761");
		bankcodesMap.put("1449","014499"); 
		bankcodesMap.put("1451","014511");     
		bankcodesMap.put("1462","014629"); 
		bankcodesMap.put("1461","014619"); 
		bankcodesMap.put("1460","014609"); 
		bankcodesMap.put("1459","014599"); 
		bankcodesMap.put("1458","014589"); 
		bankcodesMap.put("1402","014029");     
		bankcodesMap.put("1448","014489"); 
		bankcodesMap.put("3401","134019"); 
	}
	
	@Override
	public void start() throws Exception {
		HttpServer server = vertx.createHttpServer();
		Buffer buff = Buffer.buffer();
		
		server.requestHandler(request -> {
				try {
					if(0 == request.method().compareTo(HttpMethod.POST)){
						request.bodyHandler(httpServerRequesr->{
							try {
								/**
								 * handle json to Object 
								 */
								byte[] bytes = httpServerRequesr.getBytes();
								String jsonContext = new String(bytes,"UTF-8");
								TwbankForm twbankForm= new Gson().fromJson(jsonContext.toString(),  TwbankForm.class);
								
								/**
				    			 * dynamic parameter 
				    			 */
				    			String taiwanToday   = ValueConverter.toChineseDateYMD(new Date(System.currentTimeMillis()));
				    			String payAccount    = twbankForm.getPayAccount();
				    			Integer payMoney     = twbankForm.getPayMoney();
				    			String barcodeString = payAccount+"  "+ new DecimalFormat("000000").format(payMoney);
				    			String title         =  twbankForm.getTitle();
				    			String chineseNumberCharMoney = ChineseNumberChar.complexString(payMoney.toString());
				    			String payDateLine   =  twbankForm.getPayDeadline();
				    			String payName      = twbankForm.getPayName();
				    			String bankCode      = bankcodesMap.get(payAccount.substring(0, 4));
			    			
								payDateLine = ValueConverter.toChineseDateYMD(new SimpleDateFormat("yyyyMMdd").parse(payDateLine));
							
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								Document document = new Document(PageSize.A4);
								PdfWriter writer =  PdfWriter.getInstance(document, baos);
							    
								PdfReader reader = new PdfReader("src/" + fileName);
			    				
			    				document.open();
			    				PdfContentByte cb = writer.getDirectContent();
			    				PdfImportedPage impPage = writer.getImportedPage(reader, 1);
			    				
			    				Image image = Image.getInstance(impPage);
			    				image.setAbsolutePosition(0, 0);
			    				image.scalePercent(100f);
			    				/**
			    				 * 加入封面底框
			    				 */
			    				document.add(image);
			    				/**
			    				 *加入招生管道表頭
			    				 */
			    				cb.beginText();
			    				cb.setFontAndSize(getBf(BaseFont.IDENTITY_H), 16);
			    				cb.setRGBColorFill(0x00, 0x00, 0xFF);
			    				cb.setTextMatrix(126f, 786f);
			    				cb.showText(title);
			    				cb.setTextMatrix(126f, 566f);
			    				cb.showText(title);
			    				cb.setTextMatrix(126f, 346f);
			    				cb.showText(title);
			    				cb.endText();
			    				cb.resetRGBColorFill();
								
			    				/**
			    				 * 加製表日期
			    				 */
			    				cb.beginText();
			    				cb.setFontAndSize(getBf(BaseFont.IDENTITY_H), 10);
			    				cb.setTextMatrix(228f, 754f);
			    				cb.showText("製表日期:"+taiwanToday);
			    				cb.setTextMatrix(228f, 534f);
			    				cb.showText("製表日期:"+taiwanToday);
			    				cb.setTextMatrix(235f, 314f);
			    				cb.showText("製表日期:"+taiwanToday);
			    				
			    				cb.endText();
			    				
			    				/**
			    				 * 加入金額 
			    				 */
			    				cb.beginText();
			    				cb.setFontAndSize(getBf(BaseFont.IDENTITY_H), 10);
			    					//第一聯
			    				cb.setTextMatrix(165f, 706f);
			    				cb.showText(payMoney.toString());
			    				cb.setTextMatrix(177f, 619f);
			    				cb.showText(payMoney.toString());
			    				cb.setTextMatrix(408f, 603f);
			    				cb.showText(payMoney.toString());
			    					//第二聯
			    				cb.setTextMatrix(165f, 486f);
			    				cb.showText(payMoney.toString());
			    				cb.setTextMatrix(177f, 399f);
			    				cb.showText(payMoney.toString());
			    				cb.setTextMatrix(408f, 383f);
			    				cb.showText(payMoney.toString());
			    					//第三聯
			    				cb.setTextMatrix(255f, 157f);
			    				cb.showText(payMoney.toString());
			    				cb.setTextMatrix(400f, 76f);
			    				cb.showText(payMoney.toString());
			    				cb.endText();

			    				/**
			    				 * 加入大寫金額 
			    				 */
			    				cb.beginText();
			    				cb.setFontAndSize(getBf(BaseFont.IDENTITY_H), 12);
			    				cb.setTextMatrix(380f, 620f);
			    				cb.showText(chineseNumberCharMoney);
			    				cb.setTextMatrix(380f, 400f);
			    				cb.showText(chineseNumberCharMoney);
			    				cb.setTextMatrix(170f, 260f);
			    				cb.showText(chineseNumberCharMoney);
			    				cb.endText();
			    				
			    				/**
			    				 * 加入報名費字樣
			    				 */
			    				cb.beginText();
			    				cb.setFontAndSize(getBf(BaseFont.IDENTITY_H), 10);
			    					//第一聯
			    					cb.setTextMatrix(80f, 706f);
			    					cb.showText(payName);
			    					//第二聯
			    					cb.setTextMatrix(80f, 486f);
			    					cb.showText(payName);
			    				cb.endText();
			    				
			    				/**
			    				 * 加入虛擬帳號
			    				 */
			    				cb.beginText();
			    				cb.setFontAndSize(getBf(BaseFont.IDENTITY_H), 10);
			    					//第一聯
			    					cb.setTextMatrix(238f, 603f);
			    					cb.showText(payAccount);
			    					//第二聯
			    					cb.setTextMatrix(238f, 383f);
			    					cb.showText(payAccount);
			    					//第三聯
			    					cb.setTextMatrix(115f, 157f);
			    					cb.showText(payAccount);
			    					cb.setTextMatrix(228f, 76f);
			    					cb.showText(payAccount);
			    				cb.endText();	
			    				
			    				/**
			    				 *  繳費期限
			    				 */
			    				cb.beginText();
			    				cb.setFontAndSize(getBf(BaseFont.IDENTITY_H), 10);
			    					//第一聯
			    					cb.setTextMatrix(450f, 702f);
			    					cb.showText(payDateLine);
			    					//第二聯
			    					cb.setTextMatrix(450f, 482f);
			    					cb.showText(payDateLine);
			    				cb.endText();
			    				
			    				/**
			    				 * 代收類別
			    				 */
			    				cb.beginText();
			    				cb.setFontAndSize(getBf(BaseFont.IDENTITY_H), 12);
			    				cb.setTextMatrix(255f, 170f);
			    				cb.showText(bankCode);
			    				cb.endText();
			    				
			    				/**
			    				 * 加入barcode
			    				 */
			    				Barcode39 code39 = new Barcode39();
			    				code39.setCode(barcodeString);
			    				Image barcode = code39.createImageWithBarcode(cb, null, null);
			    				barcode.setAbsolutePosition(70, 115);
			    				document.add(barcode);
								
								
								document.close();
								baos.flush();
								buff.appendBytes(baos.toByteArray());
								request.response().setChunked(true);
								request.response().putHeader("Content-Type","application/pdf");
								request.response().write(buff);
								request.response().end();
								request.response().close();
							
								baos.close();
								request.response().close();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		server.listen(8888);
	}
	
	private static BaseFont getBf(String BaseFont_Type) throws DocumentException, IOException {
		return BaseFont.createFont("src/"+fontType, BaseFont_Type, BaseFont.NOT_EMBEDDED);
	}
}
