package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.JsonResult;
import com.mmall.pojo.Product;
import com.mmall.service.FileService;
import com.mmall.service.ProductService;
import com.mmall.util.PropertiesUtils;
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
    private ProductService productService;
    @Autowired
    private FileService fileService;

    @RequestMapping("/save.do")
    public JsonResult productSave(Product product) {
        return productService.saveOrUpdateProduct(product);
    }

    @RequestMapping("/set_sale_status.do")
    public JsonResult setSaleStatus(Integer productId, Integer status) {
        return productService.setSaleStatus(productId, status);
    }

    @RequestMapping("/detail.do")
    public JsonResult getDetail(Integer productId) {
        return productService.manageProductDetail(productId);
    }

    @RequestMapping("/list.do")
    public JsonResult getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return productService.getProductList(pageNum, pageSize);
    }

    @RequestMapping("/search.do")
    public JsonResult productSearch(String productName,
                                    Integer productId,
                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return productService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @RequestMapping("/upload.do")
    public JsonResult upload(@RequestParam(value = "upload_file", required = false) MultipartFile file,
                             HttpServletRequest request) {

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file, path);
        String url = PropertiesUtils.getProperty("ftp.server.http.prefix") + targetFileName;

        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return JsonResult.success(fileMap);
    }

    @RequestMapping("/richtext_img_upload.do")
    public Map<String, Object> richtextImgUpload(@RequestParam(value = "upload_file", required = false) MultipartFile file,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {

        Map<String, Object> resultMap = Maps.newHashMap();

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
    }
}
