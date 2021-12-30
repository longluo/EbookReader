# 友盟相关 SDK
# https://developer.umeng.com/docs/193624/detail/194590

-keep class com.umeng.** {*;}

-keep class com.uc.** { *; }

-keep class com.efs.** { *; }

-keepclassmembers class *{
     public<init>(org.json.JSONObject);
}
-keepclassmembers enum *{
      publicstatic**[] values();
      publicstatic** valueOf(java.lang.String);
}

# QQ 和 微信 SDK
# https://developers.weixin.qq.com/doc/oplatform/Mobile_App/Access_Guide/Android.html
-keep class com.tencent.** {*;}

