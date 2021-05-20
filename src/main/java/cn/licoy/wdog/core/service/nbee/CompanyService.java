package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找Company
     * @param id ID
     * @return Company
     */
    Company findCompanyById(String id);


    /**
     * 获取所有Company（分页）
     * @param findCompanyDTO 过滤条件
     * @return RequestResult
     */
    Page<CompanyVO> getAllCompanyBySplitPage(FindCompanyDTO findCompanyDTO);

    /**
     * Company状态改变
     * @param userId CompanyID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Company
     * @param userId CompanyID
     */
    void removeCompany(String userId);

    /**
     * 添加Company
     * @param addDTO Company数据DTO
     */
    void add(CompanyAddDTO addDTO);

    /**
     * 更新Company数据
     * @param id Companyid
     * @param updateDTO Company数据DTO
     */
    void update(String id, CompanyUpdateDTO updateDTO);
};