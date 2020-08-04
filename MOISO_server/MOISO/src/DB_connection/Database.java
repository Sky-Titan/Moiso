package DB_connection;




import java.sql.*;

import model.User;

public class Database {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	//private static final String URL  = "jdbc:mysql://localhost:3306/MOISO_server";
    private static final String URL = "jdbc:mysql://52.79.222.198:3306/MOISO_server";	
    private static final String USER ="moisoserver";
    private static final String PW = "ehfmfmrdb";    
    
    private static Connection conn = null;	
    private static Statement stmt = null;
    
    public Database() {}    
   
    /* 회원정보 */
	public static User getAccountInfo(String _id) {
		System.out.println("회원 정보 검색중...");
		User results = null;
		connect();
		try {
			String sql = "SELECT * FROM userTbl WHERE user_id = '"+_id+"'";
			ResultSet rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				results = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
			}
			rs.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
		return results;
	}
	
	/* 회원 탈퇴 */
	public void deleteAccount(User _user) {
		connect();
		
		try {
			String sql = "DELETE FROM userTbl WHERE user_id = '"+_user.getId()+"';";
			int res = stmt.executeUpdate(sql);
			
			if(res > 0) {
				System.out.println("회원 탈퇴가 정상적으로 탈퇴되었습니다.");
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
	}
	
	/* 맴버 확인*/
	public boolean isMember(User _user) {
		System.out.println("ismember()");
		boolean result = false;
		connect();
		
		try {
			String sql = "SELECT user_id FORM userTbl WHERE user_id='"+_user.getId()+"'";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("회원 검색중...");
			System.out.println("rs = " + rs);
			
			if(rs.next()) {
				System.out.println("이미 회원 입니다.");
				result = true;
			}
			else {
				result = false;
			}
			
			rs.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
		return result;
		//있으면 1 없으면 0
	}
	
	/* 회원정보 변경 */
	public boolean updateAccount(String _id, String _pw, String _email, String _nickname){
		boolean result = false;
		
		connect();
		
		try {
			String sql = "UPDATE userTbl SET user_pw = '"+_pw+"' , user_email = '"+_email+"', user_nickname = '"+_nickname+"'" ;
			int res = stmt.executeUpdate(sql);
			
			if(res > 0) {
				result = true;
				System.out.println("회원 정보가 변경 되었습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		disconnect();
		return result;
	}

	/* 회원 가입 */
	
	public boolean signup(User _user) {
		System.out.println("signup()");
		boolean result = false;
		connect();
		_user.showUser();
		try {
			String sql = "INSERT INTO userTbl (user_id,user_pw,user_email,user_nickname) VALUES ('"+_user.getId()+"','"+_user.getPassword()+"','"+_user.getEmail()+"','"+_user.getNickname()+"')";
			
			System.out.println(sql);
			int res = stmt.executeUpdate(sql);				
			if(res > 0) {
				result = true;
			}
			else {
				result = false;
			}
			conn.commit();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();		
		return result;
	}
	
	/* 로그인*/
	public boolean login(String _id, String _pw) {	
		boolean login_check = false;
		connect();	
		System.out.println("로그인 중...");
		try {			
			String sql = "SELECT user_id, user_pw FROM userTbl WHERE user_id='"+_id+"'";
			ResultSet rs = stmt.executeQuery(sql);			
			
			if(!rs.next()) {
				System.out.println("해당 아이디가 없습니다.");	
			}
			else {
				if(rs.getString(2).equals(_pw)) {
					System.out.println("로그인 성공");
					login_check = true;
				}				
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		disconnect();
		return login_check;
	}
	
	/*세션아이디 전송*/
	
	/* DB연결 */
    public static void connect(){     	
    	try {
            Class.forName(DRIVER);
            System.out.println("데이터베이스 연결");
        } catch (ClassNotFoundException e) {
            System.out.println("데이터베이스 없음");
        }
    	
    	try {
			conn = DriverManager.getConnection(URL, USER, PW);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
        try {			
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(java.sql.Connection.TRANSACTION_SERIALIZABLE);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
        
    }
    

    public static void disconnect() {         
        try {
        	stmt.close();
        	conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        
    }    
	
}
