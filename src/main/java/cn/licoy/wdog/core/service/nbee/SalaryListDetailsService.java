package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.SalaryListDetailsAddDTO;import cn.licoy.wdog.core.dto.nbee.FindSalaryListDetailsDTO;import cn.licoy.wdog.core.vo.nbee.SalaryListDetailsVO;import cn.licoy.wdog.core.dto.nbee.SalaryListDetailsUpdateDTO;import cn.licoy.wdog.core.entity.nbee.SalaryListDetails;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface SalaryListDetailsService extends IService<SalaryListDetails>{    /**
     * 根据ID查找SalaryListDetails
     * @param id ID
     * @return SalaryListDetails
     */
    SalaryListDetails findSalaryListDetailsById(String id);


    /**
     * 获取所有SalaryListDetails（分页）
     * @param findSalaryListDetailsDTO 过滤条件
     * @return RequestResult
     */
    Page<SalaryListDetailsVO> getAllSalaryListDetailsBySplitPage(FindSalaryListDetailsDTO findSalaryListDetailsDTO);

    /**
     * SalaryListDetails状态改变
     * @param userId SalaryListDetailsID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除SalaryListDetails
     * @param userId SalaryListDetailsID
     */
    void removeSalaryListDetails(String userId);

    /**
     * 添加SalaryListDetails
     * @param addDTO SalaryListDetails数据DTO
     */
    void add(SalaryListDetailsAddDTO addDTO);

    /**
     * 更新SalaryListDetails数据
     * @param id SalaryListDetailsid
     * @param updateDTO SalaryListDetails数据DTO
     */
    void update(String id, SalaryListDetailsUpdateDTO updateDTO);
};