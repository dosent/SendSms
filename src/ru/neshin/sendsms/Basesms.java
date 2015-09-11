package ru.neshin.sendsms;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

 
public class Basesms {

	private String s_password;
	private String s_ip_device; 
	private String s_user;
	private String s_line = "4";
	
	public Basesms ()
	{
		// Manuals http://www.hybertone.com/uploadfile/download/20140304125509964.pdf
		// http://www.voip-shop.ru/manual/GoIP4_UserManual.pdf
		s_password 	= "1234"; 			// default sms password
		s_user 		= "sms";			// default user sms 
		s_ip_device	= "192.168.0.100"; 	// default ip devices
		s_line = "2";
	}
	public Basesms (String a_s_password, String a_s_user, String a_s_ip_device, String a_s_line)
	{
		s_password 	= a_s_password; 			
		s_user 		= a_s_user;			 
		s_ip_device	= a_s_ip_device; 
		s_line		= a_s_line;
	}	
	
	public Boolean send(String a_telnum, String a_message) {
		if (s_user.toLowerCase(). equals("admin"))
			return allusersend(a_telnum, a_message, "//*[@id='tools_page_3_div']/div/form");
		if (s_user.toLowerCase(). equals("sms"))
			return allusersend(a_telnum, a_message, "//*[@id='tools_page_0_div']/div/form");
		
		return false; 
	}
	
	private Boolean allusersend(String a_telnum, String a_message, String a_XPath)
	{
		if (a_telnum == null || a_message == null) return false;
		
		

	    String base64encodedUsernameAndPassword = base64Encode(s_user + ":" + s_password);
		HtmlPage page;
		final WebClient webClient = new WebClient(BrowserVersion.CHROME);
		try {
			webClient.addRequestHeader("Authorization", "Basic " + base64encodedUsernameAndPassword);
			webClient.getOptions().setJavaScriptEnabled(false);
			
			//Загружаем нужную страницу
			page = webClient.getPage(getConnectionString());
			final String smscontent = a_message;
			final String line = s_line;
			final HtmlForm form = (HtmlForm) page.getByXPath(a_XPath).get(0);
			// заполняем тел номер
			final HtmlTextInput textField_telnum = form.getInputByName("telnum");
			textField_telnum.setValueAttribute(a_telnum);
			// заполняем сообщение
			final HtmlTextArea textField_smscontent = form.getTextAreaByName("smscontent");
			textField_smscontent.setText(smscontent);
			
			final HtmlRadioButtonInput ratio_line = form.getInputByName("line");
			ratio_line.setValueAttribute(line);
			
			final HtmlSubmitInput button = form.getInputByName("send");
			button.setValueAttribute("Send");

			button.click();
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
			return false;
		} 
		finally {
			webClient.close();
		}
		return true;
	}
  
	 private String base64Encode(String stringToEncode)
	 {
	    return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
	 }
	
	private String getConnectionString()
	{
		String value = null;
		value = "http://"+s_ip_device+"/default/en_US/tools.html?type=sms";
		return value;
	}

}
