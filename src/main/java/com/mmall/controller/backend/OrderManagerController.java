package com.mmall.controller.backend;

import com.mmall.common.JsonResult;
import com.mmall.controller.common.UserCheck;
import com.mmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台管理-订单Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-03-11
 */
@RestController
@RequestMapping("/admin/order")
public class OrderManagerController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserCheck userCheck;

    @RequestMapping(value = "/list.do")
    public JsonResult list(HttpServletRequest httpServletRequest,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return orderService.manageList(pageNum, pageSize);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/detail.do")
    public JsonResult detail(HttpServletRequest httpServletRequest, Long orderNo) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return orderService.manageDetail(orderNo);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/search.do")
    public JsonResult search(HttpServletRequest httpServletRequest,
                                       Long orderNo,
                                       @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return orderService.manageSearch(orderNo, pageNum, pageSize);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/send_goods.do")
    public JsonResult sendGoods(HttpServletRequest httpServletRequest, Long orderNo) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return orderService.manageSendGoods(orderNo);
        } else {
            return response;
        }
    }
}
