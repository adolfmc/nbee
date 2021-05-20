package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找SalaryList
     * @param id ID
     * @return SalaryList
     */
    SalaryList findSalaryListById(String id);


    /**
     * 获取所有SalaryList（分页）
     * @param findSalaryListDTO 过滤条件
     * @return RequestResult
     */
    Page<SalaryListVO> getAllSalaryListBySplitPage(FindSalaryListDTO findSalaryListDTO);

    /**
     * SalaryList状态改变
     * @param userId SalaryListID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除SalaryList
     * @param userId SalaryListID
     */
    void removeSalaryList(String userId);

    /**
     * 添加SalaryList
     * @param addDTO SalaryList数据DTO
     */
    void add(SalaryListAddDTO addDTO);

    /**
     * 更新SalaryList数据
     * @param id SalaryListid
     * @param updateDTO SalaryList数据DTO
     */
    void update(String id, SalaryListUpdateDTO updateDTO);
};