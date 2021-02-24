package com.heyu.travel.req;

import lombok.NoArgsConstructor;

/**
 * @author heyu
 * @Description 基础请求
 * 分页、请求参数
 */
@NoArgsConstructor
public class BaseRequest<T> extends HeadRequest {

    /**
     * @Description 请求数据体
     */
    private T data;

    /**
     * @Description 页码
     */
    Integer pageNum;

    /**
     * @Description 分页大小
     */
    Integer pageSize;

    public BaseRequest(String moduleName, String operationType, T data) {
        super(moduleName, operationType);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
