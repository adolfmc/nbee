package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.ProjectAddDTO;import cn.licoy.wdog.core.dto.nbee.FindProjectDTO;import cn.licoy.wdog.core.vo.nbee.ProjectVO;import cn.licoy.wdog.core.dto.nbee.ProjectUpdateDTO;import cn.licoy.wdog.core.entity.nbee.Project;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface ProjectService extends IService<Project>{    /**
     * 根据ID查找Project
     * @param id ID
     * @return Project
     */
    Project findProjectById(String id);


    /**
     * 获取所有Project（分页）
     * @param findProjectDTO 过滤条件
     * @return RequestResult
     */
    Page<ProjectVO> getAllProjectBySplitPage(FindProjectDTO findProjectDTO);

    /**
     * Project状态改变
     * @param userId ProjectID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Project
     * @param userId ProjectID
     */
    void removeProject(String userId);

    /**
     * 添加Project
     * @param addDTO Project数据DTO
     */
    void add(ProjectAddDTO addDTO);

    /**
     * 更新Project数据
     * @param id Projectid
     * @param updateDTO Project数据DTO
     */
    void update(String id, ProjectUpdateDTO updateDTO);
};