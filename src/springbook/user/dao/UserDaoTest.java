package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {
	/*
	 * JUnit : 자바 테스팅 프레임워크
	 * 	테스트가 main() 메소드로 만들어졌다는 건 제어권을 직접 갖는다는 의미.
	 * 	JUnit 프레임워크가 요구하는 두 가지 조건
	 * 	1. 메소드가 public으로 선언되어야 함
	 * 	2. @Test라는 애노테이션이 필요
	 */
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext(
				"applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		User user = new User();
		user.setId("hjwon");
		user.setName("원희재");
		user.setPassword("winter");

		dao.add(user);
		assertThat(dao.getCount(), is(1));
			
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
	
	public static void main(String[] args) {
		JUnitCore.main("springbook.user.dao.UserDaoTest");
	}
}
