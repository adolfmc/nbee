package cn.licoy.wdog.core.controller.nbee;

import cn.licoy.wdog.common.annotation.SysLogs;
import cn.licoy.wdog.common.bean.ResponseCode;
import cn.licoy.wdog.common.bean.ResponseResult;
import cn.licoy.wdog.common.controller.AppotBaseController;
import cn.licoy.wdog.common.util.HttpTools;
import cn.licoy.wdog.core.dto.nbee.AccountCoreAddDTO;
import cn.licoy.wdog.core.dto.nbee.AccountCoreUpdateDTO;
import cn.licoy.wdog.core.dto.nbee.FindAccountCoreDTO;
import cn.licoy.wdog.core.entity.nbee.*;
import cn.licoy.wdog.core.service.nbee.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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


    @RequestMapping(value = {"/alipay_uni_transfer_return"})
    public ResponseResult alipay_uni_transfer_return(HttpServletRequest httprequest, HttpServletResponse htttpresponse) throws Exception {
        String out_biz_no = httprequest.getParameter("out_biz_no");
        String biz_content = httprequest.getParameter("biz_content");
        Map result = (Map) JSON.parse(biz_content);

        String status = httprequest.getParameter("status");
        String code = httprequest.getParameter("code");
        String msg = httprequest.getParameter("msg");
        String submsg = httprequest.getParameter("sub_msg");
        String accountId = out_biz_no.split("_")[0];


        System.out.println("*************************** alipay_uni_transfer_return *************************************");
//        accountPersion.getId()+"_"+batch_no+"_company_"+tppTixian.getId()
        System.out.println("out_biz_no="+out_biz_no);
        System.out.println("code="+code);
        System.out.println("status="+status);
        System.out.println("submsg="+submsg);
        System.out.println("msg="+msg);
        System.out.println("accountId="+accountId);
        String txid = out_biz_no.split("_")[2];
        TppTixian tppTixian = tppTixianService.selectById(txid);


        if(out_biz_no.contains("company") && tppTixian!=null ){
            tppTixian.setRespcompanyCode(result.get("status")+"" );
            tppTixian.setRespcompanyMsg(msg);

        }else if(out_biz_no.contains("bankspersion") && tppTixian!=null){
            if("FINISH".equals(result.get("action_type")+"" )   &&  tppTixian.getResppersionCode()==null ){



                if( "FAIL".equals(result.get("status")+"" ) ){
                    tppTixian.setResppersionMsg( result.get("fail_reason")+"" );
                    tppTixian.setResppersionCode( result.get("status")+"" );

                    tppTixian.setReqpersionMsg(  result.get("fail_reason")+""  );
                    tppTixian.setReqpersionCode( result.get("status")+"" );

                    //????????????
                    EntityWrapper<SalaryListDetails> wrapper = new EntityWrapper<>();
                    wrapper.eq("txid",txid);
                    List<SalaryListDetails> salaryListDetails =  salaryListDetailsService.selectList(wrapper);
                    System.out.println("-------------------------------????????????-----------------------------------"+salaryListDetails.size());
                    if(!salaryListDetails.isEmpty()){
                        for(SalaryListDetails salaryListDetails1:salaryListDetails){
                            salaryListDetails1.setCompanyTransferDate(new Date());
                            salaryListDetails1.setPickStatus("?????????");
                            salaryListDetails1.setPickTime(new Date());
                        }
                        salaryListDetailsService.insertOrUpdateBatch(salaryListDetails);
                    }

                    //????????????
                    AccountCore accountCore = accountCoreService.selectById(accountId);
                    accountCore.setPersonPickstatus("?????????");
                    accountCoreService.insertOrUpdate(accountCore);
                }else if("SUCCESS".equals(result.get("status")+"") ){
                    tppTixian.setResppersionMsg( result.get("status")+"" );
                    tppTixian.setResppersionCode( result.get("status")+"" );

                    tppTixian.setReqpersionMsg( result.get("status")+""  );
                    tppTixian.setReqpersionCode( result.get("status")+"" );
                    //????????????
                    //????????????????????????
                    AccountCore accountCore = accountCoreService.selectById(accountId);
                    update_data_infoflow(accountCore.getPersonIdcard(),accountCore.getPersonName(),accountCore.getCompanyId()  ,txid );


                }
            }
        }else if(out_biz_no.contains("alipaypersion") && tppTixian!=null){
            tppTixian.setRespcompanyCode(result.get("status")+"" );
            tppTixian.setRespcompanyMsg(msg);
        }


        tppTixianService.insertOrUpdate( tppTixian);
        System.out.println("*************************** alipay_uni_transfer_return *************************************");
        return ResponseResult.e(ResponseCode.OK);
    }













    public void zhifubaopersion_uni_transfer(String accountId ,String idcard, String currentCompany,BigDecimal amount,String batch_no,TppAlipay persionTpp) throws Exception {
        System.out.println("=============================zhifubaopersion_uni_transfer=======================================");
        System.out.println(amount.doubleValue());
        // ??????????????? ????????????????????????
//        persionTpp

        //??????????????????????????????
        String ss = System.nanoTime()+"";
        ss = ss.substring(ss.length()-5, ss.length());
        EntityWrapper<TppAlipay> wrapperTppAlipayCompany = new EntityWrapper<>();
        wrapperTppAlipayCompany.eq("company",currentCompany);
        wrapperTppAlipayCompany.eq("data_type","company");
        TppAlipay companyTpp =  tppAlipayService.selectOne(wrapperTppAlipayCompany);
        batch_no = batch_no.replace("alipaycompany","alipaypersion");

        String txid = batch_no.split("_")[2];
        String out_biz_no = batch_no ; // accountId+"_"+persionTpp.getPayChannel()+"persion_"+txid+"_"+ss;
        String orderTitle =companyTpp.getCompany()+"????????????";
//        orderTitle = URLDecoder.decode(orderTitle ,"UTF-8");
        String payer_show_name = companyTpp.getCompany()+"????????????";


        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("out_biz_no",out_biz_no);
        parameters.put("trans_amount",""+amount.doubleValue());
        parameters.put("order_title",orderTitle);
        parameters.put("payee_zhifubao",persionTpp.getAlipayLoginId());
        parameters.put("payee_name",persionTpp.getName());
        parameters.put("payer_identity",companyTpp.getAccountBookId());
        parameters.put("payer_name",companyTpp.getAlipayLoginId());
        parameters.put("payer_agreement_no",companyTpp.getAgreementNo());
        parameters.put("payer_show_name",payer_show_name);
        parameters.put("remark",orderTitle);
        String accountbook_uni_transfer_result = HttpTools.doPost("https://yushangcc.com/alipay/zhifubaopersion_uni_transfer2",parameters);
        System.out.println("accountbook_uni_transfer_result= "+accountbook_uni_transfer_result);
        System.out.println("-------------------------------zhifubaopersion_uni_transfer_transfer_result-----------------------------------");
        System.out.println(accountbook_uni_transfer_result);
        Map result = (Map) JSON.parse(accountbook_uni_transfer_result);


        String code = result.get("code")+"";
        TppTixian tppTixiant= tppTixianService.selectById(txid) ;



        if("10000".equals(code)){
            tppTixiant.setReqpersionCode("SUCCESS");
            tppTixiant.setReqpersionMsg("SUCCESS");
            tppTixianService.insertOrUpdate(tppTixiant);
            //????????????
            //????????????????????????
            AccountCore accountCore = accountCoreService.selectById(accountId);
            update_data_infoflow(accountCore.getPersonIdcard(),accountCore.getPersonName(),accountCore.getCompanyId()  ,txid );

        }else{
            //????????????
            String submsg = result.get("sub_msg")+"";
            tppTixiant.setReqpersionCode("FAIL");
            tppTixiant.setReqpersionMsg(submsg);
            tppTixianService.insertOrUpdate(tppTixiant);


            EntityWrapper<SalaryListDetails> wrapper = new EntityWrapper<>();
            wrapper.eq("txid",txid);
            List<SalaryListDetails> salaryListDetails =  salaryListDetailsService.selectList(wrapper);
            System.out.println("-------------------------------????????????-----------------------------------"+salaryListDetails.size());



            //???????????????????????????
            EntityWrapper<AccountSalarydetails> wrapperAccountSalarydetails = new EntityWrapper<>();
            wrapperAccountSalarydetails.eq("account_id",accountId);
            wrapperAccountSalarydetails.eq("status","?????????");
            List<AccountSalarydetails> accountSalarydetails =  accountSalarydetailsService.selectList(wrapperAccountSalarydetails);

            if(!salaryListDetails.isEmpty()){
                for(SalaryListDetails salaryListDetails1:salaryListDetails){

                    //??????????????????????????????
                    for(AccountSalarydetails acct :accountSalarydetails){
                        if(salaryListDetails1.getId().equals( acct.getSalaryDetailsId() ) ){
                            salaryListDetails1.setCompanyTransferDate(new Date());
                            salaryListDetails1.setPickStatus("?????????");
                            salaryListDetails1.setPickTime(new Date());
                        }
                    }
                }
                salaryListDetailsService.insertOrUpdateBatch(salaryListDetails);
            }

            //????????????
            AccountCore accountCore = accountCoreService.selectById(accountId);
            accountCore.setPersonPickstatus("?????????");
            accountCoreService.insertOrUpdate(accountCore);
        }


    }

    private void bankspersion_uni_transfer(String accountId ,String idcard, String currentCompany,BigDecimal amount,String batch_no,TppAlipay persionTpp) throws Exception {
        System.out.println("=============================bankspersion_uni_transfer=======================================");
        System.out.println(amount.doubleValue());
        // ??????????????? ??????????????????

        String ss = System.nanoTime()+"";
        ss = ss.substring(ss.length()-5, ss.length());
        //??????????????????????????????
        EntityWrapper<TppAlipay> wrapperTppAlipayCompany = new EntityWrapper<>();
        wrapperTppAlipayCompany.eq("company",currentCompany);
        wrapperTppAlipayCompany.eq("data_type","company");
        TppAlipay companyTpp =  tppAlipayService.selectOne(wrapperTppAlipayCompany);
//        String out_biz_no = accountId+"_bankspersion_"+txid+"_"+ss;
        batch_no = batch_no.replace("bankscompany","bankspersion");

        String txid = batch_no.split("_")[2];
        String out_biz_no = batch_no ; // accountId+"_"+persionTpp.getPayChannel()+"persion_"+txid+"_"+ss;


        String orderTitle = companyTpp.getCompany()+"????????????";
        String payer_show_name = companyTpp.getCompany()+"????????????";


        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("out_biz_no",out_biz_no);
        parameters.put("trans_amount",""+amount.doubleValue());
        parameters.put("order_title",orderTitle);
        parameters.put("payee_bank_id",persionTpp.getAlipayLoginId());
        parameters.put("payee_name",persionTpp.getName());
        parameters.put("payer_identity",companyTpp.getAccountBookId());
        parameters.put("payer_name",companyTpp.getAlipayLoginId());
        parameters.put("payer_agreement_no",companyTpp.getAgreementNo());
        parameters.put("payer_show_name",payer_show_name);
        parameters.put("remark",orderTitle);
        String accountbook_uni_transfer_result = HttpTools.doPost("https://yushangcc.com/alipay/bankspersion_uni_transfer",parameters);
        System.out.println("accountbook_uni_transfer_result= "+accountbook_uni_transfer_result);
        System.out.println("-------------------------------_result-----------------------------------");
        System.out.println(accountbook_uni_transfer_result);
        Map result = (Map) JSON.parse(accountbook_uni_transfer_result);

        String submsg = result.get("sub_msg")+"";
        String code = result.get("code")+"";
        TppTixian tppTixiant= tppTixianService.selectById(txid) ;
        if(!"10000".equals(code )){
            tppTixiant.setReqpersionCode("FAIL");
            tppTixiant.setReqpersionMsg(submsg);
            AccountCore accountCore = accountCoreService.selectById(accountId);
            accountCore.setPersonPickstatus("?????????");
            accountCoreService.insertOrUpdate(accountCore);
        }else{
            tppTixiant.setReqpersionCode(code);
            tppTixiant.setReqpersionMsg(code);
        }

        tppTixianService.insertOrUpdate(tppTixiant);





    }




    //??????????????? ????????? ???????????????
    public String alipay_accountbook_uni_transfer(String idcard, String currentCompany,BigDecimal amount ,String accountId,String batch_no,TppAlipay persiontpp) throws Exception {
        EntityWrapper<TppAlipay> wrapperTppAlipayPlatform = new EntityWrapper<>();
        wrapperTppAlipayPlatform.eq("company","??????(??????)????????????????????????");
        wrapperTppAlipayPlatform.eq("data_type","company");
        TppAlipay platformTpp =  tppAlipayService.selectOne(wrapperTppAlipayPlatform);


        EntityWrapper<TppAlipay> wrapperTppAlipayCompany = new EntityWrapper<>();
        wrapperTppAlipayCompany.eq("company",currentCompany);
        wrapperTppAlipayCompany.eq("data_type","company");
        TppAlipay companyTpp =  tppAlipayService.selectOne(wrapperTppAlipayCompany);
        String out_biz_no = batch_no;
        String order_title = companyTpp.getCompany()+"????????????";
        String payer_show_name = companyTpp.getCompany()+"????????????";

        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("out_biz_no",out_biz_no);
        parameters.put("trans_amount",""+amount.doubleValue());
        parameters.put("order_title",order_title);
        parameters.put("payee_identity",companyTpp.getAccountBookId());
        parameters.put("payee_name",companyTpp.getAlipayLoginId());
        parameters.put("payee_agreement_no",companyTpp.getAgreementNo());
        parameters.put("payer_identity",platformTpp.getAccountBookId());
        parameters.put("payer_name",platformTpp.getCompany());
        parameters.put("payer_agreement_no",platformTpp.getAgreementNo());
        parameters.put("payer_show_name",payer_show_name);
        String accountbook_uni_transfer_result = HttpTools.doPost("https://yushangcc.com/alipay/accountbook_uni_transfer",parameters);
        System.out.println("-------------------------------accountbook_uni_transfer_result-----------------------------------");
        System.out.println(accountbook_uni_transfer_result);
        Map result = (Map) JSON.parse(accountbook_uni_transfer_result);

        String txid = out_biz_no.split("_")[2];
        String submsg = result.get("sub_msg")+"";
        String code = result.get("code")+"";
        TppTixian tppTixiant= tppTixianService.selectById(txid) ;
        tppTixiant.setReqcompanyCode(code);
        tppTixiant.setReqcompanyMsg(submsg);
        tppTixianService.insertOrUpdate(tppTixiant);

        if("10000".equals(code)){
                //???????????? ?????? ???????????????????????????
                AccountCore accountCore = accountCoreService.selectById(accountId);
                if("alipay".equals(persiontpp.getPayChannel())){
                    zhifubaopersion_uni_transfer(accountId,accountCore.getPersonIdcard() , accountCore.getCompanyId(), accountCore.getPersonPreacceptamt() ,batch_no,persiontpp);

                }else if("banks".equals(persiontpp.getPayChannel())){
                    bankspersion_uni_transfer(accountId,accountCore.getPersonIdcard() , accountCore.getCompanyId(), accountCore.getPersonPreacceptamt() ,batch_no,persiontpp);
                }

        }


        return accountbook_uni_transfer_result;
    }



    @RequestMapping(value = {"/pay"})
    @ApiOperation(value = "pay")
    @SysLogs("pay")
    public ResponseResult pay(String name, String idcard, String currentCompany, BigDecimal amount,String choose_pay_id) throws Exception{
        //????????????????????????
        if(check_account_amount(name,idcard,currentCompany,amount)==false){
            return ResponseResult.e(ResponseCode.FAIL ,"??????????????????,??????????????????!");
        };
        TppAlipay persionTpp =  tppAlipayService.selectById(choose_pay_id);


        AccountCore accountPersion = null;
        EntityWrapper<AccountCore> wrapperAccountCorepersion = new EntityWrapper<>();
        wrapperAccountCorepersion.eq("person_idcard",idcard) ;
        wrapperAccountCorepersion.eq("person_name",name) ;
        wrapperAccountCorepersion.eq("company_id",currentCompany) ;
        wrapperAccountCorepersion.eq("account_type","????????????");
        accountPersion = accountCoreService.selectOne(wrapperAccountCorepersion) ;
        accountPersion.setPersonPickstatus("?????????");
        accountCoreService.insertOrUpdate(accountPersion);

        //??????????????????
        TppTixian tppTixian =new TppTixian();
        tppTixian.setAmount(amount);
        tppTixian.setIdCard(idcard);
        tppTixian.setName(accountPersion.getPersonName());
        tppTixian.setCompany(currentCompany);
        tppTixian.setCreateDate(new Date());
        tppTixian.setTixType(persionTpp.getPayChannel());
        tppTixian.setTixAccount( persionTpp.getAlipayLoginId());
        tppTixianService.insert(tppTixian);


        List companys =null;
        AccountCore account=null;
        //???????????????
        EntityWrapper<SalaryListDetails> wrapper = new EntityWrapper<>();
        wrapper.eq("name",name);
        wrapper.eq("id_card",idcard);
        wrapper.eq("company",currentCompany);
        wrapper.eq("pick_status","?????????");
        List<SalaryListDetails> salaryListDetails =  salaryListDetailsService.selectList(wrapper);
        if(salaryListDetails.size()==0){
            return ResponseResult.e(ResponseCode.FAIL ,"??????????????????,??????????????????!");
        }



        //???????????????????????????
        EntityWrapper<AccountSalarydetails> wrapperAccountSalarydetails = new EntityWrapper<>();
        wrapperAccountSalarydetails.eq("account_id",accountPersion.getId());
        wrapperAccountSalarydetails.eq("status","?????????");
        List<AccountSalarydetails> accountSalarydetails =  accountSalarydetailsService.selectList(wrapperAccountSalarydetails);


        for(SalaryListDetails salaryListDetails1:salaryListDetails){

            //??????????????????????????????
            for(AccountSalarydetails acct :accountSalarydetails){
                if(salaryListDetails1.getId().equals( acct.getSalaryDetailsId() ) ){
                    salaryListDetails1.setCompanyTransferDate(new Date());
                    salaryListDetails1.setPickStatus("?????????");
                    salaryListDetails1.setOutBizNo(salaryListDetails1.getOutBizNo()+"_"+tppTixian.getId());
                    salaryListDetails1.setPickTime(new Date());
                    salaryListDetails1.setTxid(tppTixian.getId());
                }
            }
        }
        //1 ??????????????????
        salaryListDetailsService.insertOrUpdateBatch(salaryListDetails);


        try{
            String batch_no = "";

            String ss = System.nanoTime()+"";
            ss = ss.substring(ss.length()-5, ss.length());

            if( "alipay".equals(persionTpp.getPayChannel()) ){
                batch_no=accountPersion.getId()+"_alipaycompany_"+tppTixian.getId()+"_"+ss;
            }else if( "banks".equals( persionTpp.getPayChannel() ) ){
                batch_no=accountPersion.getId()+"_bankscompany_"+tppTixian.getId()+"_"+ss;
            }
            // ???????????????????????? ?????????????????????
            alipay_accountbook_uni_transfer(idcard,currentCompany,amount,accountPersion.getId() ,batch_no,persionTpp);

        }catch (Exception ex){


            for(SalaryListDetails salaryListDetails1:salaryListDetails){
                //??????????????????????????????
                for(AccountSalarydetails acct :accountSalarydetails){
                    if(salaryListDetails1.getId().equals( acct.getSalaryDetailsId() ) ){
                        salaryListDetails1.setCompanyTransferDate(new Date());
                        salaryListDetails1.setPickStatus("?????????");
                        salaryListDetails1.setOutBizNo(salaryListDetails1.getOutBizNo()+"_"+tppTixian.getId());
                        salaryListDetails1.setPickTime(new Date());
                        salaryListDetails1.setTxid(tppTixian.getId());
                    }
                }
            }
            //1 ??????????????????
            salaryListDetailsService.insertOrUpdateBatch(salaryListDetails);
            accountPersion.setPersonPickstatus("?????????");
            accountCoreService.insertOrUpdate(accountPersion);


            ex.printStackTrace();
            return ResponseResult.e(ResponseCode.FAIL ,"??????????????????,??????????????????!"+ex.getMessage());
        }


        return ResponseResult.e(ResponseCode.OK ,accountPersion);
    }


    private void update_data_infoflow(String idcard, String name,String currentCompany, String txid) throws Exception {
        //???????????? ????????????
        EntityWrapper<AccountCore> wrapperAccountCorepersion = new EntityWrapper<>();
        wrapperAccountCorepersion.eq("person_idcard",idcard) ;
        wrapperAccountCorepersion.eq("person_name",name) ;
        wrapperAccountCorepersion.eq("company_id",currentCompany) ;
        wrapperAccountCorepersion.eq("account_type","????????????");
        AccountCore accountPersion = accountCoreService.selectOne(wrapperAccountCorepersion) ;
        TppTixian tppTixian = tppTixianService.selectById(txid);
        BigDecimal amount2  =tppTixian.getAmount();
        String pickstatus = "?????????";




        //2 ????????????????????????
        EntityWrapper<AccountCore> wrapperAccountCore = new EntityWrapper<>();
        wrapperAccountCore.eq("company_id",currentCompany) ;
        wrapperAccountCore.eq("account_type","????????????") ;
        AccountCore accountCompany = accountCoreService.selectOne(wrapperAccountCore) ;
        //?????????
        accountCompany.setT3payAcceptedamt( accountCompany.getT3payAcceptedamt().add(amount2) );
        //?????????
        accountCompany.setT3payPreacceptamt( accountCompany.getT3payPreacceptamt().subtract( amount2 )  );
        //
        accountCoreService.insertOrUpdate(accountCompany);


        //3 ????????????????????????
        AccountCore accountPlatform = accountCoreService.selectById(1L);
        //pf??????????????????
        accountPlatform.setPfpaidallAmount( accountPlatform.getPfpaidallAmount().subtract(amount2) );
        //pf???????????????
        accountPlatform.setPfpaidAmount( accountPlatform.getPfpaidAmount().add(amount2 ));
        accountCoreService.insertOrUpdate(accountPlatform);


        //4 ??????????????????
        Accountingentries accountingentries = new Accountingentries();
        accountingentries.setAmount( amount2);
        accountingentries.setDataType("????????????");
        accountingentries.setBizType("??????");
        accountingentries.setDataId(idcard+" "+name+" "+currentCompany);
        accountingentries.setInId(idcard+" "+name);
        accountingentries.setOutId("??????");
        accountingentries.setDesc(  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) +"=????????????; ????????????="+amount2.doubleValue()+";????????????="+currentCompany );
        accountingentries.setCreateDate(new Date());
        accountingentriesService.insertOrUpdate(accountingentries) ;


        //5 ??????????????????
        //per???????????????
        if( accountPersion.getPersonPreacceptamt().compareTo( new BigDecimal(0))==-1 ){
            throw new Exception("??????????????????,??????????????????!");
        }

        accountPersion.setPersonPreacceptamt( accountPersion.getPersonPreacceptamt().subtract(amount2) );
        //per???????????????
        accountPersion.setPersonAcceptedamt(  accountPersion.getPersonAcceptedamt().add(amount2) );
        accountPersion.setPersonPickstatus("?????????");
        accountCoreService.insertOrUpdate(accountPersion);


        //?????? ??????????????? ??????
        EntityWrapper<AccountSalarydetails> wrapperAccountSalarydetails = new EntityWrapper<>();
        wrapperAccountSalarydetails.eq("account_id",accountPersion.getId()) ;
        wrapperAccountSalarydetails.eq("status","?????????") ;
        List<AccountSalarydetails> accountSalarydetails =  accountSalarydetailsService.selectList(wrapperAccountSalarydetails);
        if(!accountSalarydetails.isEmpty()){
            for(AccountSalarydetails accs:accountSalarydetails){
                accs.setTixId(txid);
                accs.setStatus(pickstatus);
            }
            accountSalarydetailsService.insertOrUpdateBatch(accountSalarydetails);
        }



        //???????????????
        EntityWrapper<SalaryListDetails> wrapper = new EntityWrapper<>();
        wrapper.eq("txid",txid);
        List<SalaryListDetails> salaryListDetails2 =  salaryListDetailsService.selectList(wrapper);
        if(!salaryListDetails2.isEmpty() ){
            for(SalaryListDetails salaryListDetails1:salaryListDetails2){
                amount2 = amount2.add( salaryListDetails1.getAmount()) ;
                salaryListDetails1.setPickStatus(pickstatus);
                salaryListDetails1.setPickTime(new Date());
                salaryListDetails1.setPersionTransferDate(new Date());
            }
            //1 ??????????????????
            salaryListDetailsService.insertOrUpdateBatch(salaryListDetails2);
        }

    }


    private boolean check_account_amount(String name, String idcard, String currentCompany, BigDecimal amount) {
        EntityWrapper<AccountCore> wrapperAccountCorepersion = new EntityWrapper<>();
        wrapperAccountCorepersion.eq("person_idcard",idcard) ;
        wrapperAccountCorepersion.eq("person_name",name) ;
        wrapperAccountCorepersion.eq("company_id",currentCompany) ;
        wrapperAccountCorepersion.eq("account_type","????????????");
        AccountCore accountPersion = accountCoreService.selectOne(wrapperAccountCorepersion) ;
        System.out.println("check_account_amount = "+accountPersion.getPersonPreacceptamt().compareTo(amount));
        if(accountPersion.getPersonPreacceptamt().compareTo(amount) ==-1  || accountPersion.getPersonPreacceptamt().compareTo( new BigDecimal( -1) )==-1 ){
            return false;
        }else{
            return true;
        }
   }

    @RequestMapping(value = {"/details"})
    @ApiOperation(value = "details")
    @SysLogs("details")
    public ResponseResult details(String name, String idcard, String currentCompany, BigDecimal amount){

        Map<String,Object> vv= new HashMap<String,Object>();
        String allamountsql = "select sum(amount) amount from biz_nbee.biz_tpp_tixian where name=? and id_card =? and company =? and reqpersion_code ='SUCCESS'";
        String allamount=jdbcTemplate.queryForMap(allamountsql,name,idcard,currentCompany).get("amount")+"";

        ArrayList result = new ArrayList();
        String sqlmonth = "select date_format(create_date,'%Y/%m???') datee ,sum(case when reqpersion_code ='SUCCESS' then amount  else 0 end) amount   from biz_nbee.biz_tpp_tixian btt where name=? and id_card =? and company =? group by datee order by datee desc";
        String sqldays = "select date_format(create_date ,'%Y-%m-%d %H:%i:%s') datee,amount,(case when reqpersion_code='SUCCESS' then '????????????' when reqpersion_code='FAIL' then '????????????' else '?????????' end) reqpersion_code,reqpersion_msg   from biz_nbee.biz_tpp_tixian where name=? and id_card =? and company =? and  date_format(create_date ,'%Y/%m???')=? order by create_date desc ";
        List<Map<String, Object>> months = jdbcTemplate.queryForList(sqlmonth,name,idcard,currentCompany);
        for(Map<String, Object> m:months ){
            Map<String,Object> map= new HashMap<String,Object>();
            map.put("head",m.get("datee")+"  ?????????: ??? "+m.get("amount")+" ???");
            ArrayList<String[]> body=new ArrayList<String[]>();
            List<Map<String, Object>> days = jdbcTemplate.queryForList(sqldays,name,idcard,currentCompany,m.get("datee").toString());
            for(Map<String, Object> d:days ){
                body.add(new String[]{"'"+d.get("datee")+"', '+"+d.get("amount")+"', '"+d.get("reqpersion_code")+"' "+"', '"+d.get("reqpersion_msg")+"' "});
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

    @RequestMapping(value = {"/login"})
    @ApiOperation(value = "login")
    @SysLogs("login")
    public ResponseResult get(String name,String idcard){
       List companys =null;
       AccountCore account=null;
               // ????????????
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
        // ????????????
        EntityWrapper<Customer> wrapper = new EntityWrapper<>();
        wrapper.eq("name",name);
        wrapper.eq("id_card ",idcard);
        Customer customer = customerService.selectOne(wrapper);
        if(customer!=null){
            // ????????????
            String sql ="select company_id,min(person_indate) indate from biz_nbee.biz_account_core bac where 1=1 and bac.person_idcard =? and bac.person_name =? and bac.account_type ='????????????' group by company_id ";
            companys = jdbcTemplate.queryForList(sql,idcard,name);

            // ????????????
            EntityWrapper<AccountCore> wrapperaccount = new EntityWrapper<>();
            wrapperaccount.eq("person_name",name);
            wrapperaccount.eq("person_idcard ",idcard);
            wrapperaccount.eq("company_id ",currentCompany);

            wrapperaccount.eq("account_type ","????????????");
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
    @SysLogs("????????????AccountCore??????")
    public ResponseResult panel(@RequestBody @Validated @ApiParam(value = "getPanel") FindAccountCoreDTO findAccountCoreDTO){
        AccountCore accountCore= AccountCoreService.findAccountCoreById("1");
        return ResponseResult.e(ResponseCode.OK,accountCore);
    }

    @PostMapping(value = {"/companyPanel"})
    @ApiOperation(value = "companyPanel")
    @SysLogs("????????????AccountCore??????")
    public ResponseResult companyPanel(@RequestBody @Validated @ApiParam(value = "companyPanel") FindAccountCoreDTO findAccountCoreDTO){
        AccountCore accountCore= AccountCoreService.findAccountCoreById("2");
        return ResponseResult.e(ResponseCode.OK,accountCore);
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "??????ID??????AccountCore??????")
    @SysLogs("??????ID??????AccountCore??????")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "AccountCoreID") String id){
        return ResponseResult.e(ResponseCode.OK,AccountCoreService.findAccountCoreById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "??????AccountCore")
    @SysLogs("??????AccountCore")
    public ResponseResult lock(@PathVariable("id") @ApiParam(value = "AccountCore??????ID") String id){
        AccountCoreService.statusChange(id, 0);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "??????AccountCore")
    @SysLogs("??????AccountCore")
    public ResponseResult remove(@PathVariable("id") @ApiParam(value = "AccountCore??????ID") String id){
        AccountCoreService.removeAccountCore(id);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "??????AccountCore")
    @SysLogs("??????AccountCore")
    public ResponseResult add(@RequestBody @Validated @ApiParam(value = "AccountCore??????") AccountCoreAddDTO addDTO){
        AccountCoreService.add(addDTO);
        return ResponseResult.e(ResponseCode.OK);
    }

    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "??????AccountCore")
    @SysLogs("??????AccountCore")
    public ResponseResult update(@PathVariable("id") @ApiParam(value = "AccountCore??????ID") String id,
                                 @RequestBody @Validated @ApiParam(value = "AccountCore??????") AccountCoreUpdateDTO updateDTO){
        AccountCoreService.update(id,updateDTO);
        return ResponseResult.e(ResponseCode.OK);
    }




    @RequestMapping(value = {"/alipayBangding"})
    @ApiOperation(value = "alipayBangding")
    @SysLogs("alipayBangding")
    public ResponseResult alipayBangding(String name, String idcard, String currentCompany,String alipay_login_id,String pay_chennel){
        EntityWrapper<TppAlipay> wrapper = new EntityWrapper<>();
        wrapper.eq("name",name);
        wrapper.eq("id_card ",idcard);
        wrapper.eq("company ",currentCompany);
        wrapper.eq("alipay_login_id ",alipay_login_id);
        wrapper.eq("pay_channel ",pay_chennel);
        wrapper.eq("data_type","????????????");
        TppAlipay tppAlipay = tppAlipayService.selectOne(wrapper);
        if(tppAlipay==null ){
            TppAlipay tppAlipay1 = new TppAlipay();
            tppAlipay1.setName(name);
            tppAlipay1.setIdCard(idcard);
            tppAlipay1.setCompany(currentCompany);
            tppAlipay1.setDataType("????????????");
            tppAlipay1.setCreateDate(new Date());
            if("alipay".equals(pay_chennel)){
                tppAlipay1.setPayChannel("alipay");
                tppAlipay1.setAlipayLoginId(alipay_login_id);

            }else if ("banks".equals( pay_chennel )){
                tppAlipay1.setPayChannel("banks");
                tppAlipay1.setAlipayLoginId(alipay_login_id);
            }

            tppAlipayService.insert(tppAlipay1);
            tppAlipay=tppAlipay1;
            return ResponseResult.e(ResponseCode.OK, tppAlipay);
        }else{

            return ResponseResult.e(ResponseCode.FAIL, "???????????????!");
        }

    }
}
