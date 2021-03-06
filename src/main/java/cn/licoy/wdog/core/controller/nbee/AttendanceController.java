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
     * ??????????????????
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
        String filename= "??????????????????.xls";
        String filePath = "C:/mc/works/biz_????????????" ;
        File file = new File( filePath + filename);


        EntityWrapper<PreSalaryListDetails> wrapper = new EntityWrapper<>();
        wrapper.eq("pre_salary_list_id",id);
        List<PreSalaryListDetails> preSalaryListDetails = preSalaryListDetailsService.selectList(wrapper);





        //1.????????????????????????excel??????
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //2.???????????????
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //3.???????????????
        HSSFRow titlerRow = sheet.createRow(0);

        titlerRow.createCell(0).setCellValue("?????????");
        titlerRow.createCell(1).setCellValue("?????????");
        titlerRow.createCell(2).setCellValue("?????????");
        titlerRow.createCell(3).setCellValue("????????????");
        titlerRow.createCell(4).setCellValue("?????????");
        titlerRow.createCell(5).setCellValue("??????");
        titlerRow.createCell(6).setCellValue("?????????");
        titlerRow.createCell(7).setCellValue("?????????");
        titlerRow.createCell(8).setCellValue("????????????");
        titlerRow.createCell(9).setCellValue("????????????");
        titlerRow.createCell(10).setCellValue("????????????");
        titlerRow.createCell(11).setCellValue("????????????");
        titlerRow.createCell(12).setCellValue("????????????");

        //4.????????????,???????????????
        String kaoqing_datee = null;
        String company_name = null;
        for (PreSalaryListDetails preSalaryListDetails1 : preSalaryListDetails) {
            //???????????????????????????
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
        //5.???????????????
        String fileName = kaoqing_datee +"_"+company_name+"_???????????????.xls";
        //6.?????????????????????
        ServletOutputStream outputStream = response.getOutputStream();
//        10.????????????,?????????

        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"utf8"));
        //?????????
        hssfWorkbook.write(outputStream);

    }



    @PostMapping(value = {"/list"})
    @ApiOperation(value = "????????????Attendance??????")
    @SysLogs("????????????Attendance??????")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "Attendance??????????????????") FindAttendanceDTO findAttendanceDTO){

       Page<AttendanceVO> page = AttendanceService.getAllAttendanceBySplitPage(findAttendanceDTO);
        return ResponseResult.e(ResponseCode.OK,page);
    }

    @PostMapping(value = {"/all"})
    @ApiOperation(value = "????????????Product??????")
    @SysLogs("????????????Product??????")
    public ResponseResult getall(@RequestBody @Validated @ApiParam(value = "Product??????????????????") FindProjectDTO findProjectDTO){
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
    @ApiOperation(value = "??????ID??????Attendance??????")
    @SysLogs("??????ID??????Attendance??????")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "AttendanceID") String id){
        return ResponseResult.e(ResponseCode.OK,AttendanceService.findAttendanceById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "??????Attendance")
    @SysLogs("??????Attendance")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "Attendance??????ID") String id){
        AttendanceService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "??????Attendance")
    @SysLogs("??????Attendance")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "Attendance??????ID") String id){
        AttendanceService.removeAttendance(id);
        return ResponseResult.e(ResponseCode.OK);
    }



    //????????????
    @PostMapping(value = {"/add"})
    @ApiOperation(value = "??????Attendance")
    @SysLogs("??????Attendance")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "Attendance??????") AttendanceAddDTO addDTO) throws Exception {

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

            //??????????????????
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

            //TODO ????????????????????????
//            if("??????????????????".equals( product.getPayModel()) ){
//                preSalaryListDetailsAddDTO.setAmount( product.getPayModel2Dayamount().multiply( new BigDecimal( workhours ) )  );
//                payAmount = payAmount.add( preSalaryListDetailsAddDTO.getAmount() ) ;
//            }

            //????????????????????????
            if("??????".equals( product.getPayModel())  ){
                preSalaryListDetailsAddDTO.setAmount( product.getPayModel1Payamount()  );

            }

            Date currentDate = DateUtils.parseDate(new SimpleDateFormat("YYYY/MM/dd").format(new Date())  ,"YYYY/MM/dd");
//            currentDate ??? inworkdate ????????????
            if(  AppotUtils.differentDays( inworkdate, currentDate) > product.getPayModel1Dayslater().intValue() ){
                payAmount = payAmount.add( preSalaryListDetailsAddDTO.getAmount() ) ;
                onJobMunber = onJobMunber.add( new BigDecimal(1));

                preSalaryListDetailsAddDTO.setIsPaySalary("??????????????????");
                preSalaryListDetailsAddDTO.setIsPaysalaryStatus("??????????????????");

                attendanceDetails.setIsPaysalaryStatus("??????????????????" );
                attendanceDetails.setIsPaySalary("??????????????????");
            }else{
                preSalaryListDetailsAddDTO.setIsPaySalary("?????????????????????,????????????"+AppotUtils.differentDays( inworkdate, currentDate)+"???,???????????????"+ product.getPayModel1Dayslater().intValue() +"?????????!");
                preSalaryListDetailsAddDTO.setIsPaysalaryStatus("?????????????????????");

                attendanceDetails.setIsPaysalaryStatus("?????????????????????" );
                attendanceDetails.setIsPaySalary("?????????????????????,????????????"+AppotUtils.differentDays( inworkdate, currentDate)+"???,???????????????"+ product.getPayModel1Dayslater().intValue() +"?????????!");
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
        //?????????????????? TODO
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
    @ApiOperation(value = "??????Attendance")
    @SysLogs("??????Attendance")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "Attendance??????ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "Attendance??????") AttendanceUpdateDTO updateDTO){
        AttendanceService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
