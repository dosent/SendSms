package ru.neshin.sendsms;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

 
public class Basesms {
	
	public static final int TIME_WAIT_SEND = 10;
	
	private String s_password;
	private String s_ip_device;
	private String s_user;
	
	public Basesms ()
	{
		// Manuals http://www.hybertone.com/uploadfile/download/20140304125509964.pdf
		// http://www.voip-shop.ru/manual/GoIP4_UserManual.pdf
		s_password 	= "1234"; 			// default sms password
		s_user 		= "sms";			// default user sms 
		s_ip_device	= "192.168.0.100"; // default ip devices
	}
	public Basesms (String a_s_password, String a_s_user, String a_s_ip_device)
	{
		s_password 	= a_s_password; 			
		s_user 		= a_s_user;			 
		s_ip_device	= a_s_ip_device; 
	}	
	
	public Boolean send(String a_telnum, String a_message )
	{
		if (a_telnum == null || a_message == null) return false;
		
		final WebClient webClient = new WebClient(BrowserVersion.CHROME);

	    String base64encodedUsernameAndPassword = base64Encode(s_user + ":" + s_password);
		HtmlPage page;
		try {
			webClient.addRequestHeader("Authorization", "Basic " + base64encodedUsernameAndPassword);
			webClient.getOptions().setJavaScriptEnabled(false);
			
			//Загружаем нужную страницу
			page = webClient.getPage(getConnectionString());
			
			List<?> divs = page.getByXPath("//*[@id='tools_page_3_div']/div/form/input[1]");
			final HtmlHiddenInput smskey = ((HtmlHiddenInput) divs.get(0));
			divs = page.getByXPath("//*[@id='tools_page_3_div']/div/form/input[2]");
			final HtmlHiddenInput action = ((HtmlHiddenInput) divs.get(0));
			
			final String smscontent = a_message;
			final String line = "4";
			
			//String newurl =  "http://192.168.15.67/default/en_US/?type=sms";
			
			divs = page.getByXPath("//*[@id='tools_page_3_div']/div/form");
			
			final HtmlForm form = (HtmlForm) page.getByXPath("//*[@id='tools_page_3_div']/div/form").get(0);
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

			final HtmlPage page2 = button.click();
		
			System.out.println(page2.getReadyState());
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		finally {
			webClient.close();
		}
		
		return true;
	}
	
	 private static String base64Encode(String stringToEncode)
	 {
	    return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
	 }
	
	private String getConnectionString()
	{
		String value = null;
		value = "http://"+s_ip_device+"/default/en_US/tools.html?type=sms";
		return value;
	}
/*
	private String getConnectionString(String s_ip)
	{
		String value = null;
		value = "http://"+s_ip+"/default/en_US/tools.html?type=sms";
		return value;
	}
*/

}
