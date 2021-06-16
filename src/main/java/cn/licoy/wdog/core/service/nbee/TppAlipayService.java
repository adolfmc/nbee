package cn.licoy.wdog.core.service.nbee;import cn.licoy.wdog.common.service.QueryService;import cn.licoy.wdog.core.dto.nbee.TppAlipayAddDTO;import cn.licoy.wdog.core.dto.nbee.FindTppAlipayDTO;import cn.licoy.wdog.core.vo.nbee.TppAlipayVO;import cn.licoy.wdog.core.dto.nbee.TppAlipayUpdateDTO;import cn.licoy.wdog.core.entity.nbee.TppAlipay;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.plugins.Page;import com.baomidou.mybatisplus.service.IService;/** * @author mc * @version Thu Jun 03 11:04:40 2021 */public interface TppAlipayService extends IService<TppAlipay>{    /**
     * 根据ID查找TppAlipay
     * @param id ID
     * @return TppAlipay
     */
    TppAlipay findTppAlipayById(String id);


    /**
     * 获取所有TppAlipay（分页）
     * @param findTppAlipayDTO 过滤条件
     * @return RequestResult
     */
    Page<TppAlipayVO> getAllTppAlipayBySplitPage(FindTppAlipayDTO findTppAlipayDTO);

    /**
     * TppAlipay状态改变
     * @param userId TppAlipayID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除TppAlipay
     * @param userId TppAlipayID
     */
    void removeTppAlipay(String userId);

    /**
     * 添加TppAlipay
     * @param addDTO TppAlipay数据DTO
     */
    void add(TppAlipayAddDTO addDTO);

    /**
     * 更新TppAlipay数据
     * @param id TppAlipayid
     * @param updateDTO TppAlipay数据DTO
     */
    void update(String id, TppAlipayUpdateDTO updateDTO);
};