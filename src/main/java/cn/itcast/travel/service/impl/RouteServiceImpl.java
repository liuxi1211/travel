package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.impl.RouteDao;
import cn.itcast.travel.domain.PageInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.SqlSessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteServiceImpl.class);
    private RouteDao routeDao = SqlSessionUtil.openSession().getMapper(RouteDao.class);

    @Override
    public PageInfo<Route> findPageList(int cid, int pageSize, int pageCurrent, String rname) {
        Integer totalCount = routeDao.findRouteCounts(cid, rname);//总条数
        int totalPage = totalCount / pageSize + 1;//总页数
        int pageStart = (pageCurrent - 1) * pageSize;//开始下标
        List<Route> routeList = routeDao.findRouteList(cid, pageStart, pageSize, rname);
        //开始数据结果封装
        PageInfo<Route> resultInfo = new PageInfo<>();
        resultInfo.setPageCurrent(pageCurrent);
        resultInfo.setPageSize(pageSize);
        resultInfo.setData(routeList);
        resultInfo.setTotalCount(totalCount);
        resultInfo.setTotalPage(totalPage);
        return resultInfo;
    }


    @Override
    public Route getRouteDetils(String rid) {
        return routeDao.getRouteDetils(rid);
    }
}
