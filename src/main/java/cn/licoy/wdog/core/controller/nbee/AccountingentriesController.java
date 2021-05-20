package cn.licoy.wdog.core.controller.nbee;import cn.licoy.wdog.common.annotation.SysLogs;import io.swagger.annotations.ApiImplicitParam;import cn.licoy.wdog.common.bean.ResponseCode;import cn.licoy.wdog.common.bean.ResponseResult;import cn.licoy.wdog.common.controller.AppotBaseController;import cn.licoy.wdog.core.dto.nbee.AccountingentriesAddDTO;import cn.licoy.wdog.core.dto.nbee.FindAccountingentriesDTO;import cn.licoy.wdog.core.dto.nbee.AccountingentriesUpdateDTO;import cn.licoy.wdog.core.service.nbee.AccountingentriesService;import io.swagger.annotations.Api;import com.alibaba.fastjson.JSONObject;import io.swagger.annotations.ApiOperation;import io.swagger.annotations.ApiParam;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.validation.annotation.Validated;import org.springframework.web.bind.annotation.*;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */@RestController@RequestMapping(value="/Accountingentries")public class AccountingentriesController  extends AppotBaseController{   @Autowired   private AccountingentriesService AccountingentriesService;   public AccountingentriesService getAccountingentriesService() {      return AccountingentriesService;   }   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取Accountingentries数据")
    @SysLogs("分页获取Accountingentries数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "Accountingentries获取过滤条件") FindAccountingentriesDTO findAccountingentriesDTO){
        return ResponseResult.e(ResponseCode.OK,AccountingentriesService.getAllAccountingentriesBySplitPage(findAccountingentriesDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取Accountingentries信息")
    @SysLogs("根据ID获取Accountingentries信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "AccountingentriesID") String id){
        return ResponseResult.e(ResponseCode.OK,AccountingentriesService.findAccountingentriesById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定Accountingentries")
    @SysLogs("锁定Accountingentries")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "Accountingentries标识ID") String id){
        AccountingentriesService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除Accountingentries")
    @SysLogs("删除Accountingentries")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "Accountingentries标识ID") String id){
        AccountingentriesService.removeAccountingentries(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加Accountingentries")
    @SysLogs("添加Accountingentries")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "Accountingentries数据") AccountingentriesAddDTO addDTO){
        AccountingentriesService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新Accountingentries")
    @SysLogs("更新Accountingentries")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "Accountingentries标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "Accountingentries数据") AccountingentriesUpdateDTO updateDTO){
        AccountingentriesService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}