package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.FindPreSalaryListDTO;
import cn.licoy.wdog.core.dto.nbee.PreSalaryListAddDTO;
import cn.licoy.wdog.core.dto.nbee.PreSalaryListUpdateDTO;
import cn.licoy.wdog.core.service.nbee.PreSalaryListService;
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
@RequestMapping(value="/PreSalaryList")
public class PreSalaryListController  extends AppotBaseController{

   @Autowired
   private PreSalaryListService PreSalaryListService;
   public PreSalaryListService getPreSalaryListService() {
      return PreSalaryListService;
   }
   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取PreSalaryList数据")
    @SysLogs("分页获取PreSalaryList数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "PreSalaryList获取过滤条件") FindPreSalaryListDTO findPreSalaryListDTO){


        return ResponseResult.e(ResponseCode.OK,PreSalaryListService.getAllPreSalaryListBySplitPage(findPreSalaryListDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取PreSalaryList信息")
    @SysLogs("根据ID获取PreSalaryList信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "PreSalaryListID") String id){
        return ResponseResult.e(ResponseCode.OK,PreSalaryListService.findPreSalaryListById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定PreSalaryList")
    @SysLogs("锁定PreSalaryList")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "PreSalaryList标识ID") String id){
        PreSalaryListService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除PreSalaryList")
    @SysLogs("删除PreSalaryList")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "PreSalaryList标识ID") String id){
        PreSalaryListService.removePreSalaryList(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加PreSalaryList")
    @SysLogs("添加PreSalaryList")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "PreSalaryList数据") PreSalaryListAddDTO addDTO){
        PreSalaryListService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新PreSalaryList")
    @SysLogs("更新PreSalaryList")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "PreSalaryList标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "PreSalaryList数据") PreSalaryListUpdateDTO updateDTO){
        PreSalaryListService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
