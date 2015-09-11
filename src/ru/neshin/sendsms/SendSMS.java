package ru.neshin.sendsms;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.Console;
/**
 * @author i.neshin
 */
public class SendSMS {
	
	private static Options options;

	public static void main(String[] args) {
		String s_password = null;
		String s_ip_device = null;
		String s_message = null;
		String s_user = null;
		String s_line = "4";
		String s_list_tel[] = null;

		Option o_tel = new Option("t", "telefone", true, "Номер телефона или телефонов");
		Option o_message = new Option("m", "message", true, "Сообщение длинной не более 70 символов");
		Option o_user = new Option("u", "user", true, "Имя пользователя");
		Option o_password = new Option("p", "password", true, "пароль пользователя");
		Option o_password_input = new Option("i", "in_password", false, "запросить пароль пользователя");
		Option o_action = new Option("a", "action", true, "Номер канала через какой отправлять смс, по умолчанию 4");
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
				System.out.println("Пример вызова sendsms -t 89603725051 -p 1234 -d 192.168.15.67 -m \"привет букет\" -u sms -a 3");
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
			if(cline.hasOption("a")) {
				s_line = cline.getOptionValue("a");
			}

			if ((s_password == null) ||	(s_ip_device == null) || (s_message == null) || (s_list_tel == null) ||(s_user == null) ||
					(s_list_tel[0] == null)) {
				return;
			}
			Basesms bs = new Basesms(s_password, s_user, s_ip_device, s_line);
			System.out.println(s_list_tel[0]);
			bs.send(s_list_tel[0], s_message);
			
			System.out.println(s_list_tel[0]);

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
