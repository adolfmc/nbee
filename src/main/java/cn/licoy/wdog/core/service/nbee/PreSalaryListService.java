package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找PreSalaryList
     * @param id ID
     * @return PreSalaryList
     */
    PreSalaryList findPreSalaryListById(String id);


    /**
     * 获取所有PreSalaryList（分页）
     * @param findPreSalaryListDTO 过滤条件
     * @return RequestResult
     */
    Page<PreSalaryListVO> getAllPreSalaryListBySplitPage(FindPreSalaryListDTO findPreSalaryListDTO);

    /**
     * PreSalaryList状态改变
     * @param userId PreSalaryListID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除PreSalaryList
     * @param userId PreSalaryListID
     */
    void removePreSalaryList(String userId);

    /**
     * 添加PreSalaryList
     * @param addDTO PreSalaryList数据DTO
     */
    void add(PreSalaryListAddDTO addDTO);

    /**
     * 更新PreSalaryList数据
     * @param id PreSalaryListid
     * @param updateDTO PreSalaryList数据DTO
     */
    void update(String id, PreSalaryListUpdateDTO updateDTO);
};