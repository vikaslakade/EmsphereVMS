package activitys;

/**
 * Created by commando1 on 8/22/2017.
 */

public class NotificationPojo {
 String Title,body,name ,mobile_no, representing, purpose,Notification_id,Visitor_entry_id;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getRepresenting() {
        return representing;
    }

    public void setRepresenting(String representing) {
        this.representing = representing;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNotification_id() {
        return Notification_id;
    }

    public void setNotification_id(String notification_id) {
        Notification_id = notification_id;
    }

    public String getVisitor_entry_id() {
        return Visitor_entry_id;
    }

    public void setVisitor_entry_id(String visitor_entry_id) {
        Visitor_entry_id = visitor_entry_id;
    }

    @Override
    public String toString() {
        return "NotificationPojo{" +
                "Title='" + Title + '\'' +
                ", body='" + body + '\'' +
                ", name='" + name + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", representing='" + representing + '\'' +
                ", purpose='" + purpose + '\'' +
                ", Notification_id='" + Notification_id + '\'' +
                ", Visitor_entry_id='" + Visitor_entry_id + '\'' +
                '}';
    }
}
