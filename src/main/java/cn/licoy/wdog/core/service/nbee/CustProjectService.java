package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.CustProjectAddDTO;import cn.licoy.wdog.core.dto.nbee.FindCustProjectDTO;import cn.licoy.wdog.core.vo.nbee.CustProjectVO;import cn.licoy.wdog.core.dto.nbee.CustProjectUpdateDTO;import cn.licoy.wdog.core.entity.nbee.CustProject;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface CustProjectService extends IService<CustProject>{    /**
     * 根据ID查找CustProject
     * @param id ID
     * @return CustProject
     */
    CustProject findCustProjectById(String id);


    /**
     * 获取所有CustProject（分页）
     * @param findCustProjectDTO 过滤条件
     * @return RequestResult
     */
    Page<CustProjectVO> getAllCustProjectBySplitPage(FindCustProjectDTO findCustProjectDTO);

    /**
     * CustProject状态改变
     * @param userId CustProjectID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除CustProject
     * @param userId CustProjectID
     */
    void removeCustProject(String userId);

    /**
     * 添加CustProject
     * @param addDTO CustProject数据DTO
     */
    void add(CustProjectAddDTO addDTO);

    /**
     * 更新CustProject数据
     * @param id CustProjectid
     * @param updateDTO CustProject数据DTO
     */
    void update(String id, CustProjectUpdateDTO updateDTO);
};