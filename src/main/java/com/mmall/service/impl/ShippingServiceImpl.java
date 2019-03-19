package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.JsonResult;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 收货地址Service的实现类
 * <p>
 * @Author LeifChen
 * @Date 2019-03-05
 */
@Service(value = "shippingService")
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public JsonResult add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map<String, Integer> result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return JsonResult.success("新建地址成功", result);
        }
        return JsonResult.error("新建地址失败");
    }

    @Override
    public JsonResult<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return JsonResult.success("更新地址成功");
        }
        return JsonResult.error("更新地址失败");
    }

    @Override
    public JsonResult<String> delete(Integer userId, Integer shippingId) {
        int rowCount = shippingMapper.deleteByPrimaryKey(shippingId);
        if (rowCount > 0) {
            return JsonResult.success("删除地址成功");
        }
        return JsonResult.error("删除地址失败");
    }

    @Override
    public JsonResult<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(shippingId, userId);
        if (shipping == null) {
            return JsonResult.error("无法查询到该地址");
        }
        return JsonResult.success("查询地址成功", shipping);
    }

    @Override
    public JsonResult<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return JsonResult.success(pageInfo);
    }
}
