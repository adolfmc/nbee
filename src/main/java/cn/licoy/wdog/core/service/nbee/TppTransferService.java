package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.TppTransferAddDTO;import cn.licoy.wdog.core.dto.nbee.FindTppTransferDTO;import cn.licoy.wdog.core.vo.nbee.TppTransferVO;import cn.licoy.wdog.core.dto.nbee.TppTransferUpdateDTO;import cn.licoy.wdog.core.entity.nbee.TppTransfer;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Thu Jun 03 20:15:10 2021 */public interface TppTransferService extends IService<TppTransfer>{    /**
     * 根据ID查找TppTransfer
     * @param id ID
     * @return TppTransfer
     */
    TppTransfer findTppTransferById(String id);


    /**
     * 获取所有TppTransfer（分页）
     * @param findTppTransferDTO 过滤条件
     * @return RequestResult
     */
    Page<TppTransferVO> getAllTppTransferBySplitPage(FindTppTransferDTO findTppTransferDTO);

    /**
     * TppTransfer状态改变
     * @param userId TppTransferID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除TppTransfer
     * @param userId TppTransferID
     */
    void removeTppTransfer(String userId);

    /**
     * 添加TppTransfer
     * @param addDTO TppTransfer数据DTO
     */
    void add(TppTransferAddDTO addDTO);

    /**
     * 更新TppTransfer数据
     * @param id TppTransferid
     * @param updateDTO TppTransfer数据DTO
     */
    void update(String id, TppTransferUpdateDTO updateDTO);
};