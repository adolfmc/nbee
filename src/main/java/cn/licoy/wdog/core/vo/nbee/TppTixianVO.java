package cn.licoy.wdog.core.vo.nbee;import lombok.Data;import java.util.Date;import com.fasterxml.jackson.annotation.JsonFormat;import java.math.BigDecimal;/** * @author mc * @version Sun Jun 13 17:49:58 2021 */@Datapublic class TppTixianVO{   private String id;   private String outBizNo;   private String salaryListDetailsId;   private BigDecimal amount;   private String txstatus;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;   private String msg;   private String name;   private String company;   private String idCard;   private String txstatus2;   private String msg2;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate2;   private String reqpersionCode;   private String reqpersionMsg;   private String resppersionCode;   private String resppersionMsg;   private String reqcompanyCode;   private String reqcompanyMsg;   private String respcompanyCode;   private String respcompanyMsg;   private String tixType;   private String tixAccount;}