package wang.lanchun.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 消息类
 * @author lanchun
 *
 */
public class Message implements Serializable{

	private static final long serialVersionUID = -6580034629331635348L;

	//消息id
	private String id;
	
	//发送方
	private User sender;
	
	//接收方
	private User receiver;
	
	//消息内容
	private String content;
	
	//是否已读
	private boolean read;
	
	//消息发送时间
	private Timestamp time;
	
	public Message(){
		this.read = false;
	}
	
	public Message(User sender, User receiver, String content,Timestamp time) {
		this();
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.time = time;
	}

	public Message(int fromid,String content, Timestamp time) {
		this();
		this.sender = new User(fromid);
		this.content = content;
		this.time = time;
	}

	public Message(int fromid,int toid,String content,Timestamp time){
		this(fromid,content,time);
		this.receiver = new User(toid);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	
}
