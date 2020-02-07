package cn.itcast.travel.service.impl;

import cn.itcast.travel.domain.PageInfo;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    PageInfo<Route> findPageList(int cid, int pageSize, int pageCurrent,String rname);

    Route getRouteDetils(String rid);

}
