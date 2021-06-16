package cn.licoy.wdog.core.service.nbee;
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