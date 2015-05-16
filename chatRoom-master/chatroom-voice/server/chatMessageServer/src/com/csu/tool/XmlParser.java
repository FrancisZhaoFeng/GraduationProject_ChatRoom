package com.csu.tool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.csu.bean.User;

/**
 * 解析xml文件
 * 在这里将登录用户的一部分数据保存在xml文件当中，相当于一个数据库
 * @author Administrator
 *
 */
public class XmlParser {
	/**
	 * 根据ID查询登录用户
	 * @param userId
	 * @return
	 */
	public static User queryUserById(long userId){
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(new File("loginUser.xml"));
			Element root=doc.getRootElement();
			Iterator<Element> iter=root.elementIterator("user");
			while(iter.hasNext()){
				Element item =(Element)iter.next();
				String id = item.attribute("id").getText();
				if(Integer.parseInt(id) == userId){
					User user = new User();
					user.setId(userId);
					user.setName(item.element("name").getText());
					user.setImg(item.element("image").getText());
					return user;
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * 保存登录用户信息
	 * @param name
	 * @param input
	 */
	public static String saveUserInfo(String name,
			InputStream input) {
		SAXReader read=new SAXReader();
		FileOutputStream output = null;
		XMLWriter xmlwriter = null;
		String uniqueId="";
		try {
			String filePath = "loginUser.xml";
			Document doc=read.read(new File(filePath));
			Element root=doc.getRootElement();
			Element userElement=root.addElement("user");
			uniqueId = String.valueOf(getUniqueNum());
			userElement.addAttribute("id", uniqueId);
			userElement.addElement("name").setText(name);
			String fileName = System.currentTimeMillis()+".png";
			userElement.addElement("image").setText(fileName);
			File saveFile = new File("image/"+fileName);
			output = new FileOutputStream(saveFile);
			byte buffers[] = new byte[1024];
			int length = 0;
			while(input.available() > 0){
				length = input.read(buffers);
				output.write(buffers, 0, length);
			}
			xmlwriter=new XMLWriter(new FileOutputStream(filePath),OutputFormat.createPrettyPrint());
			xmlwriter.write(doc);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
					output.close();
				if (xmlwriter != null)
					xmlwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return uniqueId;
	}
	
	/**
	 * 生成当前注册用户的唯一标识
	 */
	public static long getUniqueNum(){
		SAXReader reader = new SAXReader();
		long firstNum = 1000;
		long newNum = 0;
		try {
			Document doc = reader.read(new File("loginUser.xml"));
			Element root=doc.getRootElement();
			Iterator<Element> iter=root.elementIterator("user");
			while(iter.hasNext()){
				Element item =(Element)iter.next();
				long temp = Long.parseLong(item.attribute("id").getText());
				if(newNum < temp){
					newNum = temp;
				}
			}
			if(newNum!= 0){
				newNum = newNum+1;
			}else{
				newNum = firstNum;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return newNum;
	}
}
