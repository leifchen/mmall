package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.pojo.User;
import com.mmall.service.OrderService;
import com.mmall.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 订单Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-03-08
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/create.do")
    public JsonResult<OrderVO> create(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return orderService.create(user.getId(), shippingId);
    }

    @RequestMapping(value = "/cancel.do")
    public JsonResult<String> cancel(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return orderService.cancel(user.getId(), orderNo);
    }

    @RequestMapping(value = "/get_order_cart_product.do")
    public JsonResult getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping(value = "/detail.do")
    public JsonResult detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderDetail(user.getId(), orderNo);
    }

    @RequestMapping(value = "/list.do")
    public JsonResult<PageInfo> list(HttpSession session,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderList(user.getId(), pageNum, pageSize);
    }
}
