package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.AttendanceDetailsAddDTO;import cn.licoy.wdog.core.dto.nbee.FindAttendanceDetailsDTO;import cn.licoy.wdog.core.vo.nbee.AttendanceDetailsVO;import cn.licoy.wdog.core.dto.nbee.AttendanceDetailsUpdateDTO;import cn.licoy.wdog.core.entity.nbee.AttendanceDetails;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface AttendanceDetailsService extends IService<AttendanceDetails>{    /**
     * 根据ID查找AttendanceDetails
     * @param id ID
     * @return AttendanceDetails
     */
    AttendanceDetails findAttendanceDetailsById(String id);


    /**
     * 获取所有AttendanceDetails（分页）
     * @param findAttendanceDetailsDTO 过滤条件
     * @return RequestResult
     */
    Page<AttendanceDetailsVO> getAllAttendanceDetailsBySplitPage(FindAttendanceDetailsDTO findAttendanceDetailsDTO);

    /**
     * AttendanceDetails状态改变
     * @param userId AttendanceDetailsID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除AttendanceDetails
     * @param userId AttendanceDetailsID
     */
    void removeAttendanceDetails(String userId);

    /**
     * 添加AttendanceDetails
     * @param addDTO AttendanceDetails数据DTO
     */
    void add(AttendanceDetailsAddDTO addDTO);

    /**
     * 更新AttendanceDetails数据
     * @param id AttendanceDetailsid
     * @param updateDTO AttendanceDetails数据DTO
     */
    void update(String id, AttendanceDetailsUpdateDTO updateDTO);
};