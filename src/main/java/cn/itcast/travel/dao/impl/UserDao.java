package cn.itcast.travel.dao.impl;

import cn.itcast.travel.domain.User;

public interface UserDao {
    User findUser(User user);

    void savenUser(User user);

    void updateUser(User user);

    void updateStatusWithCode(User user);

}
