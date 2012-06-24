package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public class UserDao {
	private DataSource dataSource;
	
	/*
	 * DataSource
	 * 	자바에서 DB 커넥션을 가져오는 오브젝트의 기능을 추상화해서 비슷한 용도로
	 *  사용할 수 있게 만들어진 인터페이스
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void add(final User user) throws ClassNotFoundException, SQLException {
		// 내부 클래스에서 외부의 변수를 사용할 때는 외부 변수는 반드시 final로 선언
		// user 파라미터는 메소드 내부에서 변경될 일이 없으므로 final로 선언해도 무방
		
		// add() 메소드 내부에 선언된 로컬 클래스
		class AddStatement implements StatementStrategy {
			User user;
			
			public AddStatement(User user) {
				this.user = user;
			}
			
			public PreparedStatement makePreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = 
						c.prepareStatement("insert into users(id, name, password)" +
								" values(?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}
		}
		
		StatementStrategy st = new AddStatement(user);
		jdbcContextWithStatementStrategy(st);
	}

	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection c = this.dataSource.getConnection();
		PreparedStatement ps = c
				.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();
		
		User user = null;
		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}

		rs.close();
		ps.close();
		c.close();
		
		if (user == null) throw new EmptyResultDataAccessException(1);
		
		return user;
	}

	public void deleteAll() throws SQLException {
		// 선정한 전략 클래스의 오브젝트 생성
		StatementStrategy st = new DeleteAllStatement();
		// 컨텍스트 호출. 전략 오브젝트 전달
		jdbcContextWithStatementStrategy(st);
	}
	
	public int getCount() throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			
			ps = c.prepareStatement("select count(*) from users");
			
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	/*
	 * StatementStrategy stmt : 클라이언트가 컨텍스트를 호출할 때 넘겨줄 전략 파라미터
	 */
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws 
			SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
			
			ps = stmt.makePreparedStatement(c);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
			if (c != null) { try { c.close(); } catch (SQLException e) {} }
		}
	}

}
