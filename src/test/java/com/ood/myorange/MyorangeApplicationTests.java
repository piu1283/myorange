package com.ood.myorange;

import com.ood.myorange.dao.UserDao;
import com.ood.myorange.pojo.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyorangeApplicationTests {

	@Autowired
	UserDao userDao;

	@Test
	void contextLoads() {
//		List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from departments");
		List<User> users = userDao.selectAll();
		Assert.assertEquals(users.size(), 2);
		System.out.println(users);
	}

}
