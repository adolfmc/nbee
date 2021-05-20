package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.CompanyAddDTO;
import cn.licoy.wdog.core.dto.nbee.CompanyUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindCompanyDTO;
import cn.licoy.wdog.core.entity.nbee.AccountCore;
import cn.licoy.wdog.core.entity.nbee.Company;
import cn.licoy.wdog.core.service.nbee.AccountCoreService;
import cn.licoy.wdog.core.service.nbee.CompanyService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mc
 * @version Sat Apr 17 19:14:07 2021
 */
@RestController
@RequestMapping(value="/Company")
public class CompanyController  extends AppotBaseController{
    @Autowired
    private AccountCoreService accountCoreService;
   @Autowired
   private CompanyService CompanyService;
   public CompanyService getCompanyService() {
      return CompanyService;
   }
   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取Company数据")
    @SysLogs("分页获取Company数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "Company获取过滤条件") FindCompanyDTO findCompanyDTO){

        return ResponseResult.e(ResponseCode.OK,CompanyService.getAllCompanyBySplitPage(findCompanyDTO));
    }

    @PostMapping(value = {"/all"})
    @ApiOperation(value = "分页获取Product数据")
    @SysLogs("分页获取Product数据")
    public ResponseResult getall(@RequestBody @Validated @ApiParam(value = "Product获取过滤条件") FindCompanyDTO findCompanyDTO){
        EntityWrapper<Company> wrapper = new EntityWrapper<>();
        List<Map> list = new ArrayList() ;
        for ( Company company:CompanyService.selectList(wrapper) ){
            Map<String,String> m = new HashMap<>();
            m.put("value",company.getName()) ;
            m.put("label",company.getName()) ;
            list.add(m);
        }
        return ResponseResult.e(ResponseCode.OK,list);
    }


    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取Company信息")
    @SysLogs("根据ID获取Company信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "CompanyID") String id){
        return ResponseResult.e(ResponseCode.OK,CompanyService.findCompanyById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定Company")
    @SysLogs("锁定Company")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "Company标识ID") String id){
        CompanyService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除Company")
    @SysLogs("删除Company")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "Company标识ID") String id){
        CompanyService.removeCompany(id);

        Company company = CompanyService.selectById(id);
        EntityWrapper<AccountCore> wrapperAccountCore = new EntityWrapper<>();
        wrapperAccountCore.eq("company_id",company.getName()) ;
        wrapperAccountCore.eq("account_type","用人单位") ;
        accountCoreService.delete(wrapperAccountCore);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加Company")
    @SysLogs("添加Company")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "Company数据") CompanyAddDTO addDTO){
        addDTO.setCreateUser(getLoginUserName());

        if(addDTO.getId()==null){
            CompanyService.add(addDTO);

            AccountCore accountCore = new AccountCore();
            accountCore.setCompanyId( addDTO.getName());
            accountCore.setAccountType("用人单位");
            accountCore.setT3feeBanlance( new BigDecimal(0));
            accountCore.setT3payAmount( new BigDecimal(0));
            accountCore.setT3payPreacceptamt( new BigDecimal(0));
            accountCore.setT3creditPayamount( new BigDecimal(0));
            accountCore.setT3creditAmountBanlance( new BigDecimal(0));
            accountCore.setT3creditAmount( new BigDecimal(0));
            accountCore.setT3feeBanlance( new BigDecimal(0));
            accountCore.setT3payAcceptedamt( new BigDecimal(0));
            accountCore.setT3payamountBanlance( new BigDecimal(0));
            accountCore.setT3currentPaynumber( new BigDecimal(0));
            accountCoreService.insertOrUpdate(accountCore);
        }else{
            CompanyUpdateDTO updateDTO = new CompanyUpdateDTO() ;
            BeanUtils.copyProperties(addDTO,updateDTO );
            CompanyService.update(addDTO.getId(),updateDTO);
        }




        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新Company")
    @SysLogs("更新Company")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "Company标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "Company数据") CompanyUpdateDTO updateDTO){
        CompanyService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
