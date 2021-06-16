package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找TppTixian
     * @param id ID
     * @return TppTixian
     */
    TppTixian findTppTixianById(String id);


    /**
     * 获取所有TppTixian（分页）
     * @param findTppTixianDTO 过滤条件
     * @return RequestResult
     */
    Page<TppTixianVO> getAllTppTixianBySplitPage(FindTppTixianDTO findTppTixianDTO);

    /**
     * TppTixian状态改变
     * @param userId TppTixianID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除TppTixian
     * @param userId TppTixianID
     */
    void removeTppTixian(String userId);

    /**
     * 添加TppTixian
     * @param addDTO TppTixian数据DTO
     */
    void add(TppTixianAddDTO addDTO);

    /**
     * 更新TppTixian数据
     * @param id TppTixianid
     * @param updateDTO TppTixian数据DTO
     */
    void update(String id, TppTixianUpdateDTO updateDTO);
};