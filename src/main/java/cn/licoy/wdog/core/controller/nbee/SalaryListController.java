package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.FindSalaryListDTO;
import cn.licoy.wdog.core.dto.nbee.SalaryListAddDTO;
import cn.licoy.wdog.core.dto.nbee.SalaryListDetailsAddDTO;
import cn.licoy.wdog.core.dto.nbee.SalaryListUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.*;
import cn.licoy.wdog.core.service.nbee.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author mc
 * @version Sat Apr 17 19:14:07 2021
 */
@RestController
@RequestMapping(value="/SalaryList")
public class SalaryListController  extends AppotBaseController{

   @Autowired
   private SalaryListService SalaryListService;
   @Autowired
   private SalaryListDetailsService salaryListDetailsService;
   @Autowired
   private ProductService productService;
   @Autowired
   private AccountCoreService accountCoreService;
   @Autowired
   private CustomerService customerService;

   public SalaryListService getSalaryListService() {
      return SalaryListService;
   }
    @Value("${filepath}")
    private String filepath;

    /**
     * 处理文件上传
     */
    @PostMapping(value = "/upload")
    @ResponseBody
    public Map<String,String> uploading(@RequestParam("file") MultipartFile file) throws  Exception{
        File targetFile = new File(filepath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        Map<String,String> v = new HashMap<>();
        String file_name_path = System.nanoTime()+"_"+file.getOriginalFilename() ;


        try (FileOutputStream out = new FileOutputStream(filepath +  file_name_path );){
            out.write(file.getBytes());
            v.put("name",file.getOriginalFilename());
            v.put("path",filepath+"/"+file_name_path);
        } catch (Exception e) {
            e.printStackTrace();
            v.put("name","uploading error");
            v.put("path","uploading error");
            return v;
        }


        return v;
    }

   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取SalaryList数据")
    @SysLogs("分页获取SalaryList数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "SalaryList获取过滤条件") FindSalaryListDTO findSalaryListDTO){
        return ResponseResult.e(ResponseCode.OK,SalaryListService.getAllSalaryListBySplitPage(findSalaryListDTO));
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取SalaryList信息")
    @SysLogs("根据ID获取SalaryList信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "SalaryListID") String id){
        return ResponseResult.e(ResponseCode.OK,SalaryListService.findSalaryListById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定SalaryList")
    @SysLogs("锁定SalaryList")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "SalaryList标识ID") String id){
        SalaryListService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除SalaryList")
    @SysLogs("删除SalaryList")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "SalaryList标识ID") String id){
        SalaryListService.removeSalaryList(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/fuhe/{id}"})
    @ApiOperation(value = "删除SalaryList")
    @SysLogs("删除SalaryList")
    public ResponseResult fuhe(@PathVariable("id") @ApiParam(value = "SalaryList标识ID") String id){
        SalaryList salaryList = SalaryListService.selectById(id);
        salaryList.setPayStatus("待发放");
        salaryList.setPaySubstatus("待用人单位发放");
        SalaryListService.insertOrUpdate(salaryList);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/tongg"})
    @ApiOperation(value = "删除SalaryList")
    @SysLogs("删除SalaryList")
    public ResponseResult tongg(@RequestBody @Validated @ApiParam(value = "SalaryList数据") SalaryListAddDTO addDTO ){
        SalaryList salaryList = SalaryListService.selectById(addDTO.getId());
        salaryList.setPayDesc( addDTO.getPayDesc());
        salaryList.setPayAccount( addDTO.getPayAccount() );
        salaryList.setPayStatus("已发放");
        salaryList.setPaySubstatus("用人单位已发放");
        SalaryListService.insertOrUpdate(salaryList);

        EntityWrapper<Product> wrapperProduct = new EntityWrapper<>();
        wrapperProduct.eq("name",salaryList.getProductName());
        Product product = productService.selectOne(wrapperProduct);

        //1  更新公司信息 **************************************************************************
        EntityWrapper<AccountCore> wrapperAccountCore = new EntityWrapper<>();
        wrapperAccountCore.eq("company_id",salaryList.getCompany()) ;
        wrapperAccountCore.eq("account_type","用人单位") ;
        AccountCore accountCompany = accountCoreService.selectOne(wrapperAccountCore) ;
        //更新当前领薪人数
        accountCompany.setT3currentPaynumber( salaryList.getPayNumber());
        //公司手续费账户余额 减少 TODO
        if( "按每笔发放百元 - 每笔发放每百元，按照比例".equals(  product.getFeeModel() )){
            accountCompany.setT3feeBanlance( accountCompany.getT3feeBanlance().subtract(   salaryList.getPayAmount().multiply( product.getFeeModel5V().divide(new BigDecimal(100)) )     ) );
        }
        //已垫付金额
        accountCompany.setT3creditPayamount( accountCompany.getT3creditPayamount().add( salaryList.getPayAmount() )  );
        //剩余可垫付额度
        accountCompany.setT3creditAmountBanlance( accountCompany.getT3creditAmountBanlance().subtract(  salaryList.getPayAmount()  ) );


        //已发放金额
        accountCompany.setT3payAmount( accountCompany.getT3payAmount().add(  salaryList.getPayAmount()  ) );
        //已提现
//        accountCompany.setT3payAcceptedamt();
        //待提现
        accountCompany.setT3payPreacceptamt( accountCompany.getT3payPreacceptamt().add(   salaryList.getPayAmount()   )  );

        accountCoreService.insertOrUpdate(accountCompany);


        //2  更新平台信息 **************************************************************************
        AccountCore accountPlantform = accountCoreService.selectById("1") ;

        //公司收益账户 增加 TODO
        //2.5
        if( "按每笔发放百元 - 每笔发放每百元，按照比例".equals(  product.getFeeModel() )){
            accountPlantform.setPfincomeAccount( accountPlantform.getPfincomeAccount().add(   salaryList.getPayAmount().multiply( product.getFeeModel5V().divide(new BigDecimal(100)) )     ) );

        //2.1
        }else if( "有X天考勤信息，每天发".equals(  product.getFeeModel() )){
            /** 1) 判断该考勤当天是否已收费 **/
            
            /** 2) */
        //2.2
        }else if( "按发放次数 -- 每次发放收(XX)".equals(  product.getFeeModel() )){


        //2.3
        }else if( "按每笔发放小时数 - 每笔发放覆盖的每个工作小时收费".equals(  product.getFeeModel() )){


        //2.4
        }else if( "按每笔发放覆盖天数 - 每笔发放覆盖的天数，每天收取".equals(  product.getFeeModel() )){



        }









        //更新当前领薪人数
        accountPlantform.setT3currentPaynumber( salaryList.getPayNumber());
        accountCoreService.insertOrUpdate(accountPlantform);


        //3  更新个人信息 **************************************************************************
        EntityWrapper<SalaryListDetails> wrapperSalaryListDetails = new EntityWrapper<>();
        wrapperSalaryListDetails.eq("salary_list_id",salaryList.getId());
        List<SalaryListDetails> salaryListDetailss = salaryListDetailsService.selectList(wrapperSalaryListDetails ) ;
        for(SalaryListDetails salaryListDetails : salaryListDetailss){
            EntityWrapper<Customer> wrapperCustomer = new EntityWrapper<>();
            wrapperCustomer.eq("id_card",salaryListDetails.getIdCard());

            Customer customer = customerService.selectOne(wrapperCustomer ) ;
            if(customer==null ){
                customer = new Customer();
                customer.setName( salaryListDetails.getName() );
                customer.setIdCard( salaryListDetails.getIdCard());
                customer.setMobile( salaryListDetails.getMobile() );
            }

            customer.setCurrentCompany( salaryListDetails.getCompany() );
            customerService.insertOrUpdate( customer) ;
        }


        for(SalaryListDetails salaryListDetails : salaryListDetailss){
            EntityWrapper<AccountCore> wrapperaccountCore = new EntityWrapper<>();
            wrapperaccountCore.eq("person_idcard",salaryListDetails.getIdCard());
            wrapperaccountCore.eq("company_id ",salaryListDetails.getCompany());

            AccountCore accountCore = accountCoreService.selectOne(wrapperaccountCore ) ;
            if(accountCore==null ){
                accountCore = new AccountCore();
                accountCore.setAccountType("普通客户");
                accountCore.setPersonIdcard( salaryListDetails.getIdCard());
                accountCore.setPersonName( salaryListDetails.getName());
                accountCore.setPersonMobile( salaryListDetails.getMobile() );
                accountCore.setPersonIndate(new Date());
                accountCore.setPersonUpdatetime(new Date());
                accountCore.setPersonAmount( salaryListDetails.getAmount() );
                accountCore.setPersonPreacceptamt(  salaryListDetails.getAmount() );
                accountCore.setPersonAcceptedamt(new BigDecimal(0));

            }else{
                accountCore.setPersonAmount(  accountCore.getPersonAmount().add(  salaryListDetails.getAmount())  );
                accountCore.setPersonPreacceptamt( accountCore.getPersonPreacceptamt().add(  salaryListDetails.getAmount())  );
                accountCore.setPersonUpdatetime(new Date());
            }

            accountCore.setCompanyId( salaryListDetails.getCompany() );
            accountCoreService.insertOrUpdate( accountCore) ;
        }





        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加SalaryList")
    @SysLogs("添加SalaryList")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "SalaryList数据") SalaryListAddDTO addDTO ) throws Exception {

        List<List<String>> xlssalaryList = readExcel( addDTO.getFileAddress()) ;
        String uuid = UUID.randomUUID().toString().replace("-","");
        BigDecimal payAmount = new BigDecimal(0) ;
        String companyName = null;
        String productName = null;
        String projectName = null;
        String batchNo = null;

        for(int i=0;i<xlssalaryList.size();i++){
            if(i==0){
                continue;
            }
            List<String> rows = xlssalaryList.get(i) ;
            String company = rows.get(0);
            String product_name = rows.get(1);
            String project_name = rows.get(2);
            String kaoqing_datee = rows.get(3);
            String batch_no = rows.get(4);
            String name = rows.get(5);
            String idcard = rows.get(6);
            String mobile = rows.get(7);
            String workdays = rows.get(8);
            String amount = rows.get(9);

            companyName = company;
            productName = product_name ;
            projectName  = project_name;
            batchNo = batch_no ;

            SalaryListDetailsAddDTO salaryListDetailsAddDTO = new SalaryListDetailsAddDTO();
            salaryListDetailsAddDTO.setCompany( company);
            salaryListDetailsAddDTO.setProductName( product_name);
            salaryListDetailsAddDTO.setProjectName( project_name);
            salaryListDetailsAddDTO.setKaoqingDate(DateUtils.parseDate( kaoqing_datee,"YYYY/MM/dd") );
            salaryListDetailsAddDTO.setBatchNo( batch_no);
            salaryListDetailsAddDTO.setName( name);
            salaryListDetailsAddDTO.setIdCard(idcard);
            salaryListDetailsAddDTO.setMobile( mobile);
            salaryListDetailsAddDTO.setWorkdays( new BigDecimal(workdays));
            salaryListDetailsAddDTO.setAmount( new BigDecimal(amount));
            salaryListDetailsAddDTO.setSalaryListId( uuid);
            salaryListDetailsAddDTO.setPickStatus("未提现");
            salaryListDetailsService.add(salaryListDetailsAddDTO);

            payAmount = payAmount.add( new BigDecimal( amount)) ;

        }
        addDTO.setId(uuid);
        addDTO.setBatchName(addDTO.getFileName());
        addDTO.setBatchNo(batchNo);
        addDTO.setPayAmount(payAmount);
        addDTO.setCompany( companyName);
        addDTO.setProjectName(projectName);
        addDTO.setProductName(productName);
        addDTO.setPayNumber( new BigDecimal(xlssalaryList.size() -1) );
        addDTO.setPayStatus("待复核");
        addDTO.setPaySubstatus( "待用人单位复核" );


        EntityWrapper<Product> wrapper4 = new EntityWrapper<>();
        wrapper4.eq("name",productName) ;
        Product product = productService.selectOne(wrapper4) ;
        addDTO.setAmountSource(product.getAmountSource());
        SalaryListService.add(addDTO);





        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新SalaryList")
    @SysLogs("更新SalaryList")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "SalaryList标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "SalaryList数据") SalaryListUpdateDTO updateDTO){
        SalaryListService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }



    public static void CalculateAmt(Product product, List<SalaryListDetails> salaryListDetails  ){

        product=new Product() ;
    }
}
