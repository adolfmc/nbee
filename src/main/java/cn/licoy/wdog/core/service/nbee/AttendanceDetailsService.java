package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找AttendanceDetails
     * @param id ID
     * @return AttendanceDetails
     */
    AttendanceDetails findAttendanceDetailsById(String id);


    /**
     * 获取所有AttendanceDetails（分页）
     * @param findAttendanceDetailsDTO 过滤条件
     * @return RequestResult
     */
    Page<AttendanceDetailsVO> getAllAttendanceDetailsBySplitPage(FindAttendanceDetailsDTO findAttendanceDetailsDTO);

    /**
     * AttendanceDetails状态改变
     * @param userId AttendanceDetailsID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除AttendanceDetails
     * @param userId AttendanceDetailsID
     */
    void removeAttendanceDetails(String userId);

    /**
     * 添加AttendanceDetails
     * @param addDTO AttendanceDetails数据DTO
     */
    void add(AttendanceDetailsAddDTO addDTO);

    /**
     * 更新AttendanceDetails数据
     * @param id AttendanceDetailsid
     * @param updateDTO AttendanceDetails数据DTO
     */
    void update(String id, AttendanceDetailsUpdateDTO updateDTO);
};