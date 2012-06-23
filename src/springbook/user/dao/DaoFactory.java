package springbook.user.dao;

/*
 * 1.5.1 오브젝트 팩토리를 이용한 스프링 IoC
 * 		애플리케이션 컨텍스트와 설정정보
 * 		Bean : 스프링이 제어권을 가지고 직접 만들고 관계를 부여하는 오브젝트
 * 		Bean Factory : 빈의 생성과 관계설정 같은 제어를 담당하는 IoC 오브젝트
 * 		Application Context : IoC 방식을 따라 만들어고 좀 더 확장된 빈 팩토리
 */

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration	// 애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시 
public class DaoFactory {
	
	@Bean			// 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
	public UserDao userDao() {
		UserDao dao = new UserDao();
		dao.setDataSource(dataSource());
		return dao;
	}
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/springbook?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("winter");
		
		return dataSource;
	}

	@Bean
	public ConnectionMaker connectionMaker() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		return connectionMaker;
	}
}
