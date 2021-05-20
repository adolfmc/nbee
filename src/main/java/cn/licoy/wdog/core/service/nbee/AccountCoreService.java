package cn.licoy.wdog.core.service.nbee;
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