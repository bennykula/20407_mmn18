import java.util.Comparator;

public class Bank {
	RedBlackMax<Account> pos, neg, accounts;
	Comparator<Account> compBalance, compID;
	
	public Bank() {
		
		compBalance= new Comparator<Account>() {
			public int compare(Account a1, Account a2) {
				int bal = Double.compare(a1.getBalance(), a2.getBalance());
				if(bal==0)
					return Integer.compare(a1.getC_id(), a2.getC_id());
				return bal;
			}
		};
		compID= new Comparator<Account>() {
			public int compare(Account a1, Account a2) {
				return Integer.compare(a1.getC_id(), a2.getC_id());
			}
		};
		pos = new RedBlackMax<Account>(compBalance);
		neg = new RedBlackMax<Account>(compBalance);
		accounts = new RedBlackMax<Account>(compID);
		
		
	}
	
	public void add(String name, int id, int c_id, double balance) {
		Account a;
		a = new Account(name, id, c_id, balance);
		accounts.put(a);
		System.out.println("put account " + a + "and now let's find it");
		System.out.println(findAccountByC_ID(c_id));
		if(balance>=0) {
			pos.put(a);
		} else {
			neg.put(a);
		}
	}
	public void remove(int c_id) {
		Account a;
		
		a = findAccountByC_ID(c_id);
		accounts.delete(a);
		if(a.balance>=0) {
			pos.delete(a);
		} else {
			neg.delete(a);
		}
		
	}
	public void deposit(String name, int c_id, double amount) {
		Account a;
		a=findAccountByC_ID(c_id);
		if(a==null) {
			System.out.println("no account number " + c_id);
			return;
		}
		if(a.balance>=0) {
			pos.delete(a);
		} else {
			neg.delete(a);
		}
		a.balance+=amount;
		if(a.balance>=0) {
			pos.put(a);
		} else {
			neg.put(a);
		}
	}
	public Account findAccountByC_ID(int c_id) {
		Account query;
		query = new Account("query", 0, c_id,0);
		return accounts.get(query);
		
	}
	public Account getMax() {
		return pos.max()!=null? pos.max() : neg.max();
	}
	public String printMinus() {
		String print="";
		for(Account acc : neg) {
			print+=acc.toString();
		}
		return print;
	}
	
	
}
