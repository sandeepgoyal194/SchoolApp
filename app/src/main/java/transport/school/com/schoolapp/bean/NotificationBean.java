package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class NotificationBean{

	@SerializedName("routeid")
	private String routeid;

	@SerializedName("stopid")
	private String stopid;

	@SerializedName("message")
	private String message;

	public void setRouteid(String routeid){
		this.routeid = routeid;
	}

	public String getRouteid(){
		return routeid;
	}

	public void setStopid(String stopid){
		this.stopid = stopid;
	}

	public String getStopid(){
		return stopid;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"NotificationBean{" + 
			"routeid = '" + routeid + '\'' + 
			",stopid = '" + stopid + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}