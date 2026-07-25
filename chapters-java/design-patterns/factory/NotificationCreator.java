package factory;

public abstract class NotificationCreator {
    public abstract Notification createNotification();
    
    public void sendNotification(String message){
        createNotification().send(message);
    }
}
