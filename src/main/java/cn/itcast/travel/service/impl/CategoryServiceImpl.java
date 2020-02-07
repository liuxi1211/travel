package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.impl.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JedisUtil;
import cn.itcast.travel.util.SqlSessionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private CategoryDao categoryDao = SqlSessionUtil.openSession().getMapper(CategoryDao.class);

    /**
     * 查询全部栏目，先从redis中查找，如果redis中没有在从数据库中查询
     *
     * @return
     */
    @Override
    public String findAll() {
        Jedis jedis = JedisUtil.getJedis();
        String categorys = jedis.get("categorys");
        try {
            if (categorys == null) {
                LOGGER.debug("从数据库中取");
                //从数据库取
                List<Category> all = categoryDao.findAll();
                //将数据库中取出的数据保存在redis中
                ObjectMapper objectMapper = new ObjectMapper();
                categorys = objectMapper.writeValueAsString(all);
                jedis.set("categorys", categorys);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.debug(categorys);
        return categorys;
    }
}
