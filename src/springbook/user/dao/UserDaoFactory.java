package springbook.user.dao;

/*
 * 1.4 Ioc(Inversion of Contorl)
 * 1.4.1 오브젝트 팩토리
 * 		지금까지는 문제가 많은 초난감 DAO를 깔끔한 구조로 리팩토링.	
 * 		UserDaoTest가 독립적이지 못함.
 * 		UserDao와 ConnectionMaker 구현 클래스의 오브젝트를 만드는 것과,
 * 		그렇게 만들어진 두 개의 오브젝트가 연결돼서 사용될 수 있도록 관계를 맺어주는 것. 
 */

public class UserDaoFactory {
	/*
	 * 	Factory : 객체의 생성 방법을 결정하고 그렇게 만들어진 오브젝트를 돌려주는 오브젝트.
	 * 	애플리케이션의 컴포넌트 역할을 하는 오브젝트와 애플리케이션의 구조를 결정하는
	 * 	오브젝트를 분리했다는 데 가장 의미가 있음.
	 */
	public UserDao userDao() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDao dao = new UserDao(connectionMaker);
		return dao;
	}
}
