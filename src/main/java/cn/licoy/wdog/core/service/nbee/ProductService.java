package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找Product
     * @param id ID
     * @return Product
     */
    Product findProductById(String id);


    /**
     * 获取所有Product（分页）
     * @param findProductDTO 过滤条件
     * @return RequestResult
     */
    Page<ProductVO> getAllProductBySplitPage(FindProductDTO findProductDTO);

    /**
     * Product状态改变
     * @param userId ProductID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Product
     * @param userId ProductID
     */
    void removeProduct(String userId);

    /**
     * 添加Product
     * @param addDTO Product数据DTO
     */
    void add(ProductAddDTO addDTO);

    /**
     * 更新Product数据
     * @param id Productid
     * @param updateDTO Product数据DTO
     */
    void update(String id, ProductUpdateDTO updateDTO);
};