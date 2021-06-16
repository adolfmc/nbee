package cn.licoy.wdog.common.controller;

import cn.licoy.wdog.common.util.AppotUtils;
import cn.licoy.wdog.common.util.CheckIdCard;
import cn.licoy.wdog.common.util.JwtUtil;
import cn.licoy.wdog.common.util.POIUtil;
import cn.licoy.wdog.core.entity.nbee.*;
import cn.licoy.wdog.core.service.nbee.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.crazycake.shiro.RedisManager;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

public class AppotBaseController extends BaseInfo {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @Autowired
    public RedisManager redisManager;

    @Autowired
    public AccountingentriesService accountingentriesService ;

    @Autowired
    public CustomerService customerService ;
    @Autowired
    public TppAlipayService tppAlipayService;
    @Autowired
    public TppTransferService tppTransferService;
    @Autowired
    public ProjectService projectService;
    @Autowired
    public TppTixianService tppTixianService;
    @Autowired
    public AccountSalarydetailsService accountSalarydetailsService;

    @Autowired
    public CompanyService companyService;

    @Autowired
    public HttpServletRequest myHttpRequest;

    public String getLoginUserName(){
        String token  = myHttpRequest.getHeader("Authorization") ;
        return JwtUtil.getUsername(token);
    }

    public String getMobile(HttpServletRequest request ){
        if(AppotUtils.getHeader(request,"user.mobile")==null){
            return "";
        }
        return AppotUtils.getHeader(request,"user.mobile");
    }

    public void setRedisV(String key,String verCode,int exipreTime){
        redisManager.set(key.getBytes(),verCode.getBytes(),exipreTime);
    }

    public String getRedisV(String key)  {
        //获取缓存
        byte[] redistemp =  redisManager.get(key.getBytes());
        String dbCode = null;
        try{
            if(redistemp!=null && redistemp.length!=0){
                dbCode = new String(redistemp , "utf-8");
            }
        }catch (Exception ex){
        }

        return dbCode;
    }

    // 获取openID的URL(微信)
    final static String GETOPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public Map<String, String> getTokenOppenId(String code, String mobile,String APPID,String SECRET) {
        Map<String,String> token_oppenId = new HashMap();
        if(getRedisV(mobile+AppotUtils.WX_TOKEN_KEY) ==null || "".equals(getRedisV(mobile+AppotUtils.WX_TOKEN_KEY)) ){
            System.out.println("WX_TOKEN_KEY 非缓存模式.");
            String getOpenIdparam= "appid="+APPID+"&secret="+SECRET+"&code="+code+"&grant_type=authorization_code";
            String getOpenIdUrl = GETOPENID_URL+"?"+getOpenIdparam;
            System.out.println("***getOpenId:"+getOpenIdUrl);
            RestTemplate rest = new RestTemplate();
            rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
            String jsonStr = null ;
            try {

                String resString = rest.getForObject(new URI(getOpenIdUrl), String.class);
                JSONObject opidJsonObject = JSONObject.parseObject(resString);
                System.out.println("***opidJsonObject:" + opidJsonObject);
                String openid = opidJsonObject.get("openid").toString();//获取到了openid
                String access_token = opidJsonObject.get("access_token").toString();//获取到了access_token

                setRedisV( mobile + AppotUtils.WX_TOKEN_KEY, access_token, 3600 * 2);
                setRedisV( mobile + AppotUtils.WX_OPPENID_KEY, openid, 3600 * 1000000);

                token_oppenId.put(AppotUtils.WX_OPPENID_KEY,openid);
                token_oppenId.put(AppotUtils.WX_TOKEN_KEY,access_token);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            System.out.println("WX_TOKEN_KEY 缓存模式.");
            token_oppenId.put(AppotUtils.WX_OPPENID_KEY,getRedisV(mobile+AppotUtils.WX_OPPENID_KEY));
            token_oppenId.put(AppotUtils.WX_TOKEN_KEY,getRedisV(mobile+AppotUtils.WX_TOKEN_KEY));
        }

        return token_oppenId;
    }




    // 获取openID的URL(微信)
    final static String GETOPENID_URL_XCX = "https://api.weixin.qq.com/sns/jscode2session";
    public Map<String, String> getTokenOppenId_XCX(String code, String mobile,String APPID,String SECRET) {
        Map<String,String> token_oppenId = new HashMap();
        if(true || getRedisV(mobile+AppotUtils.WX_TOKEN_KEY) ==null || "".equals(getRedisV(mobile+AppotUtils.WX_TOKEN_KEY)) ){
            System.out.println("WX_TOKEN_KEY 非缓存模式.");
            String getOpenIdparam= "appid="+APPID+"&secret="+SECRET+"&js_code="+code+"&grant_type=authorization_code";
            String getOpenIdUrl = GETOPENID_URL_XCX+"?"+getOpenIdparam;
            System.out.println("***getOpenId:"+getOpenIdUrl);
            RestTemplate rest = new RestTemplate();
            rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
            String jsonStr = null ;
            try {

                String resString = rest.getForObject(new URI(getOpenIdUrl), String.class);
                JSONObject opidJsonObject = JSONObject.parseObject(resString);
                System.out.println("***opidJsonObject:" + opidJsonObject);
                String openid = opidJsonObject.get("openid").toString();//获取到了openid
                String access_token = opidJsonObject.get("session_key").toString();//获取到了access_token


                System.out.println("===============================================2 openid & access_token =============================================================");
                System.out.println("================== openid = "+openid );
                System.out.println("================== access_token = "+access_token );
                System.out.println("===============================================2 openid & access_token =============================================================");


                setRedisV( mobile + AppotUtils.WX_TOKEN_KEY, access_token, 3600 * 2);
                setRedisV( mobile + AppotUtils.WX_OPPENID_KEY, openid, 3600 * 1000000);

                token_oppenId.put(AppotUtils.WX_OPPENID_KEY,openid);
                token_oppenId.put(AppotUtils.WX_TOKEN_KEY,access_token);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            System.out.println("WX_TOKEN_KEY 缓存模式.");
            token_oppenId.put(AppotUtils.WX_OPPENID_KEY,getRedisV(mobile+AppotUtils.WX_OPPENID_KEY));
            token_oppenId.put(AppotUtils.WX_TOKEN_KEY,getRedisV(mobile+AppotUtils.WX_TOKEN_KEY));
        }

        return token_oppenId;
    }


    /**
     * 对集合进行深拷贝
     * 注意需要岁泛型类进行序列化（实现serializable）
     *
     * @param src
     * @param <T>
     * @return
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteOut);
        ) {
            outputStream.writeObject(src);
            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                 ObjectInputStream inputStream = new ObjectInputStream(byteIn);
            ) {
                return (List<T>) inputStream.readObject();
            }
        } catch (Exception e) {
        }
        return Collections.emptyList();
    }

    public  String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }

        return sb.toString().toUpperCase();
    }


    public Pagination getPagination(String sql ,int currentPage,int pageSize, RowMapper rowMapper){
        Pagination pagination = new Pagination(sql,currentPage,10,jdbcTemplate ,rowMapper);
        return pagination;
    }

    public String appendSqlIN(String[] values){
        StringBuffer sql = new StringBuffer();
        for (String v:values){
            sql.append("'"+v+"',");
        }
        return "("+sql.toString().substring(0,sql.toString().length()-1)+")";
    }


    public ArrayList readExcel(String filePath){

        ArrayList<ArrayList> result =new ArrayList<ArrayList> ();
        InputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            Workbook workbook = null;
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.endsWith(".xls") || filePath.endsWith(".et")) {
                workbook = new HSSFWorkbook(fis);
            }
            fis.close();
            /* 读EXCEL文字内容 */
            // 获取第一个sheet表，也可使用sheet表名获取
            Sheet sheet = workbook.getSheetAt(0);
            // 获取行
            Iterator<Row> rows = sheet.rowIterator();
            Row row;
            Cell cell;



            while (rows.hasNext()) {
                row = rows.next();
                // 获取单元格
                Iterator<Cell> cells = row.cellIterator();

                ArrayList<String> rowarray =new ArrayList<String> ();
                while (cells.hasNext()) {
                    cell = cells.next();
                    String cellValue = POIUtil.getCellValue(cell);
                    System.out.print(cellValue + " ");
                    rowarray.add(cellValue);
                }
                result.add(rowarray);
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    public void accountingEntries (String companyOrPlatfrom, String biztype,BigDecimal amount, String in , String out){
        Accountingentries accountingentries = new Accountingentries();
        accountingentries.setCreateDate(new Date());
        accountingentries.setDataType(companyOrPlatfrom);
        accountingentries.setBizType(biztype);
        accountingentries.setAmount(amount);
        accountingentries.setInId(in);
        accountingentries.setOutId(out);
        accountingentries.setDesc( biztype+amount.toString()    );
        accountingentriesService.insert(accountingentries);
    }


    public BigDecimal getALLFee(List<BigDecimal> workdays , List<Date> kaoqingdate, Product product  , AccountCore accountCompany , BigDecimal payAmount) throws Exception {

        BigDecimal allfee = new BigDecimal(0);
        //2.5
        if( "按每笔发放百元 - 每笔发放每百元，按照比例".equals(  product.getFeeModel() )){
            allfee =  payAmount.multiply( product.getFeeModel5V().divide(new BigDecimal(100)) ) ;


            //2.1
        }else if( "有X天考勤信息，每天发".equals(  product.getFeeModel() )){
            /** 1) 判断该考勤当天是否已收费 **/

            /** 2) */
            //2.2
        }else if( "按发放次数 -- 每次发放收(XX)".equals(  product.getFeeModel() )){
            allfee =  product.getFeeModel2V();


            //2.3
        }else if( "按每笔发放小时数 - 每笔发放覆盖的每个工作小时收费".equals(  product.getFeeModel() )){
            BigDecimal allhours = new BigDecimal(0);


            for(BigDecimal workday : workdays){
                allhours = allhours.add( workday ) ;
            }


            allfee =  product.getFeeModel3V().multiply(allhours);


            //2.4
        }else if( "按每笔发放覆盖天数 - 每笔发放覆盖的天数，每天收取".equals(  product.getFeeModel() )){

            Map<String,Object> day = new HashMap();

            for(Date kaoqingDate : kaoqingdate){
                day.put( kaoqingDate.toString(),0 );
            }

            allfee =  product.getFeeModel4V().multiply( new BigDecimal(day.keySet().size() ));

        }

        //小于0
        BigDecimal compfeebanlance =  accountCompany.getT3feeBanlance().subtract(allfee);
        if( compfeebanlance.compareTo( new BigDecimal( 0 ))==-1     ){
            throw new Exception("客户手续费余额不足,请充值手续费后操作,当前手续费 余额为: "+accountCompany.getT3feeBanlance()+"元");
        }

        return allfee;
    }





    public void check_data(String customer,String inWorkdate, String idCard, String workhours, String amount) throws Exception {

        try{
            DateUtils.parseDate(inWorkdate ,"YYYY/MM/dd") ;
        }catch (Exception ex){
            throw new Exception("客户 ("+customer+") "+"入职时间格式异常(正确格式 YYYY/MM/dd): "+inWorkdate);
        }

        try{
            if(CheckIdCard.validate(idCard)==Boolean.FALSE){
                throw new Exception("客户 ("+customer+") "+"身份证格式异常: "+idCard);
            }
        }catch (Exception ex){
            throw new Exception("客户 ("+customer+") "+"身份证格式异常: "+idCard);
        }

        try{
            new BigDecimal(workhours);
        }catch (Exception ex){
            throw new Exception("客户 ("+customer+") "+"考勤小时数填写错误: "+workhours);
        }

        try{
            new BigDecimal(amount);
        }catch (Exception ex){
            throw new Exception("客户 ("+customer+") "+"金额异常: "+amount);
        }
    }
}
