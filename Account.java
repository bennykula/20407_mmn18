
public class Account {
	String name;
	int id;
	int c_id;
	double balance;
	//public RedBlackMax<Account>.Node idTree, balTree;
	
	public Account() {
		this("Israerl Israeli", 0,0,0);
	}
	public Account(String name, int id, int c_id, double balance) {
		this.name = name;
		this.id = id;
		this.c_id = c_id;
		this.balance = balance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getC_id() {
		return c_id;
	}
	public void setC_id(int c_id) {
		this.c_id = c_id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Account [name=" + name + ", id=" + id + ", c_id=" + c_id + ", balance=" + balance + "]";
	}
}
