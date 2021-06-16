package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.common.util.AppotUtils;
import cn.licoy.wdog.core.dto.nbee.*;
import cn.licoy.wdog.core.entity.nbee.*;
import cn.licoy.wdog.core.service.nbee.*;
import cn.licoy.wdog.core.vo.nbee.AttendanceVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author mc
 * @version Sat Apr 17 19:14:07 2021
 */
@RestController
@RequestMapping(value="/Attendance")
public class AttendanceController  extends AppotBaseController{

   @Autowired
   private AttendanceService AttendanceService;


    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AttendanceDetailsService attendanceDetailsService;

    @Autowired
    private PreSalaryListService preSalaryListService;

    @Autowired
    private PreSalaryListDetailsService preSalaryListDetailsService;

    public AttendanceService getAttendanceService() {
      return AttendanceService;
   }

    @Autowired
    private ProjectService projectService;

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

    @RequestMapping(value = "/download")
    public void downLoad(HttpServletResponse response , String id) throws Exception {
        String filename= "考勤名单模板.xls";
        String filePath = "C:/mc/works/biz_日薪系统" ;
        File file = new File( filePath + filename);


        EntityWrapper<PreSalaryListDetails> wrapper = new EntityWrapper<>();
        wrapper.eq("pre_salary_list_id",id);
        List<PreSalaryListDetails> preSalaryListDetails = preSalaryListDetailsService.selectList(wrapper);





        //1.在内存中创建一个excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //2.创建工作簿
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //3.创建标题行
        HSSFRow titlerRow = sheet.createRow(0);

        titlerRow.createCell(0).setCellValue("公司名");
        titlerRow.createCell(1).setCellValue("产品名");
        titlerRow.createCell(2).setCellValue("项目名");
        titlerRow.createCell(3).setCellValue("考勤日期");
        titlerRow.createCell(4).setCellValue("批次号");
        titlerRow.createCell(5).setCellValue("姓名");
        titlerRow.createCell(6).setCellValue("身份证");
        titlerRow.createCell(7).setCellValue("手机号");
        titlerRow.createCell(8).setCellValue("工作小时");
        titlerRow.createCell(9).setCellValue("发放金额");
        titlerRow.createCell(10).setCellValue("入职日期");
        titlerRow.createCell(11).setCellValue("发放状态");
        titlerRow.createCell(12).setCellValue("发放说明");

        //4.遍历数据,创建数据行
        String kaoqing_datee = null;
        String company_name = null;
        for (PreSalaryListDetails preSalaryListDetails1 : preSalaryListDetails) {
            //获取最后一行的行号
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
            kaoqing_datee = DateFormatUtils.format(preSalaryListDetails1.getKaoqingDate(),"YYYYMMdd");
            dataRow.createCell(0).setCellValue( preSalaryListDetails1.getCompany() );
            dataRow.createCell(1).setCellValue( preSalaryListDetails1.getProductName() );
            dataRow.createCell(2).setCellValue( preSalaryListDetails1.getProjectName());
            dataRow.createCell(3).setCellValue( DateFormatUtils.format(preSalaryListDetails1.getKaoqingDate(),"YYYY/MM/dd") );
            dataRow.createCell(4).setCellValue( preSalaryListDetails1.getBatchNo() );
            dataRow.createCell(5).setCellValue( preSalaryListDetails1.getName() );
            dataRow.createCell(6).setCellValue( preSalaryListDetails1.getIdCard() );
            dataRow.createCell(7).setCellValue( preSalaryListDetails1.getMobile());
            dataRow.createCell(8).setCellValue( preSalaryListDetails1.getWorkdays().intValue() );
            dataRow.createCell(9).setCellValue( preSalaryListDetails1.getAmount().doubleValue() );
            dataRow.createCell(10).setCellValue( DateFormatUtils.format(preSalaryListDetails1.getInWorkdate(),"YYYY/MM/dd") );
            dataRow.createCell(11).setCellValue( preSalaryListDetails1.getIsPaysalaryStatus() );
            dataRow.createCell(12).setCellValue( preSalaryListDetails1.getIsPaySalary() );
            company_name = preSalaryListDetails1.getCompany();
        }
        //5.创建文件名
        String fileName = kaoqing_datee +"_"+company_name+"_预发薪名单.xls";
        //6.获取输出流对象
        ServletOutputStream outputStream = response.getOutputStream();
//        10.写出文件,关闭流

        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"utf8"));
        //输出流
        hssfWorkbook.write(outputStream);

    }



    @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取Attendance数据")
    @SysLogs("分页获取Attendance数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "Attendance获取过滤条件") FindAttendanceDTO findAttendanceDTO){

       Page<AttendanceVO> page = AttendanceService.getAllAttendanceBySplitPage(findAttendanceDTO);
        return ResponseResult.e(ResponseCode.OK,page);
    }

    @PostMapping(value = {"/all"})
    @ApiOperation(value = "分页获取Product数据")
    @SysLogs("分页获取Product数据")
    public ResponseResult getall(@RequestBody @Validated @ApiParam(value = "Product获取过滤条件") FindProjectDTO findProjectDTO){
        EntityWrapper<Project> wrapper = new EntityWrapper<>();
        List<Map> list = new ArrayList() ;
        for ( Project company:projectService.selectList(wrapper) ){
            Map<String,String> m = new HashMap<>();
            m.put("value",company.getName()) ;
            m.put("label",company.getName()) ;
            list.add(m);
        }
        return ResponseResult.e(ResponseCode.OK,list);
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取Attendance信息")
    @SysLogs("根据ID获取Attendance信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "AttendanceID") String id){
        return ResponseResult.e(ResponseCode.OK,AttendanceService.findAttendanceById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定Attendance")
    @SysLogs("锁定Attendance")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "Attendance标识ID") String id){
        AttendanceService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除Attendance")
    @SysLogs("删除Attendance")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "Attendance标识ID") String id){
        AttendanceService.removeAttendance(id);
        return ResponseResult.e(ResponseCode.OK);
    }



    //考勤上传
    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加Attendance")
    @SysLogs("添加Attendance")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "Attendance数据") AttendanceAddDTO addDTO) throws Exception {

        EntityWrapper<Project> wrapper = new EntityWrapper<>();
        wrapper.eq("name",addDTO.getProjectId()) ;
        Project project = projectService.selectOne(wrapper) ;

        EntityWrapper<Product> wrapper4 = new EntityWrapper<>();
        wrapper4.eq("name",project.getProductId()) ;
        Product product = productService.selectOne(wrapper4) ;

        EntityWrapper<Company> wrapper2 = new EntityWrapper<>();
        wrapper2.eq("name",project.getCompanyId()) ;
        Company company = companyService.selectOne(wrapper2);

        EntityWrapper<Attendance> wrapper3 = new EntityWrapper<Attendance>();
        wrapper3.eq("company",company.getName()) ;
        wrapper3.eq("kaoqing_date",addDTO.getKaoqingDate()) ;
        addDTO.setCompany(company.getName());
        List attendances =  AttendanceService.selectList(wrapper3) ;
        String batchno = "00000"+(attendances.size()+1);
        batchno = batchno.substring( String.valueOf((attendances.size()+1)).length(), batchno.length()) ;

        ArrayList<ArrayList<String>> results = readExcel(addDTO.getFileAddress());
        BigDecimal customer_numbers =  new BigDecimal( results.size() - 1 );

        addDTO.setAllCount( customer_numbers );
        String uuid = UUID.randomUUID().toString().replace("-","") ;
        addDTO.setId(uuid);
        addDTO.setBatchNo(batchno);
        addDTO.setCreateUser( getLoginUserName());
        addDTO.setProductName(product.getName());


        String uuid2 = UUID.randomUUID().toString().replace("-","") ;


        BigDecimal payAmount = new BigDecimal(0) ;
        BigDecimal onJobMunber = new BigDecimal(0) ;
        for (int i=0;i<results.size();i++){
            ArrayList<String> row = results.get(i);
            if(i==0){
                continue;
            }
            String inWorkdate = row.get(0);
            String customerName = row.get(1);
            String idCard = row.get(2);
            String mobile = row.get(3);
            String workhours = row.get(4);

            //考勤数据校验
            check_data(customerName,inWorkdate,idCard, workhours ,"0") ;
            Date inworkdate = DateUtils.parseDate(inWorkdate ,"YYYY/MM/dd");



            EntityWrapper<Customer> wrapperCustomer = new EntityWrapper<>();
            wrapperCustomer.eq("id_card",idCard);

            Customer customer = customerService.selectOne(wrapperCustomer ) ;
            if(customer==null ){
                customer = new Customer();
                customer.setName( customerName );
                customer.setIdCard( idCard );
                customer.setMobile( mobile );
            }
            customer.setCurrentCompany( company.getName() );
            customerService.insertOrUpdate( customer) ;



            AttendanceDetailsAddDTO attendanceDetails = new AttendanceDetailsAddDTO();
            attendanceDetails.setAttendanceId(uuid);

            attendanceDetails.setInWorkdate( inworkdate );
            attendanceDetails.setCreateDate(new Date());
            attendanceDetails.setCustomerName( customerName );
            attendanceDetails.setIdcard( idCard );
            attendanceDetails.setMobile( mobile );
            attendanceDetails.setWorkdays( new BigDecimal( workhours ));
            attendanceDetails.setCompany(company.getName());
            attendanceDetails.setBatchNo(batchno);
            attendanceDetails.setProjectName(project.getName());
            attendanceDetails.setKaoqingDate( addDTO.getKaoqingDate() );
            attendanceDetails.setCreateUser( getLoginUserName());
            attendanceDetails.setProductName( product.getName() );
            attendanceDetails.setBatchName(addDTO.getFilename());


            PreSalaryListDetailsAddDTO preSalaryListDetailsAddDTO  = new PreSalaryListDetailsAddDTO();
            preSalaryListDetailsAddDTO.setPreSalaryListId( uuid2);
            preSalaryListDetailsAddDTO.setBatchNo(batchno);
            preSalaryListDetailsAddDTO.setCompany(company.getName());
            preSalaryListDetailsAddDTO.setProjectName(project.getName());
            preSalaryListDetailsAddDTO.setProductName(product.getName());
            preSalaryListDetailsAddDTO.setCreateUser(getLoginUserName());

            //TODO 发放模式改为一种
//            if("每日发放金额".equals( product.getPayModel()) ){
//                preSalaryListDetailsAddDTO.setAmount( product.getPayModel2Dayamount().multiply( new BigDecimal( workhours ) )  );
//                payAmount = payAmount.add( preSalaryListDetailsAddDTO.getAmount() ) ;
//            }

            //入职后多少天发放
            if("入职".equals( product.getPayModel())  ){
                preSalaryListDetailsAddDTO.setAmount( product.getPayModel1Payamount()  );

            }

            Date currentDate = DateUtils.parseDate(new SimpleDateFormat("YYYY/MM/dd").format(new Date())  ,"YYYY/MM/dd");
//            currentDate 比 inworkdate 多的天数
            if(  AppotUtils.differentDays( inworkdate, currentDate) > product.getPayModel1Dayslater().intValue() ){
                payAmount = payAmount.add( preSalaryListDetailsAddDTO.getAmount() ) ;
                onJobMunber = onJobMunber.add( new BigDecimal(1));

                preSalaryListDetailsAddDTO.setIsPaySalary("满足发放条件");
                preSalaryListDetailsAddDTO.setIsPaysalaryStatus("满足发放条件");

                attendanceDetails.setIsPaysalaryStatus("满足发放条件" );
                attendanceDetails.setIsPaySalary("满足发放条件");
            }else{
                preSalaryListDetailsAddDTO.setIsPaySalary("不满足发放条件,入职距今"+AppotUtils.differentDays( inworkdate, currentDate)+"天,要求入职后"+ product.getPayModel1Dayslater().intValue() +"天发放!");
                preSalaryListDetailsAddDTO.setIsPaysalaryStatus("不满足发放条件");

                attendanceDetails.setIsPaysalaryStatus("不满足发放条件" );
                attendanceDetails.setIsPaySalary("不满足发放条件,入职距今"+AppotUtils.differentDays( inworkdate, currentDate)+"天,要求入职后"+ product.getPayModel1Dayslater().intValue() +"天发放!");
            }

            preSalaryListDetailsAddDTO.setName( customerName );
            preSalaryListDetailsAddDTO.setMobile( mobile );
            preSalaryListDetailsAddDTO.setIdCard( idCard  ) ;
            preSalaryListDetailsAddDTO.setKaoqingDate( addDTO.getKaoqingDate() );
            preSalaryListDetailsAddDTO.setInWorkdate( inworkdate );
            preSalaryListDetailsAddDTO.setWorkdays( new BigDecimal( workhours ) );
            preSalaryListDetailsAddDTO.setBatchNo(batchno);
            preSalaryListDetailsAddDTO.setBatchName(addDTO.getFilename() );


            attendanceDetailsService.add(attendanceDetails);
            preSalaryListDetailsService.add(preSalaryListDetailsAddDTO);

        }

        AttendanceService.add(addDTO);

        PreSalaryListAddDTO preSalaryListAddDTO = new PreSalaryListAddDTO();
        preSalaryListAddDTO.setId(uuid2);
        preSalaryListAddDTO.setBatchNo(batchno);
        preSalaryListAddDTO.setCompany( company.getName());
        preSalaryListAddDTO.setProjectName( project.getName());
        preSalaryListAddDTO.setCreateDate(new Date());
        preSalaryListAddDTO.setPayNumber(onJobMunber );
        //满足发放人数 TODO
        preSalaryListAddDTO.setOnJobMunber(   new BigDecimal( results.size() - 1 )  );
        preSalaryListAddDTO.setPreSalaryListCreatetime( new Date());
        preSalaryListAddDTO.setCreateUser( getLoginUserName() );
        preSalaryListAddDTO.setProductName( product.getName() );
        preSalaryListAddDTO.setPayAmount( payAmount );
        preSalaryListAddDTO.setKaoqingDate( addDTO.getKaoqingDate() );
        preSalaryListAddDTO.setBatchName(addDTO.getFilename());
        preSalaryListService.add(preSalaryListAddDTO);
        return ResponseResult.e(ResponseCode.OK);
    }





    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新Attendance")
    @SysLogs("更新Attendance")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "Attendance标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "Attendance数据") AttendanceUpdateDTO updateDTO){
        AttendanceService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
