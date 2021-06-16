package cn.licoy.wdog.core.controller.nbee;
    @ApiOperation(value = "分页获取AccountSalarydetails数据")
    @SysLogs("分页获取AccountSalarydetails数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "AccountSalarydetails获取过滤条件") FindAccountSalarydetailsDTO findAccountSalarydetailsDTO){
        return ResponseResult.e(ResponseCode.OK,AccountSalarydetailsService.getAllAccountSalarydetailsBySplitPage(findAccountSalarydetailsDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取AccountSalarydetails信息")
    @SysLogs("根据ID获取AccountSalarydetails信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "AccountSalarydetailsID") String id){
        return ResponseResult.e(ResponseCode.OK,AccountSalarydetailsService.findAccountSalarydetailsById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定AccountSalarydetails")
    @SysLogs("锁定AccountSalarydetails")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "AccountSalarydetails标识ID") String id){
        AccountSalarydetailsService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除AccountSalarydetails")
    @SysLogs("删除AccountSalarydetails")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "AccountSalarydetails标识ID") String id){
        AccountSalarydetailsService.removeAccountSalarydetails(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加AccountSalarydetails")
    @SysLogs("添加AccountSalarydetails")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "AccountSalarydetails数据") AccountSalarydetailsAddDTO addDTO){
        AccountSalarydetailsService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新AccountSalarydetails")
    @SysLogs("更新AccountSalarydetails")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "AccountSalarydetails标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "AccountSalarydetails数据") AccountSalarydetailsUpdateDTO updateDTO){
        AccountSalarydetailsService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}