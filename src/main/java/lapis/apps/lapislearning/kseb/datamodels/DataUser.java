package lapis.apps.lapislearning.kseb.datamodels;

public class DataUser {
//{"result":[{"login_id":"2","username":"1212","password":"1212","role":"user","show_alert":1,
// "alert_msg":"Your Usage 578. You have exceeded the limit 25"}]}
    String login_id,username,password,role,show_alert,alert_msg,pinCode,nameU,limitU;

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getShow_alert() {
        return show_alert;
    }

    public void setShow_alert(String show_alert) {
        this.show_alert = show_alert;
    }

    public String getAlert_msg() {
        return alert_msg;
    }

    public void setAlert_msg(String alert_msg) {
        this.alert_msg = alert_msg;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getNameU() {
        return nameU;
    }

    public void setNameU(String nameU) {
        this.nameU = nameU;
    }

    public String getLimitU() {
        return limitU;
    }

    public void setLimitU(String limitU) {
        this.limitU = limitU;
    }
}
