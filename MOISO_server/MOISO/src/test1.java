import DB_connection.Database;
import model.User;

public class test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String id = "세환";
		String pw = "세환123";
		String pwc = "세환123";
		String email = "d잉";
		String nick = "돌";
		User testuser = new User(id,pw,pwc,email,nick);
		
		
		if(new Database().signup(testuser)){
			System.out.println("회원가입 성공");			
		}
		else {
			System.out.println("실패");
		}
		
	}

}
