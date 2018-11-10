package net.payviet;

import me.takahatashun.payviet.Configurations.YAML;

public enum PayVietStatusResponse {
    SR_199(199),
    SR_200(200),
    SR_1000(1000),
    SR_1001(1001),
    SR_1002(1002),
    SR_1999(1999),
    SR_2000(2000),
    SR_2001(2001),
    SR_2002(2002),
    SR_2003(2003),
    SR_2004(2004),
    SR_2005(2005),
    SR_2006(2006),
    SR_2007(2007),
    SR_2008(2008),
    SR_2009(2009),
    SR_2010(2010),
    SR_2011(2011),
    SR_2012(2012),
    SR_2013(2013),
    SR_2014(2014),
    SR_2015(2015),
    SR_2016(2016),
    SR_2017(2017),
    SR_2018(2018),
    SR_2019(2019),
    SR_2020(2020),
    SR_2021(2021),
    SR_2022(2022),
    SR_2023(2023),
    SR_2024(2024),
    SR_2025(2025),
    SR_4002(4002),
    SR_4010(4010),
    SR_4011(4011),
    SR_4012(4012),
    SR_4013(4013),
    SR_4014(4014),
    SR_4015(4015),
    SR_4016(4016),
    SR_4017(4017),
    SR_4018(4018),
    SR_4019(4019),
    SR_4020(4020),
    SR_4021(4021),
    SR_4022(4022),
    SR_4023(4023),
    SR_4024(4024),
    SR_4098(4098),
    SR_4099(4099),
    SR_4100(4100),
    SR_4101(4101),
    SR_4102(4102),
    SR_4103(4103),
    SR_4104(4104),
    SR_4105(4105),
    SR_4106(4106),
    SR_4107(4107),
    SR_4108(4108),
    SR_4126(4126),
    SR_4201(4201),
    SR_4202(4202),
    SR_4203(4203),
    SR_4209(4209),
    SR_4210(4210),
    SR_4211(4211),
    SR_4212(4212),
    SR_4213(4213),
    SR_4214(4214),
    SR_4299(4299),
    SR_4501(4501),
    SR_4502(4502),
    SR_4503(4503),
    SR_4504(4504),
    SR_10000(10000);

    private int code;

    PayVietStatusResponse(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return YAML.getMessage().getString("PayViet.StatusResponse."+String.valueOf(this.getCode()));
    }
    public boolean isConnected(){
        switch (this){
            case SR_200:
            case SR_1000:
            case SR_10000:
                return true;
            default:return false;
        }
    }
    public boolean isSuccessful(){
        switch (this){
            case SR_4020:
            case SR_4021:
            case SR_4023:
            case SR_4024:
            case SR_4213:
                return false;
            case SR_10000:
            case SR_4002:
                return true;
            default:return false;
        }
    }
    public static PayVietStatusResponse getStatusResponse(int code){
        PayVietStatusResponse result = null;
        for (PayVietStatusResponse sr: PayVietStatusResponse.values()){
            if(code == sr.getCode()){
                result = sr;
            }
        }
        return result;
    }
    public static PayVietStatusResponse getStatusResponse(String input){
        int code = Integer.parseInt(input);
        PayVietStatusResponse output = null;
        for (PayVietStatusResponse sr: PayVietStatusResponse.values()){
            if(code == sr.getCode()){
                output = sr;
            }
        }
        return output;
    }

}
