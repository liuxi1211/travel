package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.service.impl.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/category/*")
public class CategoryServlet extends BasicServlet {
    private CategoryService categoryServiceImpl = new CategoryServiceImpl();

    public void findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String categorys = categoryServiceImpl.findAll();
        response.getWriter().write(categorys);
    }

}
