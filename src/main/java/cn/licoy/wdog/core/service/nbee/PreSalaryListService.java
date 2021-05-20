package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.PreSalaryListAddDTO;import cn.licoy.wdog.core.dto.nbee.FindPreSalaryListDTO;import cn.licoy.wdog.core.vo.nbee.PreSalaryListVO;import cn.licoy.wdog.core.dto.nbee.PreSalaryListUpdateDTO;import cn.licoy.wdog.core.entity.nbee.PreSalaryList;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface PreSalaryListService extends IService<PreSalaryList>{    /**
     * 根据ID查找PreSalaryList
     * @param id ID
     * @return PreSalaryList
     */
    PreSalaryList findPreSalaryListById(String id);


    /**
     * 获取所有PreSalaryList（分页）
     * @param findPreSalaryListDTO 过滤条件
     * @return RequestResult
     */
    Page<PreSalaryListVO> getAllPreSalaryListBySplitPage(FindPreSalaryListDTO findPreSalaryListDTO);

    /**
     * PreSalaryList状态改变
     * @param userId PreSalaryListID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除PreSalaryList
     * @param userId PreSalaryListID
     */
    void removePreSalaryList(String userId);

    /**
     * 添加PreSalaryList
     * @param addDTO PreSalaryList数据DTO
     */
    void add(PreSalaryListAddDTO addDTO);

    /**
     * 更新PreSalaryList数据
     * @param id PreSalaryListid
     * @param updateDTO PreSalaryList数据DTO
     */
    void update(String id, PreSalaryListUpdateDTO updateDTO);
};