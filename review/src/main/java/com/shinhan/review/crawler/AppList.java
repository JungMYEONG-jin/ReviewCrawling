package com.shinhan.review.crawler;

public enum AppList {
    스마트택스("smarttax"),
    비대면("noface"),
    포니("poney"),
    미래설계포유("senior"),
    미션플러스("missionplus"),
    신한모바일승인앱("safe2chssf"),
    스마트국민주택채권("ebond"),
    M_folio자산관리("mfolio"),
    SOL알리미("salimi"),
    SOLBiz("sbizbank"),
    SOL("sbank"),
    써니타임알람("sunnyalarm"),
    신한메일("smailid"),
    써니클럽("sunnyclub"),
    SOL베트남("shinhanvn"),
    써니계산기("sunnycalculator"),
    써니워치("sunnyswatch"),
    SOL중국("shinhancn"),
    신한S온라인등기("sregister"),
    써니뱅크("sunnybank"),
    SOL인도네시아("shinhanid"),
    SOL캐나다("shinhanca"),
    신한메일_베트남("smailvn"),
    신한S통장지갑("s_tongjang"),
    신한S부가세("ssurtax"),
    신한글로벌스마트뱅킹("shinhangi"),
    SOL캄보디아("shinhankh"),
    S뱅크미니("sbankmini"),
    SOL_Global("foreignerbank"),
    땡겨요("O2O");

    private String appPkg;

    AppList(String appPkg) {
        this.appPkg = appPkg;
    }

    public String getAppPkg(){
        return appPkg;
    }
}
