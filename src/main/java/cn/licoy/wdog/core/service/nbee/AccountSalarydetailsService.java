package cn.licoy.wdog.core.service.nbee;
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