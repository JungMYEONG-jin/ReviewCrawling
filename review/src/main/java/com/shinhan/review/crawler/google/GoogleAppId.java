package com.shinhan.review.crawler.google;

public enum GoogleAppId {

    smarttax("com.shinhan.smarttaxpaper"),
    noface("com.shinhan.noface"),
    poney("com.shinhan.poney"),
    senior("com.shinhan.senior"),
    missionplus("com.shinhan.missionbanking"),
    safe2chssf("com.shinhan.approval"),
    ebond("com.ebond"),
    mfolio("com.shinhan.smartfund"),
    salimi("com.shinhan.smartcaremgr"),
    sbizbank("com.shinhan.sbizbank"),
    sbank("com.shinhan.sbanking"),
    sunnyalarm("com.shinhan.sunnyalarm"),
    smailid("com.shinhan.smaild"),
    sunnyclub("com.shinhan.global.vn.sclub"),
    shinhanvn("com.shinhan.global.vn.bank"),
    sunnycalculator("com.shinhan.sunnycalculator"),
    sunnyswatch("com.shinhan.swatchbank"),
    shinhancn("com.shinhan.global.cn.bank"),
    sregister("com.shinhan.register"),
    sunnybank("com.shinhan.speedup"),
    shinhanid("com.shinhan.global.id.bank"),
    shinhanca("com.shinhan.global.ca.bank"),
    smailvn("com.shinhan.smailvn"),
    s_tongjang("com.shinhan.mobilebankbook"),
    shinhansa("com.shinhan.global.ca.bank"),
    ssurtax("com.shinhan.trade.copper"),
    shinhangi("com.shinhan.global.gi.bank"),
    shinhankh("com.shinhan.global.kh.bank"),
    sbankmini("com.shinhan.sbankmini"),
    foreignerbank("com.shinhan.foreignerbank"),
    O2O("com.shinhan.o2o");

    private String appPkg;

    GoogleAppId(String appPkg) {
        this.appPkg = appPkg;
    }

    public String getAppPkg(){
        return appPkg;
    }

}
