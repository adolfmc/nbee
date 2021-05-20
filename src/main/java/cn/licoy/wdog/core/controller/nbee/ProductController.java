package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.FindProductDTO;
import cn.licoy.wdog.core.dto.nbee.ProductAddDTO;
import cn.licoy.wdog.core.dto.nbee.ProductUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.Product;
import cn.licoy.wdog.core.service.nbee.ProductService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mc
 * @version Sat Apr 17 19:14:07 2021
 */
@RestController
@RequestMapping(value="/Product")
public class ProductController  extends AppotBaseController{

   @Autowired
   private ProductService ProductService;
   public ProductService getProductService() {
      return ProductService;
   }
    @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取Product数据")
    @SysLogs("分页获取Product数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "Product获取过滤条件") FindProductDTO findProductDTO){
        return ResponseResult.e(ResponseCode.OK,ProductService.getAllProductBySplitPage(findProductDTO));
    }


    @PostMapping(value = {"/all"})
    @ApiOperation(value = "分页获取Product数据")
    @SysLogs("分页获取Product数据")
    public ResponseResult getall(@RequestBody @Validated @ApiParam(value = "Product获取过滤条件") FindProductDTO findProductDTO){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        List<Map> list = new ArrayList() ;
        for ( Product company:ProductService.selectList(wrapper) ){
            Map<String,String> m = new HashMap<>();
            m.put("value",company.getName()) ;
            m.put("label",company.getName()) ;
            list.add(m);
        }
        return ResponseResult.e(ResponseCode.OK,list);
    }


    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取Product信息")
    @SysLogs("根据ID获取Product信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "ProductID") String id){
        return ResponseResult.e(ResponseCode.OK,ProductService.findProductById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定Product")
    @SysLogs("锁定Product")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "Product标识ID") String id){
        ProductService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除Product")
    @SysLogs("删除Product")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "Product标识ID") String id){
        ProductService.removeProduct(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加Product")
    @SysLogs("添加Product")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "Product数据") ProductAddDTO addDTO){
        ProductService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新Product")
    @SysLogs("更新Product")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "Product标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "Product数据") ProductUpdateDTO updateDTO){
        ProductService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
