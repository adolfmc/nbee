package cn.licoy.wdog.core.controller.nbee;
    @ApiOperation(value = "分页获取TppTixian数据")
    @SysLogs("分页获取TppTixian数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "TppTixian获取过滤条件") FindTppTixianDTO findTppTixianDTO){
        return ResponseResult.e(ResponseCode.OK,TppTixianService.getAllTppTixianBySplitPage(findTppTixianDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取TppTixian信息")
    @SysLogs("根据ID获取TppTixian信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "TppTixianID") String id){
        return ResponseResult.e(ResponseCode.OK,TppTixianService.findTppTixianById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定TppTixian")
    @SysLogs("锁定TppTixian")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "TppTixian标识ID") String id){
        TppTixianService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除TppTixian")
    @SysLogs("删除TppTixian")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "TppTixian标识ID") String id){
        TppTixianService.removeTppTixian(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加TppTixian")
    @SysLogs("添加TppTixian")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "TppTixian数据") TppTixianAddDTO addDTO){
        TppTixianService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新TppTixian")
    @SysLogs("更新TppTixian")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "TppTixian标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "TppTixian数据") TppTixianUpdateDTO updateDTO){
        TppTixianService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}