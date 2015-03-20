package org.yuner.www.mainBody;

import java.util.HashMap;
import java.util.List;

import org.yuner.www.R;
import org.yuner.www.bean.FrdReqNotifItemEntity;
import org.yuner.www.commons.GlobalInts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FrdRequestNotifAdapter extends BaseAdapter{	
	
	private List<FrdReqNotifItemEntity> mVector;
	private LayoutInflater mInflater;
	private Context mContext0;
	private int mSelfId;
	private HashMap<Integer, Integer> mIdForPos;
	
	public FrdRequestNotifAdapter(Context context,List<FrdReqNotifItemEntity> vector){
		this.mVector = vector;
		mInflater = LayoutInflater.from(context);
		mContext0=context;
		mSelfId = GlobalInts.idFrdReqNotifItemSt;
		mIdForPos = new HashMap<Integer, Integer>();
	}

	public View getView(int position, View convertView, ViewGroup root) {
		RelativeLayout layoutToClick;
		ImageView avatarV;
		TextView nameOfSpeakerV;
		TextView textV;
		TextView timeV;
		TextView statusV;
		
		String name = mVector.get(position).getName();
		int avatarId = mVector.get(position).getImgId();
		String sentence = mVector.get(position).getContent();
		String time = mVector.get(position).getTime();
		int status = mVector.get(position).getStatus();
		
		convertView = mInflater.inflate(R.layout.tabmsg_frd_req_notif_item, null);
		layoutToClick = (RelativeLayout)convertView.findViewById(R.id.tabmsg_frd_req_notif_item_main_layout);
		avatarV = (ImageView)convertView.findViewById(R.id.tabmsg_frd_req_notif_item_icon);
		nameOfSpeakerV = (TextView)convertView.findViewById(R.id.tabmsg_frd_req_notif_item_name);
		textV = (TextView)convertView.findViewById(R.id.tabmsg_frd_req_notif_item_content);
		timeV = (TextView)convertView.findViewById(R.id.tabmsg_frd_req_notif_item_time);
		statusV = (TextView)convertView.findViewById(R.id.tabmsg_frd_req_notif_item_status);
		
		layoutToClick.setId(mSelfId);
		mIdForPos.put(mSelfId, position);
		mSelfId ++;
		layoutToClick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				int pos = mIdForPos.get(id);
			//	TabMsgFrdReqNotifActivity.getInstance().onPopupForResponse(pos);
				FrdRequestNotifActivity.getInstance().onPopupForResponse2(pos);
			}
		});
		
		if(avatarId==0)
			avatarV.setImageResource(R.drawable.cb0_h001);
		else
			avatarV.setImageResource(R.drawable.cb0_h003);
		nameOfSpeakerV.setText(name);
		textV.setText(sentence);
		timeV.setText(time);
		if(status == FrdReqNotifItemEntity.mUnanswer) {
			statusV.setVisibility(TextView.INVISIBLE);
		} else {
			statusV.setVisibility(TextView.VISIBLE);
		}
		
		return convertView;
	}
	
	public int getCount() {
		return mVector.size();
	}

	public Object getItem(int position) {
		return mVector.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
}
