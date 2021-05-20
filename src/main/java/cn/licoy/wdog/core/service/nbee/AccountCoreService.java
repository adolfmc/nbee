package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.AccountCoreAddDTO;import cn.licoy.wdog.core.dto.nbee.FindAccountCoreDTO;import cn.licoy.wdog.core.vo.nbee.AccountCoreVO;import cn.licoy.wdog.core.dto.nbee.AccountCoreUpdateDTO;import cn.licoy.wdog.core.entity.nbee.AccountCore;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface AccountCoreService extends IService<AccountCore>{    /**
     * 根据ID查找AccountCore
     * @param id ID
     * @return AccountCore
     */
    AccountCore findAccountCoreById(String id);


    /**
     * 获取所有AccountCore（分页）
     * @param findAccountCoreDTO 过滤条件
     * @return RequestResult
     */
    Page<AccountCoreVO> getAllAccountCoreBySplitPage(FindAccountCoreDTO findAccountCoreDTO);

    /**
     * AccountCore状态改变
     * @param userId AccountCoreID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除AccountCore
     * @param userId AccountCoreID
     */
    void removeAccountCore(String userId);

    /**
     * 添加AccountCore
     * @param addDTO AccountCore数据DTO
     */
    void add(AccountCoreAddDTO addDTO);

    /**
     * 更新AccountCore数据
     * @param id AccountCoreid
     * @param updateDTO AccountCore数据DTO
     */
    void update(String id, AccountCoreUpdateDTO updateDTO);
};