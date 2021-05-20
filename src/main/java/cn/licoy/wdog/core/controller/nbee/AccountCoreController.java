package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.AccountCoreAddDTO;
import cn.licoy.wdog.core.dto.nbee.AccountCoreUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindAccountCoreDTO;
import cn.licoy.wdog.core.entity.nbee.AccountCore;
import cn.licoy.wdog.core.service.nbee.AccountCoreService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author mc
 * @version Sat Apr 17 19:14:07 2021
 */
@RestController
@RequestMapping(value="/AccountCore")
public class AccountCoreController  extends AppotBaseController{

   @Autowired
   private AccountCoreService AccountCoreService;
   public AccountCoreService getAccountCoreService() {
      return AccountCoreService;
   }
   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取AccountCore数据")
    @SysLogs("分页获取AccountCore数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "AccountCore获取过滤条件") FindAccountCoreDTO findAccountCoreDTO){
        return ResponseResult.e(ResponseCode.OK,AccountCoreService.getAllAccountCoreBySplitPage(findAccountCoreDTO));
    }

    @PostMapping(value = {"/panel"})
    @ApiOperation(value = "panel")
    @SysLogs("分页获取AccountCore数据")
    public ResponseResult panel(@RequestBody @Validated @ApiParam(value = "getPanel") FindAccountCoreDTO findAccountCoreDTO){
        AccountCore accountCore= AccountCoreService.findAccountCoreById("1");
        return ResponseResult.e(ResponseCode.OK,accountCore);
    }

    @PostMapping(value = {"/companyPanel"})
    @ApiOperation(value = "companyPanel")
    @SysLogs("分页获取AccountCore数据")
    public ResponseResult companyPanel(@RequestBody @Validated @ApiParam(value = "companyPanel") FindAccountCoreDTO findAccountCoreDTO){
        AccountCore accountCore= AccountCoreService.findAccountCoreById("2");
        return ResponseResult.e(ResponseCode.OK,accountCore);
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取AccountCore信息")
    @SysLogs("根据ID获取AccountCore信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "AccountCoreID") String id){
        return ResponseResult.e(ResponseCode.OK,AccountCoreService.findAccountCoreById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定AccountCore")
    @SysLogs("锁定AccountCore")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "AccountCore标识ID") String id){
        AccountCoreService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除AccountCore")
    @SysLogs("删除AccountCore")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "AccountCore标识ID") String id){
        AccountCoreService.removeAccountCore(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加AccountCore")
    @SysLogs("添加AccountCore")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "AccountCore数据") AccountCoreAddDTO addDTO){
        AccountCoreService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新AccountCore")
    @SysLogs("更新AccountCore")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "AccountCore标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "AccountCore数据") AccountCoreUpdateDTO updateDTO){
        AccountCoreService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
