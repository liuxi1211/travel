package cn.itcast.travel.service.impl;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;

public interface UserService {
    ResultInfo findUser(User user);

    void savenUser(User user);

    ResultInfo loginUserRegister(User user);
}
