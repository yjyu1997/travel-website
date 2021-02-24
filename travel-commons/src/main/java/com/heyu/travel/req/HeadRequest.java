package com.heyu.travel.req;


import com.heyu.travel.utils.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author heyu
 * @Description 请求默认参数
 * 主要做统一参数的接收，比如操作类型，操作的模块信息
 */
@AllArgsConstructor
@NoArgsConstructor
public class HeadRequest extends ToString {

   /**
    * @Description 模块
    */
   private String moduleName;

   /**
    * @Description 操作类型
    */
   private String operationType;

   public String getModuleName() {
      return moduleName;
   }

   public void setModuleName(String moduleName) {
      this.moduleName = moduleName;
   }

   public String getOperationType() {
      return operationType;
   }

   public void setOperationType(String operationType) {
      this.operationType = operationType;
   }

}
