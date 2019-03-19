package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.pojo.User;
import com.mmall.service.OrderService;
import com.mmall.service.UserService;
import com.mmall.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/list.do")
    public JsonResult<PageInfo> list(HttpSession session,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return orderService.manageList(pageNum, pageSize);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping(value = "/detail.do")
    public JsonResult<OrderVO> detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return orderService.manageDetail(orderNo);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping(value = "/search.do")
    public JsonResult<PageInfo> search(HttpSession session,
                                       Long orderNo,
                                       @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return orderService.manageSearch(orderNo, pageNum, pageSize);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping(value = "/send_goods.do")
    public JsonResult<String> sendGoods(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return orderService.manageSendGoods(orderNo);
        } else {
            return JsonResult.error("无权限操作");
        }
    }
}
