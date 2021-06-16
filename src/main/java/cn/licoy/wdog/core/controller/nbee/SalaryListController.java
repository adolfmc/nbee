package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.*;
import cn.licoy.wdog.core.entity.nbee.*;
import cn.licoy.wdog.core.service.nbee.*;
import cn.licoy.wdog.core.vo.nbee.SalaryListDetailsVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
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

    @RequestMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取SalaryList信息")
    @SysLogs("根据ID获取SalaryList信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "SalaryListID") String id){
        FindSalaryListDetailsDTO findSalaryListDetailsDTO =new FindSalaryListDetailsDTO();
        findSalaryListDetailsDTO.setPageSize(100000);
        findSalaryListDetailsDTO.setSalaryListId(id);
        Page<SalaryListDetailsVO> pages =  salaryListDetailsService.getAllSalaryListDetailsBySplitPage(findSalaryListDetailsDTO);


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
    @ApiOperation(value = "remove")
    @SysLogs("remove")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "SalaryList标识ID") String id){
        SalaryListService.removeSalaryList(id);
        return ResponseResult.e(ResponseCode.OK);
    }


    @PostMapping(value = {"/fuhe"})
    @ApiOperation(value = "fuhe")
    @SysLogs("fuhe")
    public ResponseResult fuhe(@RequestBody @Validated @ApiParam(value = "SalaryList数据") SalaryListUpdateDTO updateDTO) throws Exception {
        SalaryList salaryList = SalaryListService.selectById(updateDTO.getId());
        EntityWrapper<Product> wrapperProduct = new EntityWrapper<>();
        wrapperProduct.eq("name",salaryList.getProductName());
        Product product = productService.selectOne(wrapperProduct);
        EntityWrapper<Project> wrapper5 = new EntityWrapper<>();
        wrapper5.eq("name",salaryList.getProjectName()) ;
        Project project = projectService.selectOne(wrapper5) ;





        if(updateDTO.getPayStatus().equals("复核通过")){
            //TODO 平台和 用人单位权限分离
            //contains "," 两个复核方
            if(project.getCheckBill().contains(",")){
                //第一次复核情况
                if(salaryList.getPaySubstatus()==null || "".equals(salaryList.getPaySubstatus())){
                    salaryList.setPayStatus("待复核");
                    salaryList.setPaySubstatus("待平台复核发放");
                    salaryList.setFuheDate(new Date());
                    salaryList.setFuheUser( getLoginUserName());
                //第二次复核情况
                }else{
                    salaryList.setPayStatus("待发放");
                    salaryList.setPaySubstatus("待"+project.getSendBill()+"发放");
                    salaryList.setFuheDate2(new Date());
                    salaryList.setFuheUser2( getLoginUserName());
                }
            }else{
                salaryList.setPayStatus("待发放");
                salaryList.setPaySubstatus("待"+project.getSendBill()+"发放");
                salaryList.setFuheDate(new Date());
                salaryList.setFuheUser( getLoginUserName());
            }
        }else{
            salaryList.setPayStatus("复核拒绝");
            salaryList.setPaySubstatus("待"+project.getMakerBill()+"重新制单");
            salaryList.setFuheDate(new Date());
            salaryList.setFuheUser( getLoginUserName());
        }



        EntityWrapper<SalaryListDetails> wrappersalaryListDetails = new EntityWrapper<>();
        wrappersalaryListDetails.eq("salary_list_id",salaryList.getId() ) ;
        List<SalaryListDetails>  salaryListDetails =salaryListDetailsService.selectList(wrappersalaryListDetails);
        List<BigDecimal> workdayss = new ArrayList<BigDecimal>();
        List<Date> kaoqingDate = new ArrayList<Date>();

        BigDecimal maxAmount = new BigDecimal(0) ;
        for( int i=0;i<salaryListDetails.size();i++){
            SalaryListDetails salaryListDetails1 = salaryListDetails.get(i);
            workdayss.add(salaryListDetails1.getWorkdays());
            kaoqingDate.add(salaryListDetails1.getKaoqingDate()) ;

            if(i==1){
                maxAmount = salaryListDetails1.getAmount();
            }

            if( maxAmount.compareTo(salaryListDetails1.getAmount())==-1 ){
                maxAmount = salaryListDetails1.getAmount();
            }
        }


        EntityWrapper<AccountCore> wrapperAccountCore = new EntityWrapper<>();
        wrapperAccountCore.eq("company_id",salaryList.getCompany() ) ;
        wrapperAccountCore.eq("account_type","用人单位") ;
        AccountCore accountCompany = accountCoreService.selectOne(wrapperAccountCore) ;
        salaryList.setFeeAmount( this.getALLFee( workdayss,kaoqingDate,product,accountCompany,salaryList.getPayAmount() ) );
        salaryList.setFeeAccbanlance( accountCompany.getT3feeBanlance() );
        salaryList.setPaybanlanceAmount( accountCompany.getT3creditAmountBanlance());
        salaryList.setAvgAmount( salaryList.getPayAmount().divide( salaryList.getPayNumber()) );
        salaryList.setMaxAmount(maxAmount);

        SalaryListService.insertOrUpdate(salaryList);

        return ResponseResult.e(ResponseCode.OK);
    }

    //发放拒绝
    @PostMapping(value = {"/fafangbohui"})
    @ApiOperation(value = "fafangbohui")
    @SysLogs("fafangbohui")
    public ResponseResult fafangbohui(@RequestBody @Validated @ApiParam(value = "fafangbohui") SalaryListAddDTO addDTO ){
        SalaryList salaryList = SalaryListService.selectById(addDTO.getId());

        EntityWrapper<Project> wrapper5 = new EntityWrapper<>();
        wrapper5.eq("name",salaryList.getProjectName()) ;
        Project project = projectService.selectOne(wrapper5) ;


        salaryList.setPayStatus("发放拒绝");
        salaryList.setPaySubstatus("待"+project.getMakerBill()+"重新制单");
        salaryList.setFafangUser( getLoginUserName());
        salaryList.setFafangDate(new Date());
        salaryList.setPayDesc( addDTO.getPayDesc());
        SalaryListService.insertOrUpdate( salaryList) ;
        return ResponseResult.e(ResponseCode.OK);
    }



    //发放通过
    @PostMapping(value = {"/tongg"})
    @ApiOperation(value = "删除SalaryList")
    @SysLogs("删除SalaryList")
    public ResponseResult tongg(@RequestBody @Validated @ApiParam(value = "SalaryList数据") SalaryListAddDTO addDTO ) throws Exception {
        SalaryList salaryList = SalaryListService.selectById(addDTO.getId());


        EntityWrapper<Product> wrapperProduct = new EntityWrapper<>();
        wrapperProduct.eq("name",salaryList.getProductName());
        Product product = productService.selectOne(wrapperProduct);

        //获取发放明细*********************************************************************************
        EntityWrapper<SalaryListDetails> wrapperSalaryListDetails = new EntityWrapper<>();
        wrapperSalaryListDetails.eq("salary_list_id",salaryList.getId());
        List<SalaryListDetails> salaryListDetailss = salaryListDetailsService.selectList(wrapperSalaryListDetails ) ;




        //1  更新公司信息 **************************************************************************
        EntityWrapper<AccountCore> wrapperAccountCore = new EntityWrapper<>();
        wrapperAccountCore.eq("company_id",salaryList.getCompany()) ;
        wrapperAccountCore.eq("account_type","用人单位") ;
        AccountCore accountCompany = accountCoreService.selectOne(wrapperAccountCore) ;
        //更新当前领薪人数
        accountCompany.setT3currentPaynumber( salaryList.getPayNumber());
        //公司手续费账户余额 减少 TODO

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





        //2  更新平台信息 **************************************************************************
        AccountCore accountPlantform = accountCoreService.selectById("1") ;


        List<BigDecimal> workdayss = new ArrayList<BigDecimal>();
        List<Date> kaoqingDate = new ArrayList<Date>();

        BigDecimal maxAmount = new BigDecimal(0) ;
        for( int i=0;i<salaryListDetailss.size();i++){
            SalaryListDetails salaryListDetails1 = salaryListDetailss.get(i);
            workdayss.add(salaryListDetails1.getWorkdays());
            kaoqingDate.add(salaryListDetails1.getKaoqingDate()) ;

            if(i==1){
                maxAmount = salaryListDetails1.getAmount();
            }

            if( maxAmount.compareTo(salaryListDetails1.getAmount())==-1 ){
                maxAmount = salaryListDetails1.getAmount();
            }
        }
        BigDecimal allfee= getALLFee(workdayss ,kaoqingDate,product,accountCompany, salaryList.getPayAmount());
        accountPlantform.setPfincomeAccount( accountPlantform.getPfincomeAccount().add(allfee) );
        BigDecimal compfeebanlance =  accountCompany.getT3feeBanlance().subtract(allfee);
        //小于0
        if( allfee==null     ){
            return ResponseResult.e(ResponseCode.FAIL,null);
        }else{
            accountCompany.setT3feeBanlance(compfeebanlance);
        }


        accountPlantform.setT3currentPaynumber( salaryList.getPayNumber());
        //3  更新个人信息 **************************************************************************
        for(SalaryListDetails salaryListDetails : salaryListDetailss){
            EntityWrapper<Customer> wrapperCustomer = new EntityWrapper<>();
            wrapperCustomer.eq("id_card",salaryListDetails.getIdCard());

            Customer customer = customerService.selectOne(wrapperCustomer ) ;
            if(customer==null ){
                customer = new Customer();
                customer.setName( salaryListDetails.getName() );
                customer.setIdCard( salaryListDetails.getIdCard());
                customer.setMobile( salaryListDetails.getMobile() );
            }else{
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
                //首次创建 核心账户
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
            //添加或更新个人账户
            accountCoreService.insertOrUpdate( accountCore) ;


            //添加账目明细和总账金额对应关系
            AccountSalarydetails accountSalarydetails = new AccountSalarydetails();
            accountSalarydetails.setCreateDate(new Date());
            accountSalarydetails.setAccountId(accountCore.getId());
            accountSalarydetails.setSalaryDetailsId(salaryListDetails.getId());
            accountSalarydetails.setAmount(salaryListDetails.getAmount());
            accountSalarydetails.setName(salaryListDetails.getName());
            accountSalarydetails.setIdCard(salaryListDetails.getIdCard());
            accountSalarydetails.setCurrentCompany(salaryListDetails.getCompany());
            accountSalarydetails.setStatus("待提现");
            accountSalarydetailsService.insert(accountSalarydetails);

        }


        //更新平台账户
        accountCoreService.insertOrUpdate(accountPlantform);
        //更新公司账户
        accountCoreService.insertOrUpdate(accountCompany);

        //更新发放信息
        salaryList.setPayDesc( addDTO.getPayDesc());
        salaryList.setPayAccount( addDTO.getPayAccount() );


        EntityWrapper<Project> wrapper5 = new EntityWrapper<>();
        wrapper5.eq("name",salaryList.getProjectName()) ;
        Project project = projectService.selectOne(wrapper5) ;

        salaryList.setPayStatus("已发放");
        salaryList.setPaySubstatus(project.getSendBill()+"已发放");
        salaryList.setFafangDate( new Date());
        salaryList.setFafangUser( getLoginUserName());
        SalaryListService.insertOrUpdate(salaryList);


        return ResponseResult.e(ResponseCode.OK);
    }

    //制单
    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加SalaryList")
    @SysLogs("添加SalaryList")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "SalaryList数据") SalaryListAddDTO addDTO ) throws Exception {

        List<List<String>> xlssalaryList = readExcel( addDTO.getFileAddress()) ;
        String uuid = UUID.randomUUID().toString().replace("-","");
        String companyName = null;
        String productName = null;
        String projectName = null;
        String batchNo = null;

        BigDecimal maxAmount = new BigDecimal(0);
        List<BigDecimal> workdayss = new ArrayList<BigDecimal>();
        List<Date> kaoqingDate = new ArrayList<Date>();


        boolean maxamounttag = true ;

        BigDecimal payAmount = new BigDecimal(0) ;
        BigDecimal onJobMunber = new BigDecimal(0) ;
        for(int i=0;i<xlssalaryList.size();i++){
            if(i==0){
                continue;
            }
            List<String> rows = xlssalaryList.get(i) ;
            if(rows.size()!=13){
                throw new  Exception("请检查上传excel文件是否正常,确认是下载的预发薪名单excel文件吗!");
            }
            String company = rows.get(0);
            String product_name = rows.get(1);
            String project_name = rows.get(2);
            String kaoqing_datee = rows.get(3);
            String batch_no = rows.get(4);
            String name = rows.get(5);
            String idcard = rows.get(6);
            String mobile = rows.get(7);
            String workhours = rows.get(8);
            String amount = rows.get(9);
            String inWorkdate = rows.get(10);
            String isPaySalaryStatus = rows.get(11);
            String isPaySalary = rows.get(12);

            companyName = company;
            productName = product_name ;
            projectName  = project_name;
            batchNo = batch_no ;

            //考勤数据校验
            check_data(name,inWorkdate,idcard, workhours ,amount) ;


            if("满足发放条件".equals(isPaySalaryStatus)){
                SalaryListDetailsAddDTO salaryListDetailsAddDTO = new SalaryListDetailsAddDTO();
                salaryListDetailsAddDTO.setCompany( company);
                salaryListDetailsAddDTO.setProductName( product_name);
                salaryListDetailsAddDTO.setProjectName( project_name);
                salaryListDetailsAddDTO.setKaoqingDate(DateUtils.parseDate( kaoqing_datee,"YYYY/MM/dd") );
                salaryListDetailsAddDTO.setBatchNo( batch_no);
                salaryListDetailsAddDTO.setName( name);
                salaryListDetailsAddDTO.setIdCard(idcard);
                salaryListDetailsAddDTO.setMobile( mobile);
                salaryListDetailsAddDTO.setWorkdays( new BigDecimal(workhours));
                salaryListDetailsAddDTO.setAmount( new BigDecimal(amount));
                salaryListDetailsAddDTO.setSalaryListId( uuid);
                salaryListDetailsAddDTO.setPickStatus("未提现");
                salaryListDetailsService.add(salaryListDetailsAddDTO);

                payAmount = payAmount.add( new BigDecimal( amount)) ;
                onJobMunber = onJobMunber.add( new BigDecimal(1));
                workdayss.add( salaryListDetailsAddDTO.getWorkdays());
                kaoqingDate.add( salaryListDetailsAddDTO.getKaoqingDate() );




                //判断最大金额
                if(maxamounttag == Boolean.TRUE){
                    maxAmount = new BigDecimal(amount);
                    maxamounttag = Boolean.FALSE;
                }
                if( maxAmount.compareTo( new BigDecimal(amount))==-1 ){
                    maxAmount = new BigDecimal(amount);
                }

            }

        }


        EntityWrapper<Product> wrapper4 = new EntityWrapper<>();
        wrapper4.eq("name",productName) ;
        Product product = productService.selectOne(wrapper4) ;
        EntityWrapper<Project> wrapper5 = new EntityWrapper<>();
        wrapper5.eq("name",projectName) ;
        Project project = projectService.selectOne(wrapper5) ;



        addDTO.setId(uuid);
        addDTO.setBatchName(addDTO.getFileName());
        addDTO.setBatchNo(batchNo);
        addDTO.setPayAmount(payAmount);
        addDTO.setCompany( companyName);
        addDTO.setProjectName(projectName);
        addDTO.setProductName(productName);
        addDTO.setPayNumber( onJobMunber );
        addDTO.setZhidanUser(getLoginUserName() );
        addDTO.setZhidanDate(new Date());



        //复核状态标记
        if(project.getCheckBill().contains(",")){
            addDTO.setPayStatus("待复核");
            addDTO.setPaySubstatus( "待用人单位复核" );//默认首次 用人单位复核
        }else{
            addDTO.setPayStatus("待复核");
            addDTO.setPaySubstatus( "待"+project.getCheckBill()+"复核" );
        }


        addDTO.setAmountSource(product.getAmountSource());


        EntityWrapper<AccountCore> wrapperAccountCore = new EntityWrapper<>();
        wrapperAccountCore.eq("company_id",addDTO.getCompany() ) ;
        wrapperAccountCore.eq("account_type","用人单位") ;
        AccountCore accountCompany = accountCoreService.selectOne(wrapperAccountCore) ;
        addDTO.setFeeAmount( this.getALLFee( workdayss,kaoqingDate,product,accountCompany,addDTO.getPayAmount() ) );
        addDTO.setFeeAccbanlance( accountCompany.getT3feeBanlance() );
        addDTO.setPaybanlanceAmount( accountCompany.getT3creditAmountBanlance());
        addDTO.setAvgAmount( addDTO.getPayAmount().divide( addDTO.getPayNumber()) );
        addDTO.setMaxAmount(maxAmount);
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

}
