package cn.licoy.wdog.core.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    //3.添加定时任务
    @Scheduled(cron = "0 0/5 * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void checkPayOrderInvalid() {

        System.err.println("checkPayOrderInvalid: " + LocalDateTime.now());
    }


    //3.添加定时任务
    @Scheduled(cron = "0 0/5 * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void checkPayOrderIsFinish() throws  Exception{

    }


//    //3.添加定时任务
//    @Scheduled(cron = "0 0/5 * * * ?")
//    //或直接指定时间间隔，例如：5秒
//    //@Scheduled(fixedRate=5000)
//    private void checkOverdueOrder() throws  Exception{
//        List<Order> orders = service.getOrdersByStatus("2");
//        for(Order order :orders){
//
//            String s1 =  DateFormatUtils.format(new Date() ,"YYYY-MM-dd");
//            String s2 = order.getAppotDate();
//
//            if(  DateUtils.parseDate( s2 ,new String[]{"yyyy-MM-dd"}).before(  DateUtils.parseDate( s1 ,new String[]{"yyyy-MM-dd"})   )  ){
//                //支付失效
//                order.setStatus("4");
//                service.updateById(order);
//            }
//        }
//    }
}
