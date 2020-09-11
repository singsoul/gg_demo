package com.boniu.ad.bean;

public class ResultBean {

    /**
     * result : yHy0WAgKPJtJwN5YpQDqTQ==
     * returnCode : 0
     * success : true
     */

    public String result;
    public String returnCode;
    public boolean success;
    public String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
