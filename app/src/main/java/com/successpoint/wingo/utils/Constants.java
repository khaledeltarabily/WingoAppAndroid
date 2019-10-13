package com.successpoint.wingo.utils;

public class Constants {
    public static final String api_token = "smWt7sOEn2F5Uc2BZMD7gv3DYeAAHGQL";
    private static final String Domain = "http://wingo-live.com/api";

    public static final String Login = Domain.concat("/user/login");
    public static final String SendVerification = Domain.concat("/user/verification_code");
    public static final String SendCode = Domain.concat("/user/verify");
    public static final String TopTrending = Domain.concat("/user/top_trending");
    public static final String TodayTopFans = Domain.concat("/user/contributions/day");
    public static final String officialaccounts = Domain.concat("/officialaccounts");

    public static final String FacebookLogin = Domain.concat("/user/login/facebook");
    public static final String AccountUpdate = Domain.concat("/user/update");
    public static final String AccountImage = Domain.concat("/user/image");
    public static final String StreamLiveCover = Domain.concat("/stream/cover");
    public static final String Popular = Domain.concat("/stream/live");
    public static final String Nearby = Domain.concat("/stream/nearby");
    public static final String Follow = Domain.concat("/user/follow");
    public static final String Following = Domain.concat("/user/following");
    public static final String MakeViewLiveAction = Domain.concat("/stream/view");
    public static final String MakeCloseLiveAction = Domain.concat("/stream/leave");
    public static final String Search = Domain.concat("/user/search");
    public static final String ActivitiesUrl(String type) {
        return Domain.concat("/user/"+type);
    }
    public static final String LeaderBoard = Domain.concat("/user/leaderboard/");
    public static final String UserFans = Domain.concat("/user/fans");
    public static final String UserProfileFromToken = Domain.concat("/user/profile");
    public static final String UserProfile = Domain.concat("/user");
    public static final String AllGifts = Domain.concat("/gifts");
    public static final String SendGift = Domain.concat("/stream/send_gift");
    public static final String DoneFlyComment = Domain.concat("/stream/bullet");
    public static final String LevelData = Domain.concat("/user/level");
    public static final String SettingsData = Domain.concat("/user/settings/");


    public static final String FaceBookRegister = Domain.concat("/user/register/facebook");
    public static final String RegisterInstgram = Domain.concat("/user/register/instgram");
    public static final String RegisterTwitter = Domain.concat("/user/register/twitter");
    public static final String RegisterGoogle = Domain.concat("/user/register/google");
    public static final String RegisterPhone = Domain.concat("/user/register/phone");
    public static final String StreamLiveStart = Domain.concat("/stream/start");

    public static final String PhoneLogin = Domain.concat("/user/login/phone");
    public static final String InstgramLogin = Domain.concat("/user/login/instgram");
    public static final String TwitterLogin = Domain.concat("/user/login/twitter");
    public static final String GoogleLogin = Domain.concat("/user/login/google");
    public static final String PhoneTwitter = Domain.concat("/user/login/phone");
    public static final String CheckPhone = Domain.concat("/user/check_phone");
    public static final String Contributions = Domain.concat("/user/top_contributors");
    public static final String BuyData = Domain.concat("/user/vip/buy");
    public static final String RenewData = Domain.concat("/user/vip/renew");

    public static final String AuthorizedSocial(String type) {
        return  Domain.concat("user/social/"+type+"/add");
    }


}
