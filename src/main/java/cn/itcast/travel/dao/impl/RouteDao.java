package cn.itcast.travel.dao.impl;

import cn.itcast.travel.domain.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteDao {
    List<Route> findRouteList(@Param("cid") Integer cid, @Param("pageStart") Integer pageStart,
                              @Param("pageSize") Integer pageSize, @Param("rname") String rname);

    Integer findRouteCounts(@Param("cid") Integer cid, @Param("rname") String rname);

    Route getRouteDetils(String rid);
}
