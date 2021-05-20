package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找SalaryListDetails
     * @param id ID
     * @return SalaryListDetails
     */
    SalaryListDetails findSalaryListDetailsById(String id);


    /**
     * 获取所有SalaryListDetails（分页）
     * @param findSalaryListDetailsDTO 过滤条件
     * @return RequestResult
     */
    Page<SalaryListDetailsVO> getAllSalaryListDetailsBySplitPage(FindSalaryListDetailsDTO findSalaryListDetailsDTO);

    /**
     * SalaryListDetails状态改变
     * @param userId SalaryListDetailsID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除SalaryListDetails
     * @param userId SalaryListDetailsID
     */
    void removeSalaryListDetails(String userId);

    /**
     * 添加SalaryListDetails
     * @param addDTO SalaryListDetails数据DTO
     */
    void add(SalaryListDetailsAddDTO addDTO);

    /**
     * 更新SalaryListDetails数据
     * @param id SalaryListDetailsid
     * @param updateDTO SalaryListDetails数据DTO
     */
    void update(String id, SalaryListDetailsUpdateDTO updateDTO);
};