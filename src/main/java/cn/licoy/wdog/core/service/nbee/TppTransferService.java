package cn.licoy.wdog.core.service.nbee;
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