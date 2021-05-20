package cn.licoy.wdog.core.service.nbee;
     * 根据ID查找Attendance
     * @param id ID
     * @return Attendance
     */
    Attendance findAttendanceById(String id);


    /**
     * 获取所有Attendance（分页）
     * @param findAttendanceDTO 过滤条件
     * @return RequestResult
     */
    Page<AttendanceVO> getAllAttendanceBySplitPage(FindAttendanceDTO findAttendanceDTO);

    /**
     * Attendance状态改变
     * @param userId AttendanceID
     * @param status 状态码
     */
    void statusChange(String userId, Integer status);

    /**
     * 删除Attendance
     * @param userId AttendanceID
     */
    void removeAttendance(String userId);

    /**
     * 添加Attendance
     * @param addDTO Attendance数据DTO
     */
    void add(AttendanceAddDTO addDTO);

    /**
     * 更新Attendance数据
     * @param id Attendanceid
     * @param updateDTO Attendance数据DTO
     */
    void update(String id, AttendanceUpdateDTO updateDTO);
};