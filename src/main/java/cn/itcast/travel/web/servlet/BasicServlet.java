package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 方法分发Servlet
 * 利用反射方法，设计非常巧妙
 */
public class BasicServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();//获取用户uri
        String method = requestURI.substring(requestURI.lastIndexOf("/") + 1);
        LOGGER.debug(requestURI);
        try {
            Method meth = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
            meth.invoke(this, req, resp);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }
    }

    /**
     * 通用返回
     *
     * @param response 响应流
     * @param object   返回数据
     */
    public void writeValueAsString(HttpServletResponse response, Object object){
            try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), object);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }
}
