package model;

public class User {
	private String Id;
	private String Password;
	private String Password_confirm;
	private String E_mail;
	private String Nickname;
	
	public User() {}
	
	public User(String _id, String _pw, String _email, String _nickname) {
		this.Id 		= _id;
		this.Password 	= _pw;
		this.E_mail 	= _email;
		this.Nickname 	= _nickname;
	}
	
	public User(String _id, String _pw, String _pwc, String _email, String _nickname) {
		this.Id				 	= _id;
		this.Password 			= _pw;
		this.Password_confirm 	= _pwc;
		this.E_mail 			= _email;
		this.Nickname 			= _nickname;
	}
	
	public String getId() {
		return Id;
	}
	
	public void setId(String _id) {
		this.Id = _id;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public void setPassword(String _pw) {
		this.Password = _pw;
	}
	
	public String getPassword_confirm() {
		return Password_confirm;
	}
	
	public String getEmail() {
		return E_mail;
	}
	
	public void setEmail(String _email) {
		this.E_mail = _email;
	}
	
	public String getNickname() {
		return Nickname;
	}
	
	public void setNickname(String _nickname) {
		this.Nickname = _nickname;
	}
	
	public void showUser() {
		System.out.println("id="+getId());
		System.out.println("pw="+getPassword());
		System.out.println("email="+getEmail());
		System.out.println("nick="+getNickname());
	}
}
