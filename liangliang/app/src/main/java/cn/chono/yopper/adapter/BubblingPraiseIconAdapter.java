package cn.chono.yopper.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BubblingPraiseDto;
import cn.chono.yopper.data.BubblingPraiseDto.User;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public class BubblingPraiseIconAdapter extends BaseAdapter {

	public interface OnItemClickLitener {

		/**
		 * 内容图片点击监听
		 * 
		 * @param view
		 * @param position
		 * @param iconUrl
		 */
		void onPraiseIconItemClick(View view, int position, int userID);

	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;

	}

	private List<BubblingPraiseDto.BubblingPraise> list;

	private Context context;

	private BitmapPool mPool;
	private CropCircleTransformation transformation;

	public BubblingPraiseIconAdapter(Context context) {
		this.context = context;
		mPool = Glide.get(context).getBitmapPool();
		transformation = new CropCircleTransformation(mPool);
	}

	private class NewViewHolder {
		/**
		 * 图片
		 */
		private ImageView contentIcon;

	}

	public void setData(List<BubblingPraiseDto.BubblingPraise> list) {
		this.list = list;
	}

	public int getDataSize() {
		return list.size();
	}

	@Override
	public int getCount() {
		int size = 0;
		if (list == null || list.size() <= 0) {
			size = 0;
		} else if (list.size() <= 6) {
			size = list.size();

		} else {
			size = 6;
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	NewViewHolder newViewHolder;

	@SuppressWarnings("unchecked")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			newViewHolder = new NewViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bubbling_praise_icon_layout, null);

			newViewHolder.contentIcon = (ImageView) convertView
					.findViewById(R.id.contentIcon);

			int weight = UnitUtil.getScreenWidthPixels(context);

			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) newViewHolder.contentIcon
					.getLayoutParams();
			int range = (weight - (weight / 3)) / 6;
			params.width = range;
			params.height = range;
			newViewHolder.contentIcon.setLayoutParams(params);

			convertView.setTag(newViewHolder);

		} else {
			newViewHolder = (NewViewHolder) convertView.getTag();
		}
		final User user = list.get(position).getUser();
		if (user != null) {
			String uresUrl = user.getHeadImg();
			String uresIcoUrl = ImgUtils.DealImageUrl(uresUrl,
					YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
			Glide.with(context).load(uresIcoUrl)
					.error(R.drawable.error_user_icon)
					.bitmapTransform(transformation)
					.into(newViewHolder.contentIcon);
		}

		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {

			newViewHolder.contentIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickLitener.onPraiseIconItemClick(
							newViewHolder.contentIcon, position, user.getId());
				}
			});
		}
		return convertView;
	}

}
