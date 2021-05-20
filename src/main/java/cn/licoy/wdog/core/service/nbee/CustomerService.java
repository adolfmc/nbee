package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找Customer
     * @param id ID
     * @return Customer
     */
    Customer findCustomerById(String id);


    /**
     * 获取所有Customer（分页）
     * @param findCustomerDTO 过滤条件
     * @return RequestResult
     */
    Page<CustomerVO> getAllCustomerBySplitPage(FindCustomerDTO findCustomerDTO);

    /**
     * Customer状态改变
     * @param userId CustomerID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Customer
     * @param userId CustomerID
     */
    void removeCustomer(String userId);

    /**
     * 添加Customer
     * @param addDTO Customer数据DTO
     */
    void add(CustomerAddDTO addDTO);

    /**
     * 更新Customer数据
     * @param id Customerid
     * @param updateDTO Customer数据DTO
     */
    void update(String id, CustomerUpdateDTO updateDTO);
};