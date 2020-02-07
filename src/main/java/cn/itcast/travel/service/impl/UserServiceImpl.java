package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.impl.UserDao;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.SqlSessionUtil;
import cn.itcast.travel.util.UuidUtil;
import org.apache.ibatis.session.SqlSession;

import javax.xml.transform.Result;
import java.lang.annotation.ElementType;

public class UserServiceImpl implements UserService {
    private UserDao userMapper = SqlSessionUtil.openSession().getMapper(UserDao.class);

    //用户登陆验证
    @Override
    public ResultInfo loginUserRegister(User user) {
        User resultUser = userMapper.findUser(user);
        ResultInfo resultInfo = new ResultInfo();
        if (resultUser != null) {
            if (resultUser.getStatus().equalsIgnoreCase("Y")) {
                //用户已经激活
                resultInfo.setFlag(true);
            } else {
                //用户未激活
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("当前用户未激活，请登陆邮箱激活");
            }
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("当前用户不存在或密码不正确");
        }
        return resultInfo;
    }

    @Override
    public ResultInfo findUser(User user) {
        User findUser = new User();
        findUser.setUsername(user.getUsername());
        User user1 = userMapper.findUser(findUser);
        if (user1 == null) {
            //没有重名
            savenUser(user);
            return new ResultInfo(true);
        } else {
            //重名
            return new ResultInfo(false, "该用户名已存在！");
        }
    }

    @Override
    public void savenUser(User user) {
        String uuid = UuidUtil.getUuid();
        user.setCode(uuid);//设置激活码
        user.setStatus("N");
        userMapper.savenUser(user);
        String text = "<a href=http://localhost:8089/travel/user/activeUser?code=" + user.getCode() + ">立即登录</a>";
        MailUtils.sendMail(user.getEmail(), text, "一刀旅游网激活");
    }
}
