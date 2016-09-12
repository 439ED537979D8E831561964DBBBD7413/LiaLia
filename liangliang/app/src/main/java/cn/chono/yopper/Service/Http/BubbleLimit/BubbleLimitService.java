package cn.chono.yopper.Service.Http.BubbleLimit;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 检查冒泡提交限制
 * Created by zxb on 2015/11/20.
 */
public class BubbleLimitService extends HttpService{
    public BubbleLimitService(Context context) {
        super(context);
    }

    private ParameterBean iBean;
    @Override
    public void enqueue() {
        OutDataClass=BubbleLimitRespBean.class;
        callWrap= OKHttpUtils.get(context, HttpURL.v2_bubble_limit,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean=iBean;
    }
}