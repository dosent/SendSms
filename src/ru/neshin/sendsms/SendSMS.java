package ru.neshin.sendsms;
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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import java.io.File;

import java.io.Console;

/**
 * 
 */

/**
 * @author i.neshin
 * 
 *
 */
public class SendSMS {

	private static Options options;
	private static CommandLine cline;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// доделать командную строку

		String s_password = null;
		String s_ip_device = null;
		String s_message = null;
		String s_user = null;
		String s_list_tel[] = null;

		Option o_tel = new Option("t", "telefone", true, "Номер телефона или телефонов");
		Option o_message = new Option("m", "message", true, "Сообщение длинной не более 70 символов");
		Option o_user = new Option("u", "user", true, "Имя пользователя");
		Option o_password = new Option("p", "password", true, "пароль пользователя");
		Option o_password_input = new Option("i", "in_password", false, "запросить пароль пользователя");
		Option o_action = new Option("a", "action", false, "Номер канала через какой отправлять смс, по умолчанию 1");
		Option o_device = new Option("d", "device", true, "ip adress устройства");
		Option o_help = new Option("h", "help", false, "Помощь");
		
		options = new Options(); 
		options.addOption(o_message);
		options.addOption(o_user);
		options.addOption(o_password);
		options.addOption(o_action);
		options.addOption(o_tel);
		options.addOption(o_help);
		options.addOption(o_password_input);
		options.addOption(o_device);
		
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cline = parser.parse(options, args);
			if(cline.hasOption("h")) {
				System.out.println("Пример вызова sendsms -t 89603725051, 89603725150 -m \"текс сообщения\" -a 4");
				return;
	        }
			
			
			if(cline.hasOption("i")) {
				try {
					Console cons;
					char[] passwd;
					if ((cons = System.console()) != null &&
					     (passwd = cons.readPassword("%s", "Password:")) != null) {
					     s_password = new String(passwd);
					}
				}
				catch(Exception ex) {
			         ex.printStackTrace();      
			    }
	        }
			else {
				if(cline.hasOption("p")) {
					s_password = cline.getOptionValue("p");
				}
			}
			if(cline.hasOption("d")) {
				s_ip_device = cline.getOptionValue("d");
			}
			if(cline.hasOption("m")) {
				s_message = cline.getOptionValue("m");
			}
			if(cline.hasOption("t")) {
				
				s_list_tel = cline.getOptionValues("t");
			}
			if(cline.hasOption("u")) {
				
				s_user = cline.getOptionValue("u");
			}
	


			if ((s_password == null) ||	(s_ip_device == null) || (s_message == null) || (s_list_tel == null) ||(s_user == null) ||
					(s_list_tel[0] == null)) {
				return;
			}
			Basesms bs = new Basesms(s_password, s_user, s_ip_device);
			System.out.println(s_list_tel[0]);
			bs.send(s_list_tel[0], s_message);
			
			System.out.println(s_list_tel[1]);

		} catch (ParseException e) {
			System.out.println("Помощь с ключом -h");
			return;
		}
		finally {
			System.out.println("Complite");
		}
		
		return ;
	}


}
