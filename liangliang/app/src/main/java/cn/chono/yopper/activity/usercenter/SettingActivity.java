package cn.chono.yopper.activity.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import java.io.File;
import java.util.ArrayList;
import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UserLogout.LogoutBean;
import cn.chono.yopper.Service.Http.UserLogout.LogoutEntity;
import cn.chono.yopper.Service.Http.UserLogout.LogoutRespEntity;
import cn.chono.yopper.Service.Http.UserLogout.LogoutService;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.base.SelectEntryActivity;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.im.imEvent.MessageEvent;
import cn.chono.yopper.im.imbusiness.TlsBusiness;
import cn.chono.yopper.tencent.TencentShareUtil;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.AppUtil;
import cn.chono.yopper.utils.BackCall;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.SwitchButton;
import cn.chono.yopper.weibo.SinaWeiBoUtil;
import cn.chono.yopper.wxapi.WeixinUtils;

/**
 * 用户设置
 *
 * @author sam.sun
 */
public class SettingActivity extends MainFrameActivity {

    private LayoutInflater mInflater;
    private View contextView;

    private SwitchButton message_voice_myslipswitch;

    private RelativeLayout setting_password_layout_ActivitySetting;

    private RelativeLayout clear_cache_layout_ActivitySetting;// 清理缓存

    private RelativeLayout block_layout_ActivitySetting;

    private RelativeLayout feedback_ActivitySetting;


    private RelativeLayout share_layout_ActivitySetting;// 分享

    private TextView quit_account_btn_ActivitySetting;

    private TextView setting_version_tv;

    private boolean isOpean_message_Voice;

    private Dialog cachedialog;

    private Dialog quitdialog, mQuitModifyDlg;

    private String imageUrl = "http://yuepengwww.oss-cn-hangzhou.aliyuncs.com/images/share/152X152.jpg";

    private String qqLink = "http://www.yopper.cn/mobilesite/share?uid=" + LoginUser.getInstance().getUserId() + "&target=qq";

    private String qqzoneLink = "http://www.yopper.cn/mobilesite/share?uid=" + LoginUser.getInstance().getUserId() + "&target=qzone";

    private String weixinLink = "http://www.yopper.cn/mobilesite/share?uid=" + LoginUser.getInstance().getUserId() + "&target=wechat";

    private String wechat_momentsLink = "http://www.yopper.cn/mobilesite/share?uid=" + LoginUser.getInstance().getUserId() + "&target=wechat_moments";

    private String sinaLink = "http://www.yopper.cn/mobilesite/share?uid=" + LoginUser.getInstance().getUserId() + "&target=weibo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initComponent();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("设置"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("设置");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.INVISIBLE);

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.setting_activity, null);

        message_voice_myslipswitch = (SwitchButton) contextView.findViewById(R.id.message_voice_myslipswitch);

        setting_password_layout_ActivitySetting = (RelativeLayout) contextView.findViewById(R.id.setting_password_layout_ActivitySetting);

        clear_cache_layout_ActivitySetting = (RelativeLayout) contextView.findViewById(R.id.clear_cache_layout_ActivitySetting);

        block_layout_ActivitySetting = (RelativeLayout) contextView.findViewById(R.id.block_layout_ActivitySetting);

        feedback_ActivitySetting = (RelativeLayout) contextView.findViewById(R.id.feedback_ActivitySetting);

        share_layout_ActivitySetting = (RelativeLayout) contextView.findViewById(R.id.share_layout_ActivitySetting);

        quit_account_btn_ActivitySetting = (TextView) contextView.findViewById(R.id.quit_account_btn_ActivitySetting);

        setting_version_tv = (TextView) contextView.findViewById(R.id.setting_version_tv);


        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            setting_version_tv.setText("Version" + version);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        // 根据文件存储中值设定是否开启消息提醒
        isOpean_message_Voice = SharedprefUtil.getBoolean(SettingActivity.this, YpSettings.IS_OPEAN_MESSAGE_VOICE, true);


        message_voice_myslipswitch.setChecked(isOpean_message_Voice);

        message_voice_myslipswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedprefUtil.saveBoolean(SettingActivity.this, YpSettings.IS_OPEAN_MESSAGE_VOICE, isChecked);
            }
        });


        //设置登录密码：已绑定了手机号直接跳转到设置密码，未绑定则先跳转到输入手机号页面
        setting_password_layout_ActivitySetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordEvent();

            }
        });

        // 清理缓存
        clear_cache_layout_ActivitySetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 1000);
                // 清理图片缓存提示框

                cachedialog = DialogUtil.createHintOperateDialog(SettingActivity.this, "清理缓存", "确定清理缓存吗?", "取消", "确认", cachebackCallListener);
                if (!isFinishing()) {
                    cachedialog.show();
                }

            }
        });

        // 分享
        share_layout_ActivitySetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 1000);
                // 分享
                Dialog dialog = DialogUtil.createShareDialog(SettingActivity.this, new DialogBackCall());
                if (!isFinishing()) {
                    dialog.show();
                }
            }
        });

        // 退出账号
        quit_account_btn_ActivitySetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 1000);
                // 退出账号操作
                quitdialog = DialogUtil.createHintOperateDialog(SettingActivity.this, "退出登录", "退出登录后将不再收到消息,是否继续?", "取消", "确认", quitbackCallListener);
                quitdialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                if (!isFinishing()) {
                    quitdialog.show();
                }
            }
        });

        block_layout_ActivitySetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 1000);
                Bundle bundle = new Bundle();
                ActivityUtil.jump(SettingActivity.this, BlockActivity.class, bundle, 0, 100);
            }
        });

        feedback_ActivitySetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 1000);
                Bundle bundle = new Bundle();
                ActivityUtil.jump(SettingActivity.this, FeedBackActivity.class, bundle, 0, 100);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("设置"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    private BackCallListener cachebackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            clearCacheFolder(SettingActivity.this.getCacheDir(), System.currentTimeMillis());
            clearCacheFolder(App.getInstance().cacheDir, System.currentTimeMillis());

            clearCacheFolder(new File(Environment.getExternalStorageDirectory() + File.separator + "video" + File.separator + "myvideo"), System.currentTimeMillis());

            // 删除数据库聊天记录以及其他数据库缓存数据
            // try {
            // App.getInstance().db.dropTable(ChatDto.class);
            // App.getInstance().db.dropTable(ChatDto.class);
            // } catch (DbException e) {
            // e.printStackTrace();
            // }
            if (!isFinishing()) {
                cachedialog.dismiss();
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                cachedialog.dismiss();
            }

        }
    };

    private BackCallListener quitbackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {

            if (!isFinishing()) {
                quitdialog.dismiss();
            }

            getUserLogout(false);

        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                quitdialog.dismiss();
            }

        }
    };

    private BackCallListener quitModifybackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {


            if (!isFinishing()) {
                mQuitModifyDlg.dismiss();
            }
            setPasswordEvent();


        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                mQuitModifyDlg.dismiss();
            }


            getUserLogout(true);

        }
    };


    private void userLogoutEvent(){

        if(YpSettings.isTest){
            TlsBusiness.logout(LoginUser.getInstance().getUserId()+"@test");
        }else{
            TlsBusiness.logout(LoginUser.getInstance().getUserId()+"");
        }


        MessageEvent.getInstance().clear();

        AppUtil.setRefreshTokenExpiration(this,0);
        LoginUser.getInstance().reSetLoginUser();

        QuitRemoveAliasAsyncTask loginAsyncTask = new QuitRemoveAliasAsyncTask();
        loginAsyncTask.execute("SettingActivity");

        Bundle bundle = new Bundle();
        //需要清理task 所以传入flagtype=2
        ActivityUtil.jump(SettingActivity.this, SelectEntryActivity.class, bundle, 2, 0);
    }



    private void getUserLogout(boolean force){

        LogoutBean logoutBean=new LogoutBean();

        logoutBean.force=force;

        LogoutService logoutService=new LogoutService(this);

        logoutService.parameter(logoutBean);

        logoutService.callBack(new OnCallBackSuccessListener(){
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                LogoutRespEntity logoutRespEntity= (LogoutRespEntity) respBean;

                LogoutEntity logoutEntity=logoutRespEntity.resp;

                if (logoutEntity !=null && TextUtils.equals("NoPassword",logoutEntity.code)){

                    mQuitModifyDlg = DialogUtil.createHintOperateDialog(SettingActivity.this, "退出登录", "为保证账号安全，请设置登录密码后再退出登录", "仍然退出", "设置密码", quitModifybackCallListener);
                    mQuitModifyDlg.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                    if (!isFinishing()) {
                        mQuitModifyDlg.show();
                    }

                    return;

                }


                userLogoutEvent();



            }
        });

        logoutService.enqueue();
    }

    /**
     * 设置密码
     */
    private void setPasswordEvent() {
        UserDto dto = DbHelperUtils.getDbUserInfo(LoginUser.getInstance().getUserId());

        if (((dto.getProfile().getStatus() >> 2) & 1) == 1) {
            //去设置密码页面
            Bundle buns = new Bundle();
            buns.putInt(YpSettings.FROM_PAGE, 1);
            ActivityUtil.jump(SettingActivity.this, SettingPasswordActivity.class, buns, 0, 100);
        } else {
            Bundle buns = new Bundle();
            buns.putInt(YpSettings.FROM_PAGE, 3);
            ActivityUtil.jump(SettingActivity.this, SettingPhoneActivity.class, buns, 0, 100);
        }


    }


    /**
     * 筛选 dialog按钮监听事件
     *
     * @ClassName: DialogBackCall
     * @author: xianbin.zou
     * @date: 2015年4月6日 下午6:31:26
     */
    private class DialogBackCall extends BackCall {
        @Override
        public void deal(int which, Object... obj) {

            switch (which) {
                case R.id.setting_share_to_sina_weibo:
                    SinaWeiBoUtil.sendMessage(SettingActivity.this,true, false, "真人视频认证，分享真实生活。", "分享动态，记录生活，俩俩相遇，一拍即合。", imageUrl, sinaLink);

                    break;

                case R.id.setting_share_to_qq:
                    TencentShareUtil.shareToQQ(SettingActivity.this, "「俩俩」相望，拉近「你我」", "遇见你的心动，开启俩人世界。", qqLink, imageUrl, "俩俩");
                    break;

                case R.id.setting_share_to_weixin_friend:
                    // 微信朋友圈分享

                    if (WeixinUtils.isWeixinAvailable()) {

                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.share_weixin_image);

                        WeixinUtils.sendFriendTextAndPicture("超人气「视频认证」交友平台，搜美女、搜帅哥、搜同城、搜附近...", "超人气「视频认证」交友平台，搜美女、搜帅哥、搜同城、搜附近...", wechat_momentsLink, bmp);

                    } else {
                        DialogUtil.showTopToast(SettingActivity.this, "您的手机没有安装微信!");
                    }
                    break;

                case R.id.setting_share_to_weixin:

                    if (WeixinUtils.isWeixinAvailable()) {

                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.share_weixin_image);
                        WeixinUtils.sendTextAndPicture("开启俩俩「主题约会」，告别「单身汪」模式", "一个人的世界太无聊？俩俩带你找到更多一拍即合之人！", weixinLink, bmp);

                    } else {
                        DialogUtil.showTopToast(SettingActivity.this, "您的手机没有安装微信!");
                    }

                    break;

                case R.id.setting_share_to_qq_zone:

                    // http://cdn.duitang.com/uploads/item/201209/03/20120903120924_TSsvE.jpeg
                    ArrayList<String> img_url_list = new ArrayList<String>();
                    img_url_list.add(imageUrl);
                    TencentShareUtil.shareToQzone(SettingActivity.this, "超人气火爆陌生人交友app", "搜美女、搜帅哥、搜同城、搜附近...", qqzoneLink, img_url_list, "俩俩");

                    break;

                default:
                    break;
            }

            super.deal(which, obj);
        }

    }


    private int clearCacheFolder(File dir, long numDays) {

        int deletedFiles = 0;

        if (dir != null && dir.isDirectory()) {

            try {

                for (File child : dir.listFiles()) {

                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }

                    if (child.lastModified() < numDays) {

                        if (child.delete()) {

                            deletedFiles++;

                        }

                    }
                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }
        return deletedFiles;
    }


    public class QuitRemoveAliasAsyncTask extends AsyncTask<String, Integer, String> {

        // 该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
        // 主要用于进行异步操作
        @Override
        protected String doInBackground(String... params) {


            try {
                PushAgent mPushAgent = PushAgent.getInstance(SettingActivity.this);
                mPushAgent.removeAlias(LoginUser.getInstance().getUserId() + "", "chono");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return params[0];

        }

        // 该方法运行在Ui线程内，可以对UI线程内的控件设置和修改其属性
        @Override
        protected void onPreExecute() {
        }


        // 在doInBackground方法执行结束后再运行，并且运行在UI线程当中
        // 主要用于将异步操作任务执行的结果展示给用户
        @Override
        protected void onPostExecute(String result) {

        }

    }


}