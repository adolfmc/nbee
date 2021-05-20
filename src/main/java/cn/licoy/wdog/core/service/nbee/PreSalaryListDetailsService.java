package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.PreSalaryListDetailsAddDTO;import cn.licoy.wdog.core.dto.nbee.FindPreSalaryListDetailsDTO;import cn.licoy.wdog.core.vo.nbee.PreSalaryListDetailsVO;import cn.licoy.wdog.core.dto.nbee.PreSalaryListDetailsUpdateDTO;import cn.licoy.wdog.core.entity.nbee.PreSalaryListDetails;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface PreSalaryListDetailsService extends IService<PreSalaryListDetails>{    /**
     * 根据ID查找PreSalaryListDetails
     * @param id ID
     * @return PreSalaryListDetails
     */
    PreSalaryListDetails findPreSalaryListDetailsById(String id);


    /**
     * 获取所有PreSalaryListDetails（分页）
     * @param findPreSalaryListDetailsDTO 过滤条件
     * @return RequestResult
     */
    Page<PreSalaryListDetailsVO> getAllPreSalaryListDetailsBySplitPage(FindPreSalaryListDetailsDTO findPreSalaryListDetailsDTO);

    /**
     * PreSalaryListDetails状态改变
     * @param userId PreSalaryListDetailsID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除PreSalaryListDetails
     * @param userId PreSalaryListDetailsID
     */
    void removePreSalaryListDetails(String userId);

    /**
     * 添加PreSalaryListDetails
     * @param addDTO PreSalaryListDetails数据DTO
     */
    void add(PreSalaryListDetailsAddDTO addDTO);

    /**
     * 更新PreSalaryListDetails数据
     * @param id PreSalaryListDetailsid
     * @param updateDTO PreSalaryListDetails数据DTO
     */
    void update(String id, PreSalaryListDetailsUpdateDTO updateDTO);
};