package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.AccountingentriesAddDTO;import cn.licoy.wdog.core.dto.nbee.FindAccountingentriesDTO;import cn.licoy.wdog.core.vo.nbee.AccountingentriesVO;import cn.licoy.wdog.core.dto.nbee.AccountingentriesUpdateDTO;import cn.licoy.wdog.core.entity.nbee.Accountingentries;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */public interface AccountingentriesService extends IService<Accountingentries>{    /**
     * 根据ID查找Accountingentries
     * @param id ID
     * @return Accountingentries
     */
    Accountingentries findAccountingentriesById(String id);


    /**
     * 获取所有Accountingentries（分页）
     * @param findAccountingentriesDTO 过滤条件
     * @return RequestResult
     */
    Page<AccountingentriesVO> getAllAccountingentriesBySplitPage(FindAccountingentriesDTO findAccountingentriesDTO);

    /**
     * Accountingentries状态改变
     * @param userId AccountingentriesID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Accountingentries
     * @param userId AccountingentriesID
     */
    void removeAccountingentries(String userId);

    /**
     * 添加Accountingentries
     * @param addDTO Accountingentries数据DTO
     */
    void add(AccountingentriesAddDTO addDTO);

    /**
     * 更新Accountingentries数据
     * @param id Accountingentriesid
     * @param updateDTO Accountingentries数据DTO
     */
    void update(String id, AccountingentriesUpdateDTO updateDTO);
};