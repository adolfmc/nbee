package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.core.dto.nbee.FindTppAlipayDTO;
import cn.licoy.wdog.core.dto.nbee.TppAlipayAddDTO;
import cn.licoy.wdog.core.dto.nbee.TppAlipayUpdateDTO;
import cn.licoy.wdog.core.entity.nbee.Customer;
import cn.licoy.wdog.core.entity.nbee.TppAlipay;
import cn.licoy.wdog.core.service.nbee.TppAlipayService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author mc
 * @version Thu Jun 03 11:04:40 2021
 */
@RestController
@RequestMapping(value="/TppAlipay")
public class TppAlipayController  extends AppotBaseController{

   @Autowired
   private TppAlipayService TppAlipayService;

   public TppAlipayService getTppAlipayService() {
      return TppAlipayService;
   }


    @RequestMapping(value = {"/query_alipay_user_id"}, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseResult query_alipay_user_id(String id_card ,String company) throws Exception {
//        String id_card = request.getParameter("id_card");
//        String company = request.getParameter("company");
        System.out.println("id_card -------------------------------");
        EntityWrapper<TppAlipay> wrapperTppAlipay = new EntityWrapper<>();
        wrapperTppAlipay.eq("id_card",id_card);
        wrapperTppAlipay.eq("company",company);
        wrapperTppAlipay.eq("data_type","普通客户");
        List<TppAlipay> tppAlipay2 =TppAlipayService.selectList(wrapperTppAlipay);

        return ResponseResult.e(ResponseCode.OK,tppAlipay2);
    }


    @RequestMapping(value = {"/save_alipay_user_info"}, produces = "application/json; charset=utf-8")
    @ResponseBody
    public void save_alipay_user_info(HttpServletRequest request, HttpServletResponse response,String customer_id
            ,String user_id,String avatar ,String gender,String nick_name) throws Exception {

        EntityWrapper<Customer> wrapperCustomer = new EntityWrapper<>();
        wrapperCustomer.eq("id",customer_id);
        Customer customer =customerService.selectOne(wrapperCustomer);

        if(customer!=null ){
            EntityWrapper<TppAlipay> wrapperTppAlipay = new EntityWrapper<>();
            wrapperTppAlipay.eq("id_card",customer.getIdCard());
            wrapperTppAlipay.eq("company",customer.getCurrentCompany());

            TppAlipay tppAlipay =TppAlipayService.selectOne(wrapperTppAlipay);
            if( tppAlipay ==null ){
                tppAlipay = new TppAlipay() ;
                tppAlipay.setCreateDate(  new java.util.Date());
                tppAlipay.setDataType("普通客户");
                tppAlipay.setAlipayImg(avatar);
                tppAlipay.setSex(gender);
                tppAlipay.setNickname(nick_name);
                tppAlipay.setUserId( user_id);
                tppAlipay.setCompany(customer.getCurrentCompany());
                tppAlipay.setIdCard(customer.getIdCard());
                tppAlipay.setName(customer.getName());

            }else{
                tppAlipay.setCompany(customer.getCurrentCompany());
                tppAlipay.setIdCard(customer.getIdCard());
                tppAlipay.setName(customer.getName());
                tppAlipay.setAlipayImg(avatar);
                tppAlipay.setSex(gender);
                tppAlipay.setNickname(nick_name);
                tppAlipay.setUserId( user_id);
            }

            TppAlipayService.insertOrUpdate(tppAlipay);



            String url1 = "http://yushangcc.com:8003/nbeeapp/#/pages/index/index?customer_id="+customer_id;

            System.out.println("alipays , url1 = "+ url1 );
            String submitFormData="<!DOCTYPE html><html xmlns='http://www.w3.org/1999/xhtml' xmlns:shiro='http://www.pollix.at/thymeleaf/shiro'  xmlns:th='http://www.thymeleaf.org' lang='en'><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'> <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'></head><body><script th:inline='javascript'>    window.onload = function () {        window.location.href = '"+url1+"';     }</script></body></html>";
            //客户端拿到submitFormData做表单提交
            //通过alipayClient调用API，获得对应的response类
            System.out.println("------------------------------------------------");
            System.out.println(submitFormData);
            System.out.println("------------------------------------------------");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println(submitFormData);
        }else{

        }



    }


   @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取TppAlipay数据")
    @SysLogs("分页获取TppAlipay数据")
    public ResponseResult get(@RequestBody @Validated @ApiParam(value = "TppAlipay获取过滤条件") FindTppAlipayDTO findTppAlipayDTO){
        return ResponseResult.e(ResponseCode.OK,TppAlipayService.getAllTppAlipayBySplitPage(findTppAlipayDTO));
    }

    @RequestMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取TppAlipay信息")
    @SysLogs("根据ID获取TppAlipay信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "TppAlipayID") String id){
        return ResponseResult.e(ResponseCode.OK,TppAlipayService.findTppAlipayById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定TppAlipay")
    @SysLogs("锁定TppAlipay")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "TppAlipay标识ID") String id){
        TppAlipayService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @RequestMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除TppAlipay")
    @SysLogs("删除TppAlipay")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "TppAlipay标识ID") String id){
        TppAlipayService.removeTppAlipay(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加TppAlipay")
    @SysLogs("添加TppAlipay")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "TppAlipay数据") TppAlipayAddDTO addDTO){
        EntityWrapper<TppAlipay> wrapperTppAlipay = new EntityWrapper<>();
        wrapperTppAlipay.eq("id_card",addDTO.getIdCard());
        wrapperTppAlipay.eq("company",addDTO.getCompany());
        wrapperTppAlipay.eq("alipay_login_id",addDTO.getAlipayLoginId());
        wrapperTppAlipay.eq("data_type","普通客户");
        List<TppAlipay> tppAlipay2 =TppAlipayService.selectList(wrapperTppAlipay);
        if(tppAlipay2.isEmpty()){
            TppAlipayService.add(addDTO);
            return ResponseResult.e(ResponseCode.OK);
        }else{
            return ResponseResult.e(ResponseCode.FAIL,"账号已存在!");
        }


    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新TppAlipay")
    @SysLogs("更新TppAlipay")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "TppAlipay标识ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "TppAlipay数据") TppAlipayUpdateDTO updateDTO){
        TppAlipayService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }
}
