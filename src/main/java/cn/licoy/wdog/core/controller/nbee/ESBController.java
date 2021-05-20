package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.AccountCoreAddDTO;
import cn.licoy.wdog.core.dto.nbee.AccountCoreUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindAccountCoreDTO;
import cn.licoy.wdog.core.entity.nbee.AccountCore;
import cn.licoy.wdog.core.entity.nbee.Accountingentries;
import cn.licoy.wdog.core.entity.nbee.Customer;
import cn.licoy.wdog.core.entity.nbee.SalaryListDetails;
import cn.licoy.wdog.core.service.nbee.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author mc
 * @version Sat Apr 17 19:14:07 2021
 */
@RestController
@RequestMapping(value="/ESB")
public class ESBController extends AppotBaseController{
   @Autowired
   private AccountCoreService AccountCoreService;
   @Autowired
   private CustomerService customerService;
   @Autowired
   private SalaryListDetailsService salaryListDetailsService;
   @Autowired
   private AccountCoreService accountCoreService;
   @Autowired
   private AccountingentriesService accountingentriesService;
   @Autowired
   private ProductService productService;

   public AccountCoreService getAccountCoreService() {
      return AccountCoreService;
   }
    @RequestMapping(value = {"/details"})
    @ApiOperation(value = "details")
    @SysLogs("details")
    public ResponseResult details(String name, String idcard, String currentCompany, BigDecimal amount){

        Map<String,Object> vv= new HashMap<String,Object>();
       String allamountsql = "select sum(amount) amount from biz_nbee.biz_salary_list_details where name=? and id_card =? and company =? and pick_status ='已提现'";
       String allamount=jdbcTemplate.queryForMap(allamountsql,name,idcard,currentCompany).get("amount")+"";

        ArrayList result = new ArrayList();
            String sqlmonth = "select date_format(kaoqing_date,'%Y/%m月') datee ,sum(case when pick_status='已提现' then amount  else 0 end) amount   from biz_nbee.biz_salary_list_details where name=? and id_card =? and company =? group by datee";
            String sqldays = "select date_format(pick_time ,'%Y-%m-%d %H:%i:%s') datee,amount,pick_status from biz_nbee.biz_salary_list_details where name=? and id_card =? and company =? and  date_format(kaoqing_date,'%Y/%m月')=? and pick_status='已提现'";
            List<Map<String, Object>> months = jdbcTemplate.queryForList(sqlmonth,name,idcard,currentCompany);
            for(Map<String, Object> m:months ){
                Map<String,Object> map= new HashMap<String,Object>();
                map.put("head",m.get("datee")+"  已提现: ￥ "+m.get("amount")+" 元");
                ArrayList<String[]> body=new ArrayList<String[]>();
                List<Map<String, Object>> days = jdbcTemplate.queryForList(sqldays,name,idcard,currentCompany,m.get("datee").toString());
                for(Map<String, Object> d:days ){
                    body.add(new String[]{"'"+d.get("datee")+"', '+"+d.get("amount")+"', '"+d.get("pick_status")+"'"});
                }

                map.put("body",body);
                map.put("open",true);
                map.put("disabled",true);
                result.add(map);
            }
            vv.put("result",result);
            vv.put("allamount",allamount);
        return ResponseResult.e(ResponseCode.OK,vv);
    }

    @RequestMapping(value = {"/pay"})
    @ApiOperation(value = "pay")
    @SysLogs("pay")
    public ResponseResult pay(String name, String idcard, String currentCompany, BigDecimal amount){
        List companys =null;
        AccountCore account=null;
        //待取现总额
        EntityWrapper<SalaryListDetails> wrapper = new EntityWrapper<>();
        wrapper.eq("name",name);
        wrapper.eq("id_card",idcard);
        wrapper.eq("company",currentCompany);
        wrapper.eq("pick_status","未提现");
        List<SalaryListDetails> salaryListDetails =  salaryListDetailsService.selectList(wrapper);
        if(salaryListDetails.size()==0){
            return ResponseResult.e(ResponseCode.FAIL);
        }
        BigDecimal amount2 = new BigDecimal(0);
        String pickstatus = "已提现";
        for(SalaryListDetails salaryListDetails1:salaryListDetails){
            amount2 = amount2.add( salaryListDetails1.getAmount()) ;
            salaryListDetails1.setPickStatus(pickstatus);
            salaryListDetails1.setPickTime(new Date());
        }
        //1 更新明细状态
        salaryListDetailsService.insertOrUpdateBatch(salaryListDetails);


        //2 更新总账公司信息
        EntityWrapper<AccountCore> wrapperAccountCore = new EntityWrapper<>();
        wrapperAccountCore.eq("company_id",currentCompany) ;
        wrapperAccountCore.eq("account_type","用人单位") ;
        AccountCore accountCompany = accountCoreService.selectOne(wrapperAccountCore) ;
        //已提现
        accountCompany.setT3payAcceptedamt( accountCompany.getT3payAcceptedamt().add(amount2) );
        //待提现
        accountCompany.setT3payPreacceptamt( accountCompany.getT3payPreacceptamt().subtract( amount2 )  );
        //
        accountCoreService.insertOrUpdate(accountCompany);


        //3 更新总账平台信息
        AccountCore accountPlatform = accountCoreService.selectById(1L);
        //pf垫付账户总额
        accountPlatform.setPfpaidallAmount( accountPlatform.getPfpaidallAmount().subtract(amount2) );
        //pf已垫付金额
        accountPlatform.setPfpaidAmount( accountPlatform.getPfpaidAmount().add(amount2 ));
        accountCoreService.insertOrUpdate(accountPlatform);


        //4 添加提现流水
        Accountingentries accountingentries = new Accountingentries();
        accountingentries.setAmount( amount2);
        accountingentries.setDataType("普通客户");
        accountingentries.setBizType("提现");
        accountingentries.setDataId(idcard+" "+name+" "+currentCompany);
        accountingentries.setInId(idcard+" "+name);
        accountingentries.setOutId("平台");
        accountingentries.setDesc(  Calendar.getInstance().getTime().toString()+"=提现申请; 提现金额="+amount2.doubleValue()+";当前公司="+currentCompany );
        accountingentriesService.insertOrUpdate(accountingentries) ;


        //5 更新个人账户
        EntityWrapper<AccountCore> wrapperAccountCorepersion = new EntityWrapper<>();
        wrapperAccountCorepersion.eq("person_idcard",idcard) ;
        wrapperAccountCorepersion.eq("person_name",name) ;
        wrapperAccountCorepersion.eq("company_id",currentCompany) ;
        wrapperAccountCorepersion.eq("account_type","普通客户");
        AccountCore accountPersion = accountCoreService.selectOne(wrapperAccountCorepersion) ;
        //per待提现金额
        accountPersion.setPersonPreacceptamt( accountPersion.getPersonPreacceptamt().subtract(amount2) );
        //per已提现金额
        accountPersion.setPersonAcceptedamt(  accountPersion.getPersonAcceptedamt().add(amount2) );
        accountPersion.setPersonPickstatus(pickstatus);
        accountCoreService.insertOrUpdate(accountPersion);


        return ResponseResult.e(ResponseCode.OK ,accountPersion);
    }


    @RequestMapping(value = {"/login"})
    @ApiOperation(value = "login")
    @SysLogs("login")
    public ResponseResult get(String name,String idcard){
       List companys =null;
       AccountCore account=null;
               // 个人信息
       EntityWrapper<Customer> wrapper = new EntityWrapper<>();
       wrapper.eq("name",name);
       wrapper.eq("id_card ",idcard);
       Customer customer = customerService.selectOne(wrapper);

        return ResponseResult.e(ResponseCode.OK,customer);
    }

    @RequestMapping(value = {"/account"})
    @ApiOperation(value = "account")
    @SysLogs("account")
    public ResponseResult account(String name,String idcard,String currentCompany){
        List companys =null;
        AccountCore account=null;
        // 个人信息
        EntityWrapper<Customer> wrapper = new EntityWrapper<>();
        wrapper.eq("name",name);
        wrapper.eq("id_card ",idcard);
        Customer customer = customerService.selectOne(wrapper);
        if(customer!=null){
            // 公司列表
            String sql ="select company_id,min(person_indate) indate from biz_nbee.biz_account_core bac where 1=1 and bac.person_idcard =? and bac.person_name =? and bac.account_type ='普通客户' group by company_id ";
            companys = jdbcTemplate.queryForList(sql,idcard,name);

            // 账务信息
            EntityWrapper<AccountCore> wrapperaccount = new EntityWrapper<>();
            wrapperaccount.eq("person_name",name);
            wrapperaccount.eq("person_idcard ",idcard);
            wrapperaccount.eq("company_id ",currentCompany);
            wrapperaccount.eq("account_type ","普通客户");
            account = AccountCoreService.selectOne(wrapperaccount);
        }

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("customer",customer);
        result.put("companys",companys);
        result.put("account",account);

        return ResponseResult.e(ResponseCode.OK,result);
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
