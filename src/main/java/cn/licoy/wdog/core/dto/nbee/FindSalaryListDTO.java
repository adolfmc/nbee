package cn.licoy.wdog.core.dto.nbee;import cn.licoy.wdog.core.dto.SplitPageDTO;import com.fasterxml.jackson.annotation.JsonFormat;import lombok.Data;import java.math.BigDecimal;import java.util.Date;/** * @author mc * @version Tue Jun 08 12:09:43 2021 */@Datapublic class FindSalaryListDTO extends SplitPageDTO  {   private String id;   private String projectName;   private String companyId;   private String batchNo;   private String batchName;   private BigDecimal payNumber;   private BigDecimal payAmount;   private String payStatus;   private String paySubstatus;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;   private String checkInfo;   private String payDesc;   private String createUser;   private String company;   private String fileName;   private String fileAddress;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date kaoqingDate;   private String productName;   private String amountSource;   private String payAccount;   private String zhidanUser;   private String fuheUser;   private String fafangUser;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date zhidanDate;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date fuheDate;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date fafangDate;   private BigDecimal avgAmount;   private BigDecimal maxAmount;   private BigDecimal paybanlanceAmount;   private BigDecimal feeAmount;   private BigDecimal feeAccbanlance;   private String fafangDesc;   private String fuheUser2;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date fuheDate2;}