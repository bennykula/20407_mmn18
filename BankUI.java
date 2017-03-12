import java.io.PrintStream;
import java.util.*;
public class BankUI {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in); //If input is from a file, change it here or just use input redirection
		PrintStream out=System.out;
		Bank b = new Bank();
		while(input.hasNext()) {
			out.println(command(input.nextLine(), b));
		}
		input.close();
	}
	
	public static String command(String input, Bank b) {
		switch(input.charAt(0)) {
			case '+':	return newAccount(input, b);
				
			case '-':	return remAccount(input, b);
				
			case '?':	return query(input, b);
				
			default:	return deposit(input, b);
		}
	}

	static String deposit(String input, Bank b) {
		int c_id;
		double amount;
		String name;
		String[] splitted = input.split(" ");
		name = splitted[0].concat(" ").concat(splitted[1]);
		c_id = Integer.parseInt(splitted[2]);
		amount = Double.parseDouble(splitted[3]);
		b.deposit(name, c_id, amount);
		return "deposited "+ amount + "to account number " + c_id +", " + name;
	}

	static String query(String input, Bank b) {
		String[] splitted = input.split(" ");
		if(splitted.length!=2) return "";
		switch(splitted[1]) {
		case "MAX": return b.getMax().toString();
		case "MINUS": return b.printMinus();
		default: return b.findAccountByC_ID(Integer.parseInt(splitted[1])).toString();
		
		}
		
	}

	static String remAccount(String input, Bank b) {
		String[] splitted = input.split(" ");
		b.remove(Integer.parseInt(splitted[1]));
		return "Removed account number "+ splitted[1];
	}

	static String newAccount(String input, Bank b) {
		String name;
		int c_id, id;
		double balance;
		String[] splitted = input.split(" ");
		name = splitted[1].concat(" ").concat(splitted[2]);
		id=Integer.parseInt(splitted[3]);
		c_id=Integer.parseInt(splitted[4]);
		balance=Double.parseDouble(splitted[5]);
		b.add(name, id, c_id, balance);
		return "Added account for " + name;
	}
	
	
}