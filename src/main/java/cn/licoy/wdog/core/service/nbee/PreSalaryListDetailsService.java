package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找PreSalaryListDetails
     * @param id ID
     * @return PreSalaryListDetails
     */
    PreSalaryListDetails findPreSalaryListDetailsById(String id);


    /**
     * 获取所有PreSalaryListDetails（分页）
     * @param findPreSalaryListDetailsDTO 过滤条件
     * @return RequestResult
     */
    Page<PreSalaryListDetailsVO> getAllPreSalaryListDetailsBySplitPage(FindPreSalaryListDetailsDTO findPreSalaryListDetailsDTO);

    /**
     * PreSalaryListDetails状态改变
     * @param userId PreSalaryListDetailsID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除PreSalaryListDetails
     * @param userId PreSalaryListDetailsID
     */
    void removePreSalaryListDetails(String userId);

    /**
     * 添加PreSalaryListDetails
     * @param addDTO PreSalaryListDetails数据DTO
     */
    void add(PreSalaryListDetailsAddDTO addDTO);

    /**
     * 更新PreSalaryListDetails数据
     * @param id PreSalaryListDetailsid
     * @param updateDTO PreSalaryListDetails数据DTO
     */
    void update(String id, PreSalaryListDetailsUpdateDTO updateDTO);
};