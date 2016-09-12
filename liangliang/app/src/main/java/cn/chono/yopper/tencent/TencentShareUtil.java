package cn.chono.yopper.tencent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;


public class TencentShareUtil {
    //1103969444
    private static String app_test_key = "1104603577";

    public static Tencent tencent;

    private static boolean isServerSideLogin = false;

    /**
     * 初始化新浪api
     *
     * @param context
     */
    public static void initTencent(Context context) {
        try {
            // 如果是测试版使用测试版app_id
            //			 if (DEBUG == true)
            //			 APP_ID = APP_ID_TEST;
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            tencent = Tencent.createInstance(app_test_key, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareToQQ(final Activity activity, String title, String description, String url, String img_url, String app_name) {

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, img_url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, app_name);

        tencent.shareToQQ(activity, params, new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
//				Toast.makeText(activity, "onError: " + arg0.errorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
//				Toast.makeText(activity, "onComplete: " + arg0.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
//				Toast.makeText(activity, "onCancel: ", Toast.LENGTH_LONG).show();
            }
        });


    }

    public static void shareToQzone(final Activity activity, String title, String description, String url, ArrayList<String> img_url_list, String app_name) {

        final Bundle params = new Bundle();

        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, description);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, img_url_list);
        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, app_name);

        tencent.shareToQzone(activity, params, new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
                //Toast.makeText(activity, "onError: " + arg0.errorMessage, Toast.LENGTH_LONG).show();

//				DialogUtil.showTopToast(activity, "分享失败");
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                //Toast.makeText(activity, "onComplete: " + arg0.toString(), Toast.LENGTH_LONG).show();
//				DialogUtil.showTopToast(activity, "分享成功");
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                //Toast.makeText(activity, "onCancel: ", Toast.LENGTH_LONG).show();
//				DialogUtil.showTopToast(activity, "取消分享");
            }
        });


    }








    public static  void updateUserInfo(final Activity activity,IUiListener updateUserInfoUiListener) {
        if (tencent != null && tencent.isSessionValid()) {
            UserInfo  mInfo = new UserInfo(activity, tencent.getQQToken());
            mInfo.getUserInfo(updateUserInfoUiListener);

        }
    }


    public static void loginToQQ(final Activity activity, IUiListener iUiListener) {


        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", iUiListener);

        }

    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                tencent.setAccessToken(token, expires);
                tencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }


}