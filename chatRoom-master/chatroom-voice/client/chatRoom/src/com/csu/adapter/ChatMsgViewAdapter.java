package com.csu.adapter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.csu.bean.Message;
import com.csu.bean.User;
import com.csu.chatroom.R;
import com.csu.tool.ExpressionUtil;
import com.csu.tool.SystemConstant;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.MessageQueue.IdleHandler;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 聊天页面ListView内容适配器
 */
public class ChatMsgViewAdapter extends BaseAdapter{
	private Context context;
    private LayoutInflater mInflater;
    private List<Message> msgList;
    private User curUser;
    private Timer timer = new Timer();
    public ChatMsgViewAdapter(Context context, User curUser, List<Message> msgList) {
        this.context = context;
        this.msgList = msgList;
        this.curUser = curUser;
        mInflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return msgList.size();
    }

    public Object getItem(int position) {
        return msgList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
	public int getItemViewType(int position) {
		// 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
		Message msg = msgList.get(position);
		if (msg.getId() == curUser.getId()) {
				return 0;
		} else {
				return 1;
		}
	}
    
	public int getViewTypeCount() {
		// 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
		return 2;
	}
	
    public View getView(final int position, View convertView, ViewGroup parent) {
    	final Message msg = msgList.get(position);
    	ViewHolder viewHolder = null;
	    if (convertView == null)
	    {	
	    	  if (msg.getId() == curUser.getId())
			  {	
				  convertView = mInflater.inflate(R.layout.chat_item_right, null);
			  }else{
				  convertView = mInflater.inflate(R.layout.chat_item_left, null);
			  }
	    	  viewHolder = new ViewHolder();
			  viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			  viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
			  viewHolder.msgBgView = (View) convertView.findViewById(R.id.chat_msg_bg);
			  viewHolder.ivUserImage = (ImageView) convertView.findViewById(R.id.iv_userhead);
			  convertView.setTag(viewHolder);
	    }else{
	        viewHolder = (ViewHolder) convertView.getTag();
	    }
	    viewHolder.tvSendTime.setText(msg.getSend_date());
	    viewHolder.tvUserName.setText(msg.getSend_person());
	    SpannableString spannableString = ExpressionUtil.getExpressionString(context, msg.getSend_ctn()); 
	    TextView tvContent = (TextView) viewHolder.msgBgView.findViewById(R.id.tv_chatcontent);
	    final ImageView ivPlay = (ImageView) viewHolder.msgBgView.findViewById(R.id.iv_play_voice);
	    tvContent.setText(spannableString);
	    if(msg.isIfyuyin()){
	    	ivPlay.setVisibility(View.VISIBLE);
	    }else{
	    	ivPlay.setVisibility(View.GONE);
	    }
	    //处理语音消息的单击事件
	    viewHolder.msgBgView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String path = msg.getRecord_path();
				if(null!= path && !"".equals(path)){
					try {
						//播放动画
						final long recordTime = msg.getRecordTime();
						//根据类型选择左右不同的动画
						final int type = getItemViewType(position);
						if(type == 0){
							ivPlay.setBackgroundResource(R.drawable.chatto_voice_play_frame);
						}else{
							ivPlay.setBackgroundResource(R.drawable.chatfrom_voice_play_frame);
						}
						final AnimationDrawable animation = (AnimationDrawable) ivPlay.getBackground();
						context.getMainLooper().myQueue().addIdleHandler(new IdleHandler() {
							public boolean queueIdle() {
								animation.start();
								timer.schedule(new RecordTimeTask(animation, ivPlay, type), recordTime);
								return false;
							}
						});
						//此处需开启线程进行音频播放，否则会堵塞UI
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									BufferedInputStream dis = new BufferedInputStream(
											new FileInputStream(path));
									int bufferSize = AudioRecord
											.getMinBufferSize(
													SystemConstant.SAMPLE_RATE_IN_HZ,
													SystemConstant.CHANNEL_CONFIG,
													SystemConstant.AUDIO_FORMAT);
									byte[] buffer = new byte[bufferSize];
									//实例化AudioTrack  
									AudioTrack track = new AudioTrack(
											AudioManager.STREAM_MUSIC,
											SystemConstant.SAMPLE_RATE_IN_HZ,
											SystemConstant.CHANNEL_CONFIG,
											SystemConstant.AUDIO_FORMAT,
											bufferSize, AudioTrack.MODE_STREAM);
									//开始播放  
									track.play();
									//由于AudioTrack播放的是流，所以，我们需要一边播放一边读取  
									int length = 0;
									while ((length = dis.read(buffer)) != -1) {
										//然后将数据写入到AudioTrack中
										track.write(buffer, 0, length);
									}
									track.stop();
									track.release();
									dis.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	    if(null!= msg.getBitmap()){
	    	viewHolder.ivUserImage.setImageBitmap(msg.getBitmap());
	    }
	    return convertView;
    }
    
    private final class RecordTimeTask extends TimerTask{
		private AnimationDrawable animation;
		private ImageView ivPlay;
		private int type;
		public RecordTimeTask(AnimationDrawable animation, ImageView ivPlay, int type) {
			this.animation = animation;
			this.ivPlay = ivPlay;
			this.type = type;
		}
		public void run() {
			animation.stop();
			android.os.Message msg = handle.obtainMessage();
			msg.obj = ivPlay;
			msg.arg1 = this.type;
			handle.sendMessage(msg);
		}
    }
    
    private Handler handle = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			ImageView ivPlay = (ImageView) msg.obj;
			int type = msg.arg1;
			if(type == 0){
				ivPlay.setBackgroundResource(R.drawable.chatto_voice_playing);
			}else{
				ivPlay.setBackgroundResource(R.drawable.chatfrom_voice_playing);
			}
		}
    };
    
    private class ViewHolder { 
        public TextView tvSendTime;
        public TextView tvUserName;
        public View msgBgView;
        public ImageView ivUserImage;
    }
}
