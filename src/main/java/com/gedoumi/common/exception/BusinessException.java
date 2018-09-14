package com.gedoumi.common.exception;

import com.gedoumi.common.enums.CodeEnum;

public class BusinessException extends Exception {

    private CodeEnum codeEnum;

    public BusinessException(CodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

    public CodeEnum getCodeEnum() {
        return codeEnum;
    }

    public void setCodeEnum(CodeEnum codeEnum) {
        this.codeEnum = codeEnum;
    }
}
