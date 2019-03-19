package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.pojo.User;
import com.mmall.service.CartService;
import com.mmall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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

    @RequestMapping("/add.do")
    public JsonResult<CartVO> add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.add(user.getId(), productId, count);
    }

    @RequestMapping("/update.do")
    public JsonResult<CartVO> update(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.update(user.getId(), productId, count);
    }

    @RequestMapping("/delete_product.do")
    public JsonResult<CartVO> deleteProduct(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping("/list.do")
    public JsonResult<CartVO> list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.list(user.getId());
    }

    @RequestMapping("/select_all.do")
    public JsonResult<CartVO> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnselect(user.getId(), null, Const.Cart.CHECKED);
    }

    @RequestMapping("/un_select_all.do")
    public JsonResult<CartVO> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnselect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("/select.do")
    public JsonResult<CartVO> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnselect(user.getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping("/un_select.do")
    public JsonResult<CartVO> unSelect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnselect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("/get_cart_product_count.do")
    public JsonResult<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.success(0);
        }
        return cartService.getCartProductCount(user.getId());
    }
}
