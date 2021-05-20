package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.FindProjectDTO;
import cn.licoy.wdog.core.dto.nbee.ProjectAddDTO;
import cn.licoy.wdog.core.dto.nbee.ProjectUpdateDTO;
import cn.licoy.wdog.core.service.nbee.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author mc
 * @version Sat Apr 17 19:14:07 2021
 */
@RestController
@RequestMapping(value="/Project")
public class ProjectController  extends AppotBaseController{

   @Autowired
   private ProjectService ProjectService;
   public ProjectService getProjectService() {
      return ProjectService;
   }
   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取Project数据")
    @SysLogs("分页获取Project数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "Project获取过滤条件") FindProjectDTO findProjectDTO){
        return ResponseResult.e(ResponseCode.OK,ProjectService.getAllProjectBySplitPage(findProjectDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取Project信息")
    @SysLogs("根据ID获取Project信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "ProjectID") String id){
        return ResponseResult.e(ResponseCode.OK,ProjectService.findProjectById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定Project")
    @SysLogs("锁定Project")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "Project标识ID") String id){
        ProjectService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除Project")
    @SysLogs("删除Project")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "Project标识ID") String id){
        ProjectService.removeProject(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加Project")
    @SysLogs("添加Project")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "Project数据") ProjectAddDTO addDTO){
       if(addDTO.getId()!=null && !"".equals(addDTO.getId())){
           ProjectUpdateDTO updateDTO = new ProjectUpdateDTO();
           BeanUtils.copyProperties(addDTO , updateDTO);
           ProjectService.update(addDTO.getId(),updateDTO);
       }else{
           ProjectService.add(addDTO);
       }

        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新Project")
    @SysLogs("更新Project")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "Project标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "Project数据") ProjectUpdateDTO updateDTO){
        ProjectService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
