package UserData;

public class users {
	public String name;
	public String account;
	public String password;
	public users(String name,String account,String password) {
		this.name=name;
		this.account=account;
		this.password=password;
	}
	public String getName() {
		return name;
	}
	public String getAccount() {
		return account;
	}
	public String getPassword() {
		return password;
	}
}
