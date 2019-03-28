package com.mmall.controller.backend;

import com.mmall.common.JsonResult;
import com.mmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理-订单Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-03-11
 */
@RestController
@RequestMapping("/admin/order")
public class OrderManageController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/list.do")
    public JsonResult list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return orderService.manageList(pageNum, pageSize);
    }

    @RequestMapping(value = "/detail.do")
    public JsonResult detail(Long orderNo) {
        return orderService.manageDetail(orderNo);
    }

    @RequestMapping(value = "/search.do")
    public JsonResult search(Long orderNo,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return orderService.manageSearch(orderNo, pageNum, pageSize);
    }

    @RequestMapping(value = "/send_goods.do")
    public JsonResult sendGoods(Long orderNo) {
        return orderService.manageSendGoods(orderNo);
    }
}
