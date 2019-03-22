package com.mmall.controller.portal;

import com.mmall.common.JsonResult;
import com.mmall.controller.common.UserCheck;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 收货地址Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-03-05
 */
@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;
    @Autowired
    private UserCheck userCheck;

    @RequestMapping(value = "/add.do")
    public JsonResult add(HttpServletRequest httpServletRequest, Shipping shipping) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return shippingService.add(user.getId(), shipping);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/update.do")
    public JsonResult update(HttpServletRequest httpServletRequest, Shipping shipping) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return shippingService.update(user.getId(), shipping);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/delete.do")
    public JsonResult delete(HttpServletRequest httpServletRequest, Integer shippingId) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return shippingService.delete(user.getId(), shippingId);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/select.do")
    public JsonResult select(HttpServletRequest httpServletRequest, Integer shippingId) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return shippingService.select(user.getId(), shippingId);
        } else {
            return response;
        }
    }

    @RequestMapping(value = "/list.do")
    public JsonResult list(HttpServletRequest httpServletRequest,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return shippingService.list(user.getId(), pageNum, pageSize);
        } else {
            return response;
        }
    }
}
