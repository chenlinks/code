 package com.cl.house.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cl.house.common.model.House;
import com.cl.house.common.page.PageParams;
import com.cl.house.filter.LogFilter;
import com.cl.house.service.HouseService;
import com.cl.house.service.RecommendService;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import redis.clients.jedis.Jedis;

/**
 * @author chenling
 * @date 2018/12/17
 */
@Service
public class RecommendServiceImpl implements RecommendService {
	
	private static final String HOT_HOUSE_KEY = "hot_house";

	private static final Logger logger = LogManager.getLogger(LogFilter.class);
	  @Autowired
	  private HouseService houseService;

	  public void increase(Long id) {
	    try {
	      Jedis jedis = new Jedis("127.0.0.1");
	      jedis.zincrby(HOT_HOUSE_KEY, 1.0D, id + "");
	      jedis.zremrangeByRank(HOT_HOUSE_KEY, 0, -11);// 0代表第一个元素,-1代表最后一个元素，保留热度由低到高末尾10个房产
	      jedis.close();
	    } catch (Exception e) {
	      logger.error(e.getMessage(),e);
	    }
	   
	  }

	  public List<Long> getHot() {
	    try {
	      Jedis jedis = new Jedis("127.0.0.1");
	      Set<String> idSet = jedis.zrevrange(HOT_HOUSE_KEY, 0, -1);
	      jedis.close();
	      List<Long> ids = idSet.stream().map(Long::parseLong).collect(Collectors.toList());
	      return ids;
	    } catch (Exception e) {
	      logger.error(e.getMessage(), e);
	      return Lists.newArrayList();
	    }

	  }

	  public List<House> getHotHouse(Integer size) {
	    House query = new House();
	    List<Long> list = getHot();
	    list = list.subList(0, Math.min(list.size(), size));
	    if (list.isEmpty()) {
	      return Lists.newArrayList();
	    }
	    query.setIds(list);
	    final List<Long> order = list;
	    List<House> houses = houseService.queryAndSetImg(query, PageParams.build(size, 1));
	    Ordering<House> houseSort = Ordering.natural().onResultOf(hs -> {
	      return order.indexOf(hs.getId());
	    });
	    return houseSort.sortedCopy(houses);
	  }

	  
	  
	  public List<House> getLastest() {
	    House query = new House();
	    query.setSort("create_time");
	    List<House> houses = houseService.queryAndSetImg(query, new PageParams(8, 1));
	    return houses;
	  }
	
	
}
