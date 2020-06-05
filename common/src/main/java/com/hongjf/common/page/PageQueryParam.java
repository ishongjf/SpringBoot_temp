package com.hongjf.common.page;

/**
 *
 *
 * @Author: Hongjf
 * @Date: 2019/8/24
 * @Time: 11:01
 * @Description:
 */

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PageQueryParam {

    /**
     * 目标页
     **/
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = 1;

    /**
     * 每页条数
     **/
    @Min(value = 1, message = "条数最少1条")
    private Integer pageSize = 1;
}
