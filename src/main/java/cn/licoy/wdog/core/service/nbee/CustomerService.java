package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.CustomerAddDTO;import cn.licoy.wdog.core.dto.nbee.FindCustomerDTO;import cn.licoy.wdog.core.vo.nbee.CustomerVO;import cn.licoy.wdog.core.dto.nbee.CustomerUpdateDTO;import cn.licoy.wdog.core.entity.nbee.Customer;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface CustomerService extends IService<Customer>{    /**
     * 根据ID查找Customer
     * @param id ID
     * @return Customer
     */
    Customer findCustomerById(String id);


    /**
     * 获取所有Customer（分页）
     * @param findCustomerDTO 过滤条件
     * @return RequestResult
     */
    Page<CustomerVO> getAllCustomerBySplitPage(FindCustomerDTO findCustomerDTO);

    /**
     * Customer状态改变
     * @param userId CustomerID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Customer
     * @param userId CustomerID
     */
    void removeCustomer(String userId);

    /**
     * 添加Customer
     * @param addDTO Customer数据DTO
     */
    void add(CustomerAddDTO addDTO);

    /**
     * 更新Customer数据
     * @param id Customerid
     * @param updateDTO Customer数据DTO
     */
    void update(String id, CustomerUpdateDTO updateDTO);
};