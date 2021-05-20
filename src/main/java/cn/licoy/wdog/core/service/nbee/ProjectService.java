package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找Project
     * @param id ID
     * @return Project
     */
    Project findProjectById(String id);


    /**
     * 获取所有Project（分页）
     * @param findProjectDTO 过滤条件
     * @return RequestResult
     */
    Page<ProjectVO> getAllProjectBySplitPage(FindProjectDTO findProjectDTO);

    /**
     * Project状态改变
     * @param userId ProjectID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Project
     * @param userId ProjectID
     */
    void removeProject(String userId);

    /**
     * 添加Project
     * @param addDTO Project数据DTO
     */
    void add(ProjectAddDTO addDTO);

    /**
     * 更新Project数据
     * @param id Projectid
     * @param updateDTO Project数据DTO
     */
    void update(String id, ProjectUpdateDTO updateDTO);
};