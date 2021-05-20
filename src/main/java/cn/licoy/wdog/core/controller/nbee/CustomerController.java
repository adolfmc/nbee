package cn.licoy.wdog.core.controller.nbee;
    @ApiOperation(value = "分页获取Customer数据")
    @SysLogs("分页获取Customer数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "Customer获取过滤条件") FindCustomerDTO findCustomerDTO){
        return ResponseResult.e(ResponseCode.OK,CustomerService.getAllCustomerBySplitPage(findCustomerDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取Customer信息")
    @SysLogs("根据ID获取Customer信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "CustomerID") String id){
        return ResponseResult.e(ResponseCode.OK,CustomerService.findCustomerById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定Customer")
    @SysLogs("锁定Customer")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "Customer标识ID") String id){
        CustomerService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除Customer")
    @SysLogs("删除Customer")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "Customer标识ID") String id){
        CustomerService.removeCustomer(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加Customer")
    @SysLogs("添加Customer")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "Customer数据") CustomerAddDTO addDTO){
        CustomerService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新Customer")
    @SysLogs("更新Customer")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "Customer标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "Customer数据") CustomerUpdateDTO updateDTO){
        CustomerService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}