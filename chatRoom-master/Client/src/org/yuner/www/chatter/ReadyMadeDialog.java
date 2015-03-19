package org.yuner.www.chatter;

import java.util.ArrayList;
import java.util.List;

import org.yuner.www.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ReadyMadeDialog {

	private Context mContext0;
	private EditText mEt0;
	private Dialog mBuilder=null;
	
	public ReadyMadeDialog(Context context,EditText et)
	{
		this.mContext0=context;
		this.mEt0=et;
	}
	
	public void createReadyMadeDialog()
	{
		mBuilder = new Dialog(mContext0);
		
		LayoutInflater inflater = LayoutInflater.from(mContext0);
		View viewTemp = inflater.inflate(R.layout.cb0_ready_made, null);
		ListView tempView=(ListView)viewTemp.findViewById(R.id.cb0ReadymadeListview);
		
		List<String> list0=new ArrayList<String>();
		list0.add("there is a car accident");
		list0.add("can anyone tell me the police number");
		tempView.setAdapter(new ReadyMadeAdapter(mContext0,list0));
		tempView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) 
			{
				switch (position){
				case 0:
					String st0=new MyLocation(mContext0).getMyLocation();
					mEt0.append(st0);
					mBuilder.dismiss();
					break;
				default:
					mBuilder.dismiss();
					break;
				}
			}
		});
		mBuilder.setContentView(viewTemp);
		mBuilder.show();
	}
	
	public class ReadyMadeAdapter extends BaseAdapter{
		private Context context;
		private List<String> vector;
		LayoutInflater inflater;
		
		public ReadyMadeAdapter(Context context,List<String> vector){
			this.context = context;
			this.vector = vector;
			inflater = LayoutInflater.from(context);
		}

		public View getView(int position, View convertView, ViewGroup root) {
			TextView content;
			
			String st0=vector.get(position);
			
			convertView = inflater.inflate(R.layout.cb0_ready_made_item, null);
			content=(TextView) convertView.findViewById(R.id.cb0ReadymadeItem);
			content.setText(st0);

			return convertView;
		}
		
		public int getCount() {
			return vector.size();
		}

		public Object getItem(int position) {
			return vector.get(position);
		}

		public long getItemId(int position) {
			return position;
		}
	}
}
