package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.CartService;
import com.mmall.util.BigDecimalUtils;
import com.mmall.util.PropertiesUtils;
import com.mmall.vo.CartProductVO;
import com.mmall.vo.CartVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车Service的实现类
 * <p>
 * @Author LeifChen
 * @Date 2019-03-01
 */
@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public JsonResult<CartVO> add(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return JsonResult.error(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 该商品不在购物车里，表示新增
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartMapper.insert(cartItem);
        } else {
            // 该商品已在购物车里，表示修改
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return list(userId);
    }

    @Override
    public JsonResult<CartVO> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return JsonResult.error(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return list(userId);
    }

    @Override
    public JsonResult<CartVO> deleteProduct(Integer userId, String productIds) {
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)) {
            return JsonResult.error(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(), ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdAndProductIds(userId, productList);
        return list(userId);
    }

    @Override
    public JsonResult<CartVO> list(Integer userId) {
        CartVO cartVO = getCartVOLimit(userId);
        return JsonResult.success(cartVO);
    }

    @Override
    public JsonResult<CartVO> selectOrUnselect(Integer userId, Integer productId, Integer checked) {
        cartMapper.checkedOrUncheckedProduct(userId, productId, checked);
        return list(userId);
    }

    @Override
    public JsonResult<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return JsonResult.success(0);
        }
        return JsonResult.success(cartMapper.selectCartProductCount(userId));
    }

    /**
     * 限制购物车里商品的购买数量
     * @param userId 用户id
     * @return
     */
    private CartVO getCartVOLimit(Integer userId) {
        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();

        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cartItem.getId());
                cartProductVO.setUserId(cartItem.getUserId());
                cartProductVO.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStock(product.getStock());
                    // 判断库存
                    int buyLimitCount;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        // 库存充足
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        // 购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVO.setQuantity(buyLimitCount);
                    // 计算总价
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), cartProductVO.getQuantity()));
                    cartProductVO.setProductChecked(cartItem.getChecked());
                }

                // 状态为勾选，增加到整个的购物车总价中
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setAllChecked(getAllCheckStatus(userId));
        cartVO.setIamgeHost(PropertiesUtils.getProperty("ftp.server.http.prefix", "http://image.mall.com/"));
        return cartVO;
    }

    /**
     * 检查购物车里面的所有商品是否都已被勾选
     * @param userId 用户id
     * @return
     */
    private boolean getAllCheckStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCheckedStatusByUserId(userId) == 0;
    }
}
