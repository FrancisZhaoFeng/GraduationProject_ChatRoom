package com.csu.service;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.csu.bean.Message;
import com.csu.bean.User;
import com.csu.constant.ContentFlag;
import com.csu.db.MessageDbHelper.MessageColumns;
import com.csu.impl.IhandleMessge;
import com.csu.tool.StreamTool;

public class MessageService {
	private Context context;
	private User user;
	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private Map<Integer, Bitmap> imgMap = new HashMap<Integer, Bitmap>();	//缓存在线用户头像数据
	public MessageService(Context context) {
		this.context = context;
	}
	
	/**
	 * 建立连接
	 * @return
	 */
	public void startConnect(User user, IhandleMessge handle) throws IOException{
		this.user = user;
		String ip = user.getIp();
		String port = user.getPort();
		long id = user.getId();
		try {
			SocketAddress socAddress = new InetSocketAddress(InetAddress.getByName(ip), Integer.parseInt(port)); 
			socket = new Socket();
			socket.connect(socAddress, 5*1000);
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			//处理用户登录
			String str = ContentFlag.ONLINE_FLAG + id;
			output.writeUTF(str);
			// 缓存其它登录者的头像数据
			int fileNums = input.readInt(); // 图片文件的数量
			for (int i = 0; i < fileNums; i++) {
				int tempId = Integer.parseInt(input.readUTF());
				byte[] datas = StreamTool.readStream(input);
			    Bitmap tempImg = BitmapFactory.decodeByteArray(datas, 0, datas.length);
				imgMap.put(tempId, tempImg);
			}
			//接收消息
			receiveMsg(handle);
		} catch (IOException e) {
			throw new IOException("fail connect to the server");
		} 
	}
	
	/**
	 * 应用退出
	 */
	public void quitApp() {
		String sendStr="";
		if(null!= user){
			sendStr = ContentFlag.OFFLINE_FLAG + this.user.getId();
		}
		if (!socket.isClosed()) {
			if (socket.isConnected()) {
				if (!socket.isOutputShutdown()) {
					try {
						output.writeUTF(sendStr);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (null != input)  input.close();
							if (null != output) output.close();
							if (null != socket) socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 接收消息
	 * @return
	 * @throws IOException 
	 */
	public void receiveMsg(IhandleMessge handle) throws IOException {
		try {
			while(true){
				String msgCtn = input.readUTF();
				if(msgCtn.startsWith(ContentFlag.ONLINE_FLAG)){				//处理登录消息
					String json = input.readUTF();
					Message msg = parseJsonToObject(json);
					byte[] datas = StreamTool.readStream(input);
				    Bitmap bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
					msg.setBitmap(bitmap);
					handle.handleMsg(msg);
					imgMap.put(msg.getId(), bitmap);
				}else if(msgCtn.startsWith(ContentFlag.OFFLINE_FLAG)){		//处理退出消息
					String json = input.readUTF();
					Message msg = parseJsonToObject(json);
					msg.setBitmap(imgMap.get(msg.getId()));
					handle.handleMsg(msg);
					imgMap.remove(msg.getId());
				}else if(msgCtn.startsWith(ContentFlag.RECORD_FLAG)){		//处理语音消息
					String filename = msgCtn.substring(ContentFlag.RECORD_FLAG.length());
					File dir = new File(Environment.getExternalStorageDirectory() + "/recordMsg/");
					if(!dir.exists()) dir.mkdirs();
					File file = new File(dir, filename);
					String json = input.readUTF();
					Message msg = parseJsonToObject(json);
					msg.setRecord_path(file.getAbsolutePath());
					msg.setBitmap(imgMap.get(msg.getId()));
					msg.setIfyuyin(true);
					handle.handleMsg(msg);
					saveRecordFile(file);
				}else{														//处理普通消息
					Message msg = parseJsonToObject(msgCtn);
					msg.setBitmap(imgMap.get(msg.getId()));
					handle.handleMsg(msg);
				}
			}
		} catch (Exception e) {
			if (!socket.isClosed()) {
				throw new IOException("fail connect to the server");
			}
		}
	}
	
	/**
	 * 保存录音文件
	 * @param filename
	 * @param input2
	 * @throws IOException 
	 */
	private void saveRecordFile(File file) throws IOException {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			byte[] datas = StreamTool.readStream(input);
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(datas);
		}
	}

	/**
	 * 解析json字符串
	 */
	public Message parseJsonToObject(String json){
		try {
			JSONArray arrays = new JSONArray(json);
			JSONObject jsonObject = arrays.getJSONObject(0);
			int userId = Integer.parseInt(jsonObject.getString(MessageColumns.ID));	//用户的ID
			String send_person = jsonObject.getString(MessageColumns.SEND_PERSON);	//发送者
			String send_ctn = jsonObject.getString(MessageColumns.SEND_CTN);			//发送内容
			String send_date = jsonObject.getString(MessageColumns.SEND_DATE);		//发送时间
			Message msg = new Message();
			msg.setId(userId);
			msg.setSend_ctn(send_ctn);
			msg.setSend_person(send_person);
			msg.setSend_date(send_date);
			if(jsonObject.has("recordTime")){
				String recordTime = jsonObject.getString("recordTime");
				msg.setRecordTime(Long.valueOf(recordTime));
			}
			return msg;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送消息
	 * @param ctn
	 * @throws IOException 
	 */
	public void sendMsg(String ctn) throws Exception {
		output.writeUTF(ctn);
	}
	
	/**
	 * 发送语音消息
	 * @param file
	 * @throws Exception 
	 */
	public void sendRecordMsg(File file, long recordTime) throws Exception{
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			String flagLine = ContentFlag.RECORD_FLAG + file.getName();
			//写入标识行
			output.writeUTF(flagLine);
			//写入语音时间
			output.writeLong(recordTime);
			byte[] buffer = new byte[2048];
			int length = 0;
			//写入文件的大小
			output.writeInt((int) file.length());
			while ((length = inputStream.read(buffer)) != -1) {
				output.write(buffer, 0, length);
			}
			output.flush();
		} catch (Exception e) {
			throw new Exception();
		}finally{
			try {
				if (file != null) file.delete();
				if (inputStream != null) inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
