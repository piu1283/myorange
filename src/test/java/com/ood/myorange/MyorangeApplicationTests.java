package com.ood.myorange;

import com.ood.myorange.dao.UserDao;
import com.ood.myorange.pojo.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

@SpringBootTest
class MyorangeApplicationTests {

	@Autowired
	UserDao userDao;

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void testUserDao() {
		List<User> users = userDao.selectAll();
		Assert.assertEquals(users.size(), 2);
		System.out.println(users);
	}

	@Test
	void testRedis() {
		// operate 'string' type data
		ValueOperations<String, String> valueStr = redisTemplate.opsForValue();
		// insert set
		valueStr.set("goodsProdu","BMW");
		// redis get
		String goodsName = valueStr.get("goodsProdu");
		System.out.println(goodsName);
		//insert multiple data
		Map<String,String> map = new HashMap<>();
		map.put("goodsName","BMWCar");
		map.put("goodsPrice","88888");
		map.put("goodsId","88");

		valueStr.multiSet(map);
		//get multiple data
		List<String>list = new ArrayList<>();
		list.add("goodsName");
		list.add("goodsPrice");
		list.add("goodsId");
		list.add("goodsProdu");

		List<String> listKeys = valueStr.multiGet(list);
		for (String key : listKeys) {
			System.out.println(key);
		}
	}

}
