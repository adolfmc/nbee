package cn.licoy.wdog.core.entity.nbee;import com.baomidou.mybatisplus.annotations.TableId;import com.baomidou.mybatisplus.annotations.TableName;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import java.math.BigDecimal;import lombok.NoArgsConstructor;import java.io.Serializable;import com.fasterxml.jackson.annotation.JsonFormat;import java.util.Date;/** * @author mc * @version Sun May 02 11:04:52 2021 */@Data@NoArgsConstructor@AllArgsConstructor@Builder@TableName(value ="biz_accountingentries")public class Accountingentries implements Serializable {   @TableId   private String id;   private String dataType;   private String bizType;   private String dataId;   private BigDecimal amount;   private String inId;   private String outId;   private String desc;   @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")   private Date createDate;}