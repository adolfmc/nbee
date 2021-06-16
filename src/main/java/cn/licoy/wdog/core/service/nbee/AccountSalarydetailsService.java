package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.AccountSalarydetailsAddDTO;import cn.licoy.wdog.core.dto.nbee.FindAccountSalarydetailsDTO;import cn.licoy.wdog.core.vo.nbee.AccountSalarydetailsVO;import cn.licoy.wdog.core.dto.nbee.AccountSalarydetailsUpdateDTO;import cn.licoy.wdog.core.entity.nbee.AccountSalarydetails;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Fri Jun 11 10:20:20 2021 */public interface AccountSalarydetailsService extends IService<AccountSalarydetails>{    /**
     * 根据ID查找AccountSalarydetails
     * @param id ID
     * @return AccountSalarydetails
     */
    AccountSalarydetails findAccountSalarydetailsById(String id);


    /**
     * 获取所有AccountSalarydetails（分页）
     * @param findAccountSalarydetailsDTO 过滤条件
     * @return RequestResult
     */
    Page<AccountSalarydetailsVO> getAllAccountSalarydetailsBySplitPage(FindAccountSalarydetailsDTO findAccountSalarydetailsDTO);

    /**
     * AccountSalarydetails状态改变
     * @param userId AccountSalarydetailsID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除AccountSalarydetails
     * @param userId AccountSalarydetailsID
     */
    void removeAccountSalarydetails(String userId);

    /**
     * 添加AccountSalarydetails
     * @param addDTO AccountSalarydetails数据DTO
     */
    void add(AccountSalarydetailsAddDTO addDTO);

    /**
     * 更新AccountSalarydetails数据
     * @param id AccountSalarydetailsid
     * @param updateDTO AccountSalarydetails数据DTO
     */
    void update(String id, AccountSalarydetailsUpdateDTO updateDTO);
};