package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageInfo;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.impl.RouteService;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import cn.itcast.travel.web.filter.EncodingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/route/*")
public class RouteServlet extends BasicServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteServlet.class);
    private RouteService routeService = new RouteServiceImpl();

    /**
     * 列表详情分页查询
     *
     * @param request
     * @param response
     */
    public void findPageList(HttpServletRequest request, HttpServletResponse response) {
        String rname = request.getParameter("rname");//搜索条件
        String pageSizeStr = request.getParameter("pageSize");
        pageSizeStr = pageSizeStr == null ? "10" : pageSizeStr;//如果没有pageSzie，默认每页展示10条
        String pageCurrentStr = request.getParameter("pageCurrent");//当前页码
        pageCurrentStr = pageCurrentStr == null ? "1" : pageCurrentStr;
        Integer pageSize = Integer.valueOf(pageSizeStr);
        Integer pageCurrent = Integer.valueOf(pageCurrentStr);
        String cid = request.getParameter("cid");
        ResultInfo resultInfo = new ResultInfo();
        try {
            PageInfo data = routeService.findPageList(Integer.valueOf(cid), pageSize, pageCurrent, rname);
            resultInfo.setFlag(true);
            resultInfo.setData(data);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg(e.getMessage());
        }
        writeValueAsString(response, resultInfo);
    }

    /**
     * 查询列表详情
     *
     * @param request
     * @param response
     */
    public void getRouteDetils(HttpServletRequest request, HttpServletResponse response) {
        String rid = request.getParameter("rid");
        ResultInfo resultInfo = new ResultInfo();
        try {
            Route route = routeService.getRouteDetils(rid);
            resultInfo.setFlag(true);
            resultInfo.setData(route);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg(e.getMessage());
        }
        writeValueAsString(response, resultInfo);
    }
}
