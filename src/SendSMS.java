import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import java.io.File;

/**
 * 
 */

/**
 * @author i.neshin
 * 
 *
 */
public class SendSMS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// �������� ��������� ������
		
/*		Option o_tel = new Option("t", "telefone(s)", true, "����� �������� ��� ��������� ����� �������");
		Option o_message = new Option("m", "message", true, "��������� ������� �� ����� 70 ��������");
		Option o_action = new Option("a", "action", true, "����� ������ ����� ����� ���������� ���");
		
		Options options = new Options(); 
		options.addOption(o_message); 
		options.addOption(o_action);
		
*/		

		final WebClient webClient = new WebClient(BrowserVersion.CHROME);
		String username = "admin";
	    String password = "admin";
	    String base64encodedUsernameAndPassword = base64Encode(username + ":" + password);
		HtmlPage page;
		try {
			webClient.addRequestHeader("Authorization", "Basic " + base64encodedUsernameAndPassword);
			webClient.getOptions().setJavaScriptEnabled(false);
			
			//��������� ������ ��������
			page = webClient.getPage("http://192.168.15.67/default/en_US/tools.html?type=sms");
			
			List<?> divs = page.getByXPath("//*[@id='tools_page_3_div']/div/form/input[1]");
			final HtmlHiddenInput smskey = ((HtmlHiddenInput) divs.get(0));
			divs = page.getByXPath("//*[@id='tools_page_3_div']/div/form/input[2]");
			final HtmlHiddenInput action = ((HtmlHiddenInput) divs.get(0));
			
			final String telnum = "89603725051";
			final String smscontent = "���� ���� ���� ���� ���� ���� ���� ���� ���� ���� ���� ���� ���� ���� ����1";
			final String line = "4";
			
			String newurl =  "http://192.168.15.67/default/en_US/?type=sms";
			
			divs = page.getByXPath("//*[@id='tools_page_3_div']/div/form");
			
			final HtmlForm form = (HtmlForm) page.getByXPath("//*[@id='tools_page_3_div']/div/form").get(0);
			// ��������� ��� �����
			final HtmlTextInput textField_telnum = form.getInputByName("telnum");
			textField_telnum.setValueAttribute(telnum);
			// ��������� ���������
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
		} 
		finally {
			webClient.close();
		}
		
		
		return ;
	}
	
	 private static String base64Encode(String stringToEncode)
	 {
	    return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
	 }

}
