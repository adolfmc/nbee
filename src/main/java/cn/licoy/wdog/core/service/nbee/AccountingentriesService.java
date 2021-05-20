package cn.licoy.wdog.core.service.nbee;
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