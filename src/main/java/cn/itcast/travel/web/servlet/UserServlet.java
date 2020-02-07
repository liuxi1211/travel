package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.impl.ActiveUserService;
import cn.itcast.travel.service.impl.ActiveUserServiceImpl;
import cn.itcast.travel.service.impl.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

@WebServlet("/user/*")
public class UserServlet extends BasicServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);

    /**
     * 用户注册
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER"); //防止验证码复用
        ResultInfo resultInfo = null;
        if (checkcode_server != null && checkcode_server.equalsIgnoreCase(request.getParameter("check"))) {
            //校验用户名时候存在，如果存在就返回报错信息，
            //如果不存在就保存用户信息，并返回成功
            Map<String, String[]> parameterMap = request.getParameterMap();
            User user = new User();
            try {
                BeanUtils.populate(user, parameterMap);
                UserService userService = new UserServiceImpl();
                resultInfo = userService.findUser(user);
            } catch (Exception e) {
                LOGGER.debug(e.getMessage());
                resultInfo = new ResultInfo(false, e.getMessage());
            }
        } else {
            //验证码不正确
            resultInfo = new ResultInfo(false, "验证码不正确！");
        }
        writeValueAsString(response, resultInfo);
    }

    /**
     * 登陆
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        String check = request.getParameter("check");
        String autoLogin = request.getParameter("autoLogin");//自动登陆开关
        ResultInfo resultInfo = new ResultInfo();
        if (checkcode_server != null && check != null && checkcode_server.equalsIgnoreCase(check)) {
            try {
                Map<String, String[]> parameterMap = request.getParameterMap();
                User user = new User();
                BeanUtils.populate(user, parameterMap);
                UserService userService = new UserServiceImpl();
                resultInfo = userService.loginUserRegister(user);
                if (resultInfo.isFlag()) {
                    //如果登陆成功，将用户信息存放在session中
                    if (autoLogin != null && autoLogin.equalsIgnoreCase("on")) {
                        session.setAttribute("user", user.getUsername());
                        Cookie c = new Cookie("JSESSIONID", session.getId());
                        c.setMaxAge(7 * 24 * 60 * 60);
                        response.addCookie(c);
                        session.setMaxInactiveInterval(7 * 24 * 60 * 60);
                    } else {
                        session.setAttribute("user", user.getUsername());
                    }
                }
            } catch (Exception e) {
                LOGGER.debug(e.getMessage());
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg(e.getMessage());
            }
        } else {
            //验证码校验失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码校验失败");
        }
        writeValueAsString(response, resultInfo);
    }

    /**
     * 登出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * 登陆状态查询
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void loginShow(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        ResultInfo resultInfo = new ResultInfo();
        if (user != null) {
            resultInfo.setFlag(true);
            resultInfo.setData(user);
            LOGGER.debug("用户 " + user + "登陆");
        } else {
            resultInfo.setFlag(false);
            LOGGER.debug("未登陆！");
        }
        writeValueAsString(response, resultInfo);
    }

    /**
     * 用户激活
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1.根据激活码查询当前用户是否存在
        try {
            ActiveUserService activeUserService = new ActiveUserServiceImpl();
            String code = request.getParameter("code");
            if (code != null && code.length() != 0) {
                activeUserService.activeUser(code);
                response.getWriter().write("已激活成功,点击跳转" +
                        "<a href='login.html'>登陆</a>" + "页面");
            }
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            response.getWriter().write("激活失败");
        }
    }

    /**
     * 生成验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void checkCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //服务器通知浏览器不要缓存
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");

        //在内存中创建一个长80，宽30的图片，默认黑色背景
        //参数一：长
        //参数二：宽
        //参数三：颜色
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //获取画笔
        Graphics g = image.getGraphics();
        //设置画笔颜色为灰色
        g.setColor(Color.GRAY);
        //填充图片
        g.fillRect(0, 0, width, height);

        //产生4个随机验证码，12Ey
        String checkCode = getCheckCode();
        //将验证码放入HttpSession中
        request.getSession().setAttribute("CHECKCODE_SERVER", checkCode);

        //设置画笔颜色为黄色
        g.setColor(Color.YELLOW);
        //设置字体的小大
        g.setFont(new Font("黑体", Font.BOLD, 24));
        //向图片上写入验证码
        g.drawString(checkCode, 15, 25);

        //将内存中的图片输出到浏览器
        //参数一：图片对象
        //参数二：图片的格式，如PNG,JPG,GIF
        //参数三：图片输出到哪里去
        ImageIO.write(image, "PNG", response.getOutputStream());
    }

    /**
     * @return 生成4位验证码
     */
    private String getCheckCode() {
        String base = "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
        int size = base.length();
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= 4; i++) {
            //产生0到size-1的随机值
            int index = r.nextInt(size);
            //在base字符串中获取下标为index的字符
            char c = base.charAt(index);
            //将c放入到StringBuffer中去
            sb.append(c);
        }
        return sb.toString();
    }
}
