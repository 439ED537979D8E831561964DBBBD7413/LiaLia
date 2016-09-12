package cn.chono.yopper.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.Banners.BannersData;
import cn.chono.yopper.Service.Http.Banners.BannersDto;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.JsonUtils;

/**
 * Created by sunquan on 16/2/29.
 */
public class BannerOperatePopupwindow extends PopupWindow implements View.OnClickListener {

    private OnSelectedListener onSelectedListener;

    private LinearLayout banner_operate_off_iv_layout;
    private TextView banner_operate_finish_tv;
    private TextView banner_operate_detele_tv;

    private TextView banner_add_more_tv;

    private FlowCenterLeftLayout select_lable_flowlayout;
    private FlowCenterLeftLayout no_select_lable_flowlayout;

    private Context mContext;

    private List<BannersData> usernoSelectList = new ArrayList<BannersData>();

    private List<BannersData> userSelectList = new ArrayList<BannersData>();

    private int selectPosition;

    private LayoutInflater inflate;

    private ViewGroup.MarginLayoutParams lp;
    private List<BannersData> bannerlistFormHttp=new ArrayList<BannersData>();

    private String horStr;

    private String bannerId;

    private BannersData selectBannersData;


    public BannerOperatePopupwindow(Context context, String bannerid, String hor,int selectposition, List<BannersData> userList, List<BannersData> noSelectList, List<BannersData> allList,OnSelectedListener listener) {
        super(context, null);

        mContext = context;
        inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        lp = new ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 0;
        lp.bottomMargin = 0;

        horStr=hor;
        bannerId=bannerid;

        selectPosition = selectposition;
        selectBannersData=userList.get(selectPosition);

        onSelectedListener = listener;
        View view = LayoutInflater.from(context).inflate(R.layout.banner_operate_layout, null);

        banner_operate_off_iv_layout = (LinearLayout) view.findViewById(R.id.banner_operate_off_iv_layout);
        banner_operate_off_iv_layout.setOnClickListener(this);

        banner_operate_finish_tv = (TextView) view.findViewById(R.id.banner_operate_finish_tv);
        banner_operate_finish_tv.setOnClickListener(this);

        banner_operate_detele_tv = (TextView) view.findViewById(R.id.banner_operate_detele_tv);
        banner_operate_detele_tv.setOnClickListener(this);

        banner_add_more_tv = (TextView) view.findViewById(R.id.banner_add_more_tv);


        select_lable_flowlayout = (FlowCenterLeftLayout) view.findViewById(R.id.select_lable_flowlayout);

        no_select_lable_flowlayout = (FlowCenterLeftLayout) view.findViewById(R.id.no_select_lable_flowlayout);

        userSelectList = userList;
        usernoSelectList = noSelectList;
        bannerlistFormHttp=allList;

        if (userSelectList != null && userSelectList.size() > 0) {

            select_lable_flowlayout.removeAllViews();

            for (int i = 0; i < userSelectList.size(); i++) {
                initLableViews(i, selectPosition, userSelectList.get(i),false);
            }
        }

        initNoSelectLableViews(noSelectList);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.color_ffffff)));

        setOutsideTouchable(true);
        setFocusable(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.banner_operate_off_iv_layout:
                onSelectedListener.onSelectedListener(id, userSelectList);
                break;

            case R.id.banner_operate_finish_tv:
                lableOperateFinish();
                onSelectedListener.onSelectedListener(id, userSelectList);
                break;

            case R.id.banner_operate_detele_tv:
                lableDelete();

                break;

        }
    }


    public interface OnSelectedListener {
        public void onSelectedListener(int vid, List<BannersData> userList);
    }


    private void lableDelete() {

        banner_operate_finish_tv.setVisibility(View.VISIBLE);
        banner_operate_detele_tv.setVisibility(View.GONE);
        banner_add_more_tv.setVisibility(View.GONE);
        no_select_lable_flowlayout.setVisibility(View.GONE);

        if (userSelectList != null && userSelectList.size() > 0) {

            select_lable_flowlayout.removeAllViews();

            for (int i = 0; i < userSelectList.size(); i++) {
                initLableViews(i, selectPosition, userSelectList.get(i),true);
            }
        }

    }

    private void lableOperateFinish() {

        banner_operate_finish_tv.setVisibility(View.GONE);
        banner_operate_detele_tv.setVisibility(View.VISIBLE);

        int w=userSelectList.indexOf(selectBannersData);
        LogUtils.e("w="+w);
        if(w<0||w>=userSelectList.size()){
            selectPosition=0;
        }else{
            selectPosition=w;
        }
        selectBannersData=userSelectList.get(selectPosition);

        if (userSelectList != null && userSelectList.size() > 0) {

            select_lable_flowlayout.removeAllViews();

            for (int i = 0; i < userSelectList.size(); i++) {
                initLableViews(i, selectPosition, userSelectList.get(i),false);
            }
        }


        usernoSelectList.clear();

        if(bannerlistFormHttp.size()==0){
            String bannerStr=DbHelperUtils.getBannersFromDb(bannerId);
            if(!CheckUtil.isEmpty(bannerStr)){
                BannersDto bannersDto=JsonUtils.fromJson(bannerStr,BannersDto.class);
                bannerlistFormHttp=bannersDto.getList();
            }

        }

        usernoSelectList=bannerlistFormHttp;
        LogUtils.e("原始usernoSelectList="+usernoSelectList.size());
        LogUtils.e("usernoSelectList="+userSelectList.size());


        List<BannersData> postionlist=new ArrayList<BannersData>();

        for(int i=0;i<usernoSelectList.size();i++){
            BannersData dto=usernoSelectList.get(i);

            for(int j=0;j<userSelectList.size();j++){

                if(dto.getName().equals(userSelectList.get(j).getName())){
                    LogUtils.e("i="+i+userSelectList.get(j).getName());
                    postionlist.add(dto);
                }
            }
        }

        usernoSelectList.removeAll(postionlist);


        LogUtils.e("usernoSelectList="+usernoSelectList.size());
        initNoSelectLableViews(usernoSelectList);
    }


    private void initLableViews(int positon, int selectPosition, final  BannersData dto, boolean isdelete) {



        View layoutView = inflate.inflate(R.layout.banner_detele_view, null);
        TextView name_tv = (TextView) layoutView.findViewById(R.id.banner_name_tv);

        RelativeLayout banner_detele_layout = (RelativeLayout) layoutView.findViewById(R.id.banner_detele_layout);


        banner_detele_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int positon=getpositonFromUserList(dto);
                select_lable_flowlayout.removeViewAt(positon);
                userSelectList.remove(positon);


            }
        });

        name_tv.setText(dto.getName());

        if (isdelete) {
            if(dto.getName().equals("全部") || dto.getName().equals(horStr)){

                banner_detele_layout.setVisibility(View.GONE);
            }else{
                banner_detele_layout.setVisibility(View.VISIBLE);
            }

        } else {
            banner_detele_layout.setVisibility(View.GONE);
        }




        if (selectPosition == positon) {
            name_tv.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));
            name_tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.banner_lable_select_bg));
        } else {
            name_tv.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            name_tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.banner_lable_bg));
        }

        select_lable_flowlayout.addView(layoutView,lp);


    }


    private int getpositonFromUserList(BannersData dto){
        int positon=0;
        for(int i=0;i<userSelectList.size();i++){
            BannersData bannersData=userSelectList.get(i);
            if(bannersData.getBannerId().equals(dto.getBannerId())){
                positon=i;
            }
        }
        return positon;
    }

    private int getpositonFromUserNoSelectList(BannersData dto){
        int positon=0;
        for(int i=0;i<usernoSelectList.size();i++){
            BannersData bannersData=usernoSelectList.get(i);
            if(bannersData.getBannerId().equals(dto.getBannerId())){
                positon=i;
            }
        }
        return positon;
    }


    private void initNoSelectLableViews(List<BannersData> userList) {
        no_select_lable_flowlayout.removeAllViews();

        if (userList != null && userList.size() > 0) {

            no_select_lable_flowlayout.setVisibility(View.VISIBLE);
            banner_add_more_tv.setVisibility(View.VISIBLE);




            if (userList != null && userList.size() > 0) {

                for (int i = 0; i < userList.size(); i++) {

                    View layoutView = inflate.inflate(R.layout.banner_detele_view, null);
                    TextView name_tv = (TextView) layoutView.findViewById(R.id.banner_name_tv);

                    RelativeLayout banner_detele_layout = (RelativeLayout) layoutView.findViewById(R.id.banner_detele_layout);

                    banner_detele_layout.setVisibility(View.GONE);

                    name_tv.setText(userList.get(i).getName());
                    name_tv.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                    name_tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.banner_lable_bg));

                    name_tv.setTag(new BannerView(false, userList.get(i)));
                    name_tv.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            BannerView bannerView = (BannerView) v.getTag();

                            int position= getpositonFromUserNoSelectList(bannerView.getDto());
                            LogUtils.e("移除的位置＝"+position);

                            no_select_lable_flowlayout.removeViewAt(position);


                            initLableViews(usernoSelectList.size() + 1, selectPosition, bannerView.getDto(),false);

                            usernoSelectList.remove(bannerView.getDto());

                            userSelectList.add(bannerView.getDto());

                            if (usernoSelectList.size() == 0) {
                                no_select_lable_flowlayout.setVisibility(View.GONE);
                                banner_add_more_tv.setVisibility(View.GONE);
                            }
                            LogUtils.e("usernoSelectList＝"+usernoSelectList.size());


                        }
                    });

                    no_select_lable_flowlayout.addView(layoutView,lp);
                }
            }
        } else {
            no_select_lable_flowlayout.setVisibility(View.GONE);
            banner_add_more_tv.setVisibility(View.GONE);
        }
    }

    class BannerView {

        private boolean isSelect;
        private BannersData dto;

        public BannerView( boolean isSelect, BannersData dto) {

            this.isSelect = isSelect;
            this.dto = dto;
        }


        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public BannersData getDto() {
            return dto;
        }

        public void setDto(BannersData dto) {
            this.dto = dto;
        }
    }


}