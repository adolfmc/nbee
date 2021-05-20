package cn.licoy.wdog.core.controller.nbee;
    @ApiOperation(value = "分页获取SalaryListDetails数据")
    @SysLogs("分页获取SalaryListDetails数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "SalaryListDetails获取过滤条件") FindSalaryListDetailsDTO findSalaryListDetailsDTO){
        return ResponseResult.e(ResponseCode.OK,SalaryListDetailsService.getAllSalaryListDetailsBySplitPage(findSalaryListDetailsDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取SalaryListDetails信息")
    @SysLogs("根据ID获取SalaryListDetails信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "SalaryListDetailsID") String id){
        return ResponseResult.e(ResponseCode.OK,SalaryListDetailsService.findSalaryListDetailsById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定SalaryListDetails")
    @SysLogs("锁定SalaryListDetails")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "SalaryListDetails标识ID") String id){
        SalaryListDetailsService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除SalaryListDetails")
    @SysLogs("删除SalaryListDetails")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "SalaryListDetails标识ID") String id){
        SalaryListDetailsService.removeSalaryListDetails(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加SalaryListDetails")
    @SysLogs("添加SalaryListDetails")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "SalaryListDetails数据") SalaryListDetailsAddDTO addDTO){
        SalaryListDetailsService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新SalaryListDetails")
    @SysLogs("更新SalaryListDetails")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "SalaryListDetails标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "SalaryListDetails数据") SalaryListDetailsUpdateDTO updateDTO){
        SalaryListDetailsService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}