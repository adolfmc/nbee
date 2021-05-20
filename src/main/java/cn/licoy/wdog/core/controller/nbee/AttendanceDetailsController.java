package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.AttendanceDetailsAddDTO;
import cn.licoy.wdog.core.dto.nbee.AttendanceDetailsUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindAttendanceDetailsDTO;
import cn.licoy.wdog.core.service.nbee.AttendanceDetailsService;
import cn.licoy.wdog.core.vo.nbee.AttendanceDetailsVO;
import com.baomidou.mybatisplus.plugins.Page;
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
@RequestMapping(value="/AttendanceDetails")
public class AttendanceDetailsController  extends AppotBaseController{

   @Autowired
   private AttendanceDetailsService AttendanceDetailsService;
   public AttendanceDetailsService getAttendanceDetailsService() {
      return AttendanceDetailsService;
   }
   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取AttendanceDetails数据")
    @SysLogs("分页获取AttendanceDetails数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "AttendanceDetails获取过滤条件") FindAttendanceDetailsDTO findAttendanceDetailsDTO){




        return ResponseResult.e(ResponseCode.OK,AttendanceDetailsService.getAllAttendanceDetailsBySplitPage(findAttendanceDetailsDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取AttendanceDetails信息")
    @SysLogs("根据ID获取AttendanceDetails信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "AttendanceDetailsID") String id){
        return ResponseResult.e(ResponseCode.OK,AttendanceDetailsService.findAttendanceDetailsById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定AttendanceDetails")
    @SysLogs("锁定AttendanceDetails")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "AttendanceDetails标识ID") String id){
        AttendanceDetailsService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除AttendanceDetails")
    @SysLogs("删除AttendanceDetails")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "AttendanceDetails标识ID") String id){
        AttendanceDetailsService.removeAttendanceDetails(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加AttendanceDetails")
    @SysLogs("添加AttendanceDetails")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "AttendanceDetails数据") AttendanceDetailsAddDTO addDTO){
        AttendanceDetailsService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新AttendanceDetails")
    @SysLogs("更新AttendanceDetails")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "AttendanceDetails标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "AttendanceDetails数据") AttendanceDetailsUpdateDTO updateDTO){
        AttendanceDetailsService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
