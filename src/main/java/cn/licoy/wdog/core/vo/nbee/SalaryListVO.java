package cn.licoy.wdog.core.vo.nbee;import lombok.Data;import java.util.Date;import com.fasterxml.jackson.annotation.JsonFormat;import java.math.BigDecimal;/** * @author mc * @version Fri May 21 10:18:15 2021 */@Datapublic class SalaryListVO{   private String id;   private String projectName;   private String companyId;   private String batchNo;   private String batchName;   private BigDecimal payNumber;   private BigDecimal payAmount;   private String payStatus;   private String paySubstatus;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;   private String checkInfo;   private String payDesc;   private String createUser;   private String company;   private String fileName;   private String fileAddress;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date kaoqingDate;   private String productName;   private String amountSource;   private String payAccount;   private String zhidanUser;   private String fuheUser;   private String fafangUser;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date zhidanDate;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date fuheDate;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date fafangDate;   private BigDecimal avgAmount;   private BigDecimal maxAmount;   private BigDecimal paybanlanceAmount;   private BigDecimal feeAmount;   private BigDecimal feeAccbanlance;   private String fafangDesc;}