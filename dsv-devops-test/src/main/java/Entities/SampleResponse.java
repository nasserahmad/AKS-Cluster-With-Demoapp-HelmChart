package Entities;


import java.util.Date;

public class SampleResponse {
    private String msg;
    private int responseCode;
    private Date responseTime;
    private String URL;
    private String method;
    private String orgType;

    public SampleResponse(String msg, int responseCode, Date responseTime, String URL, String method,String orgType) {
        this.msg = msg;
        this.responseCode = responseCode;
        this.responseTime = responseTime;
        this.URL = URL;
        this.method = method;
        this.orgType=orgType;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public SampleResponse() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }


    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SampleResponse{" +
                "msg='" + msg + '\'' +
                ", responseCode=" + responseCode +
                ", responseTime=" + responseTime +
                ", URL='" + URL + '\'' +
                ", method='" + method + '\'' +
                ", orgType='" + orgType + '\'' +
                '}';
    }
}
