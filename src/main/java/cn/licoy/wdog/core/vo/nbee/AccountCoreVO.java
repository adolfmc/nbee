package cn.licoy.wdog.core.vo.nbee;import lombok.Data;import java.util.Date;import com.fasterxml.jackson.annotation.JsonFormat;import java.math.BigDecimal;/** * @author mc * @version Thu May 06 18:58:30 2021 */@Datapublic class AccountCoreVO{   private String id;   private String companyId;   private String accountType;   private BigDecimal t3currentPaynumber;   private BigDecimal t3feeBanlance;   private BigDecimal t3payamountBanlance;   private BigDecimal t3creditAmount;   private BigDecimal t3creditPayamount;   private BigDecimal t3creditAmountBanlance;   private BigDecimal t3payAmount;   private BigDecimal t3payPreacceptamt;   private BigDecimal t3payAcceptedamt;   private BigDecimal pfpreinfeeAccount;   private BigDecimal pfincomeAccount;   private BigDecimal pfpaidallAmount;   private BigDecimal pfpaidAmount;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;   private BigDecimal personAmount;   private BigDecimal personPreacceptamt;   private BigDecimal personAcceptedamt;   private String personIdcard;   private String personName;   private String personMobile;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date personIndate;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date personUpdatetime;   private String personPickstatus;}