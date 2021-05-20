package cn.licoy.wdog.core.controller.nbee;import cn.licoy.wdog.common.annotation.SysLogs;import io.swagger.annotations.ApiImplicitParam;import cn.licoy.wdog.common.bean.ResponseCode;import cn.licoy.wdog.common.bean.ResponseResult;import cn.licoy.wdog.common.controller.AppotBaseController;import cn.licoy.wdog.core.dto.nbee.PreSalaryListDetailsAddDTO;import cn.licoy.wdog.core.dto.nbee.FindPreSalaryListDetailsDTO;import cn.licoy.wdog.core.dto.nbee.PreSalaryListDetailsUpdateDTO;import cn.licoy.wdog.core.service.nbee.PreSalaryListDetailsService;import io.swagger.annotations.Api;import com.alibaba.fastjson.JSONObject;import io.swagger.annotations.ApiOperation;import io.swagger.annotations.ApiParam;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.validation.annotation.Validated;import org.springframework.web.bind.annotation.*;/** * @author mc * @version Sat Apr 17 19:14:07 2021 */@RestController@RequestMapping(value="/PreSalaryListDetails")public class PreSalaryListDetailsController  extends AppotBaseController{   @Autowired   private PreSalaryListDetailsService PreSalaryListDetailsService;   public PreSalaryListDetailsService getPreSalaryListDetailsService() {      return PreSalaryListDetailsService;   }   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取PreSalaryListDetails数据")
    @SysLogs("分页获取PreSalaryListDetails数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "PreSalaryListDetails获取过滤条件") FindPreSalaryListDetailsDTO findPreSalaryListDetailsDTO){
        return ResponseResult.e(ResponseCode.OK,PreSalaryListDetailsService.getAllPreSalaryListDetailsBySplitPage(findPreSalaryListDetailsDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取PreSalaryListDetails信息")
    @SysLogs("根据ID获取PreSalaryListDetails信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "PreSalaryListDetailsID") String id){
        return ResponseResult.e(ResponseCode.OK,PreSalaryListDetailsService.findPreSalaryListDetailsById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定PreSalaryListDetails")
    @SysLogs("锁定PreSalaryListDetails")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "PreSalaryListDetails标识ID") String id){
        PreSalaryListDetailsService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除PreSalaryListDetails")
    @SysLogs("删除PreSalaryListDetails")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "PreSalaryListDetails标识ID") String id){
        PreSalaryListDetailsService.removePreSalaryListDetails(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加PreSalaryListDetails")
    @SysLogs("添加PreSalaryListDetails")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "PreSalaryListDetails数据") PreSalaryListDetailsAddDTO addDTO){
        PreSalaryListDetailsService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新PreSalaryListDetails")
    @SysLogs("更新PreSalaryListDetails")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "PreSalaryListDetails标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "PreSalaryListDetails数据") PreSalaryListDetailsUpdateDTO updateDTO){
        PreSalaryListDetailsService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}