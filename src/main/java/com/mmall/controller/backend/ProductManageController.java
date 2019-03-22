package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.JsonResult;
import com.mmall.controller.common.UserCheck;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.FileService;
import com.mmall.service.ProductService;
import com.mmall.service.UserService;
import com.mmall.util.CookieUtils;
import com.mmall.util.JsonUtils;
import com.mmall.util.PropertiesUtils;
import com.mmall.util.RedisPoolUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 后台管理-商品Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-02-28
 */
@RestController
@RequestMapping("/admin/product")
public class ProductManageController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserCheck userCheck;

    @RequestMapping("/save.do")
    public JsonResult productSave(HttpServletRequest httpServletRequest, Product product) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return productService.saveOrUpdateProduct(product);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping("/set_sale_status.do")
    public JsonResult setSaleStatus(HttpServletRequest httpServletRequest, Integer productId, Integer status) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return productService.setSaleStatus(productId, status);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping("/detail.do")
    public JsonResult getDetail(HttpServletRequest httpServletRequest, Integer productId) {
        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return productService.manageProductDetail(productId);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping("/list.do")
    public JsonResult getList(HttpServletRequest httpServletRequest,
                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return productService.getProductList(pageNum, pageSize);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping("/search.do")
    public JsonResult productSearch(HttpServletRequest httpServletRequest,
                                    String productName,
                                    Integer productId,
                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            return productService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping("/upload.do")
    public JsonResult upload(HttpServletRequest httpServletRequest,
                             @RequestParam(value = "upload_file", required = false) MultipartFile file,
                             HttpServletRequest request) {

        JsonResult response = userCheck.checkAdminRoleLogin(httpServletRequest);
        if(response.isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            String url = PropertiesUtils.getProperty("ftp.server.http.prefix") + targetFileName;

            Map<String, String> fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return JsonResult.success(fileMap);
        } else {
            return JsonResult.error("无权限操作");
        }
    }

    @RequestMapping("/richtext_img_upload.do")
    public Map<String, Object> richtextImgUpload(HttpServletRequest httpServletRequest,
                                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {

        Map<String, Object> resultMap = Maps.newHashMap();
        String loginToken = CookieUtils.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        String userJsonStr = RedisPoolUtils.get(loginToken);
        User user = JsonUtils.string2Obj(userJsonStr, User.class);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }

        if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }

            String url = PropertiesUtils.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }
}
