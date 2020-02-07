package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.impl.UserDao;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.SqlSessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveUserServiceImpl implements ActiveUserService {
    private UserDao userMapper = SqlSessionUtil.openSession().getMapper(UserDao.class);

    @Override
    public void activeUser(String code) {
        User user = new User();
        user.setCode(code);
        User user1 = userMapper.findUser(user);
        if (user1 != null) {
            //如果用户存在，将当前用户的状态设置成 Y-已激活
            user.setStatus("Y");
            userMapper.updateStatusWithCode(user);
        }
    }
}
