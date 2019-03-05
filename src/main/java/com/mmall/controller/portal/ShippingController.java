package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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

    @RequestMapping(value = "/add.do")
    public JsonResult add(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return shippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "/update.do")
    public JsonResult<String> update(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return shippingService.update(user.getId(), shipping);
    }

    @RequestMapping(value = "/delete.do")
    public JsonResult<String> delete(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return shippingService.delete(user.getId(), shippingId);
    }

    @RequestMapping(value = "/select.do")
    public JsonResult<Shipping> select(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return shippingService.select(user.getId(), shippingId);
    }

    @RequestMapping(value = "/list.do")
    public JsonResult<PageInfo> list(HttpSession session,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
