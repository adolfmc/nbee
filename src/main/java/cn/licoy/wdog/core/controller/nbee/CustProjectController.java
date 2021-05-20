package cn.licoy.wdog.core.controller.nbee;
    @ApiOperation(value = "分页获取CustProject数据")
    @SysLogs("分页获取CustProject数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "CustProject获取过滤条件") FindCustProjectDTO findCustProjectDTO){
        return ResponseResult.e(ResponseCode.OK,CustProjectService.getAllCustProjectBySplitPage(findCustProjectDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取CustProject信息")
    @SysLogs("根据ID获取CustProject信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "CustProjectID") String id){
        return ResponseResult.e(ResponseCode.OK,CustProjectService.findCustProjectById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定CustProject")
    @SysLogs("锁定CustProject")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "CustProject标识ID") String id){
        CustProjectService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除CustProject")
    @SysLogs("删除CustProject")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "CustProject标识ID") String id){
        CustProjectService.removeCustProject(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加CustProject")
    @SysLogs("添加CustProject")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "CustProject数据") CustProjectAddDTO addDTO){
        CustProjectService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新CustProject")
    @SysLogs("更新CustProject")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "CustProject标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "CustProject数据") CustProjectUpdateDTO updateDTO){
        CustProjectService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}