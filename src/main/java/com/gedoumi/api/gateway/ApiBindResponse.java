package com.gedoumi.api.gateway;

import com.gedoumi.api.gateway.vo.BindVO;
import com.gedoumi.api.gateway.vo.QueryVO;
import com.gedoumi.common.Constants;

public class ApiBindResponse extends ApiResponse{

    private QueryVO data;

    @Override
    public QueryVO getData() {
        return data;
    }

    public void setData(QueryVO data) {
        this.data = data;
    }
}
