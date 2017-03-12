package com.example.dominic.alipaydemo;

/**
 * Created by Dominic on 2017/3/12.
 */

public class AliPayInfo {

    /**
     * payType : 1
     * errCode : 0
     * errMsg : 请求成功
     * payInfo :
     */

    private String payType;
    private String errCode;
    private String errMsg;
    private String payInfo;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    @Override
    public String toString() {
        return "AliPayInfo{" +
                "payType='" + payType + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", payInfo='" + payInfo + '\'' +
                '}';
    }
}
