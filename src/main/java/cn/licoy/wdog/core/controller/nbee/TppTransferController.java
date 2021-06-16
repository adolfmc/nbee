package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.FindTppTransferDTO;
import cn.licoy.wdog.core.dto.nbee.TppTransferAddDTO;
import cn.licoy.wdog.core.dto.nbee.TppTransferUpdateDTO;
import cn.licoy.wdog.core.service.nbee.TppTransferService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mc
 * @version Thu Jun 03 20:15:10 2021
 */
@RestController
@RequestMapping(value="/TppTransfer")
public class TppTransferController  extends AppotBaseController{

   @Autowired
   private TppTransferService TppTransferService;

    public TppTransferService getTppTransferService() {
        return TppTransferService;
    }








   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取TppTransfer数据")
    @SysLogs("分页获取TppTransfer数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "TppTransfer获取过滤条件") FindTppTransferDTO findTppTransferDTO){
        return ResponseResult.e(ResponseCode.OK,TppTransferService.getAllTppTransferBySplitPage(findTppTransferDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取TppTransfer信息")
    @SysLogs("根据ID获取TppTransfer信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "TppTransferID") String id){
        return ResponseResult.e(ResponseCode.OK,TppTransferService.findTppTransferById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定TppTransfer")
    @SysLogs("锁定TppTransfer")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "TppTransfer标识ID") String id){
        TppTransferService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除TppTransfer")
    @SysLogs("删除TppTransfer")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "TppTransfer标识ID") String id){
        TppTransferService.removeTppTransfer(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加TppTransfer")
    @SysLogs("添加TppTransfer")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "TppTransfer数据") TppTransferAddDTO addDTO){
        TppTransferService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新TppTransfer")
    @SysLogs("更新TppTransfer")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "TppTransfer标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "TppTransfer数据") TppTransferUpdateDTO updateDTO){
        TppTransferService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
