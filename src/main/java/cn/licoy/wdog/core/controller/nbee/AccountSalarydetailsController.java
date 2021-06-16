package cn.licoy.wdog.core.controller.nbee;import cn.licoy.wdog.common.annotation.SysLogs;import io.swagger.annotations.ApiImplicitParam;import cn.licoy.wdog.common.bean.ResponseCode;import cn.licoy.wdog.common.bean.ResponseResult;import cn.licoy.wdog.common.controller.AppotBaseController;import cn.licoy.wdog.core.dto.nbee.AccountSalarydetailsAddDTO;import cn.licoy.wdog.core.dto.nbee.FindAccountSalarydetailsDTO;import cn.licoy.wdog.core.dto.nbee.AccountSalarydetailsUpdateDTO;import cn.licoy.wdog.core.service.nbee.AccountSalarydetailsService;import io.swagger.annotations.Api;import com.alibaba.fastjson.JSONObject;import io.swagger.annotations.ApiOperation;import io.swagger.annotations.ApiParam;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.validation.annotation.Validated;import org.springframework.web.bind.annotation.*;/** * @author mc * @version Fri Jun 11 10:20:20 2021 */@RestController@RequestMapping(value="/AccountSalarydetails")public class AccountSalarydetailsController  extends AppotBaseController{   @Autowired   private AccountSalarydetailsService AccountSalarydetailsService;   public AccountSalarydetailsService getAccountSalarydetailsService() {      return AccountSalarydetailsService;   }   @PostMapping(value = {"/list"})
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