package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.controller.common.UserCheck;
import com.mmall.pojo.User;
import com.mmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 购物车Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-03-01
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserCheck userCheck;

    @RequestMapping("/add.do")
    public JsonResult add(HttpServletRequest httpServletRequest, Integer count, Integer productId) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.add(user.getId(), productId, count);
        } else {
            return response;
        }
    }

    @RequestMapping("/update.do")
    public JsonResult update(HttpServletRequest httpServletRequest, Integer count, Integer productId) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.update(user.getId(), productId, count);
        } else {
            return response;
        }
    }

    @RequestMapping("/delete_product.do")
    public JsonResult deleteProduct(HttpServletRequest httpServletRequest, String productIds) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.deleteProduct(user.getId(), productIds);
        } else {
            return response;
        }
    }

    @RequestMapping("/list.do")
    public JsonResult list(HttpServletRequest httpServletRequest) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.list(user.getId());
        } else {
            return response;
        }
    }

    @RequestMapping("/select_all.do")
    public JsonResult selectAll(HttpServletRequest httpServletRequest) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.selectOrUnselect(user.getId(), null, Const.Cart.CHECKED);
        } else {
            return response;
        }
    }

    @RequestMapping("/un_select_all.do")
    public JsonResult unSelectAll(HttpServletRequest httpServletRequest) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.selectOrUnselect(user.getId(), null, Const.Cart.UN_CHECKED);
        } else {
            return response;
        }
    }

    @RequestMapping("/select.do")
    public JsonResult select(HttpServletRequest httpServletRequest, Integer productId) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.selectOrUnselect(user.getId(), productId, Const.Cart.CHECKED);
        } else {
            return response;
        }
    }

    @RequestMapping("/un_select.do")
    public JsonResult unSelect(HttpServletRequest httpServletRequest, Integer productId) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.selectOrUnselect(user.getId(), productId, Const.Cart.UN_CHECKED);
        } else {
            return response;
        }
    }

    @RequestMapping("/get_cart_product_count.do")
    public JsonResult<Integer> getCartProductCount(HttpServletRequest httpServletRequest) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return cartService.getCartProductCount(user.getId());
        } else {
            return JsonResult.success(0);
        }
    }
}
