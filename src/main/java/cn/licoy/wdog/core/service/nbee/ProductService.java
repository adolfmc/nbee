package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.ProductAddDTO;import cn.licoy.wdog.core.dto.nbee.FindProductDTO;import cn.licoy.wdog.core.vo.nbee.ProductVO;import cn.licoy.wdog.core.dto.nbee.ProductUpdateDTO;import cn.licoy.wdog.core.entity.nbee.Product;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface ProductService extends IService<Product>{    /**
     * 根据ID查找Product
     * @param id ID
     * @return Product
     */
    Product findProductById(String id);


    /**
     * 获取所有Product（分页）
     * @param findProductDTO 过滤条件
     * @return RequestResult
     */
    Page<ProductVO> getAllProductBySplitPage(FindProductDTO findProductDTO);

    /**
     * Product状态改变
     * @param userId ProductID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Product
     * @param userId ProductID
     */
    void removeProduct(String userId);

    /**
     * 添加Product
     * @param addDTO Product数据DTO
     */
    void add(ProductAddDTO addDTO);

    /**
     * 更新Product数据
     * @param id Productid
     * @param updateDTO Product数据DTO
     */
    void update(String id, ProductUpdateDTO updateDTO);
};