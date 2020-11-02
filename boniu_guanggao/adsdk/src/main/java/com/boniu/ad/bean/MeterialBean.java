package com.boniu.ad.bean;

import java.util.List;

public class MeterialBean {

    /**
     * appName : 童话故事社
     * countdown : 20
     * jumpUrl : www.baidu.com
     * lineMate : ["http://boniuearth.oss-cn-hangzhou.aliyuncs.com/c864d100eaa147579c318dd630eec42d.jpg"]
     * logoUrl : http://boniuearth.oss-cn-hangzhou.aliyuncs.com/c864d100eaa147579c318dd630eec42d.jpg
     * materialName : 测试素材1
     * openMode : 1
     * packageName : com.weidai.org
     * rowMate : ["http://boniuearth.oss-cn-hangzhou.aliyuncs.com/c864d100eaa147579c318dd630eec42d.jpg"]
     */

    private String appName;
    private int countdown;
    private String jumpUrl;
    private String logoUrl;
    private String materialName;
    private int openMode;
    private String packageName;
    private List<String> lineMate;
    private List<String> rowMate;
    private String vname = "";
    private String vdesc = "";

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVdesc() {
        return vdesc;
    }

    public void setVdesc(String vdesc) {
        this.vdesc = vdesc;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getOpenMode() {
        return openMode;
    }

    public void setOpenMode(int openMode) {
        this.openMode = openMode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getLineMate() {
        return lineMate;
    }

    public void setLineMate(List<String> lineMate) {
        this.lineMate = lineMate;
    }

    public List<String> getRowMate() {
        return rowMate;
    }

    public void setRowMate(List<String> rowMate) {
        this.rowMate = rowMate;
    }
}
