package com.heyu.travel.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route implements Serializable {
    private Long id;

    private String routeName;

    private BigDecimal price;

    private String routeIntroduce;

    private String flag;

    private String isThemeTour;

    /** 
    * 关注数
    */
    private Integer attentionCount;

    private Long categoryId;

    private Long sellerId;

    private Date createdTime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}