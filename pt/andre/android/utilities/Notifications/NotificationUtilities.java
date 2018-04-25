package pt.andre.android.utilities.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtilities {

    public static void dismiss(@NonNull Context context, int id){
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if(notificationManager != null)
            notificationManager.cancel(id);
    }

    public static NotificationUtilities.Builder notify(@NonNull Context context, int id){
        return new Builder(context, id);
    }


    public static class Builder{
        private Context context;
        private int id;
        private String title;
        private String description;
        private int icon = -1;
        private Intent onClick;
        private String channelID = "DEFAULT_ID";
        private boolean dismissOnTouch = false;
        private boolean onGoing = false;
        private int notificationPriority = NotificationCompat.PRIORITY_DEFAULT;
        private int channelPriority = 3; //Default;
        private String channelName = "General";

        private Builder(){}

        Builder(Context context, int id) {
            this.context = context;
            this.id = id;
        }

        public Builder setTitle(String title){
            this.title = title;
            return this;
        }

        public Builder setDescription(String description){
            this.description = description;
            return this;
        }

        public Builder setIcon(int icon){
            this.icon = icon;
            return this;
        }

        public Builder setOnNotificationClick(Intent onClick){
            this.onClick = onClick;
            return this;
        }

        public Builder selectNotificationChannel(String channel){
            this.channelID = channel;
            return this;
        }

        public Builder dismissOnTouch(boolean value){
            this.dismissOnTouch = value;
            return this;
        }

        public Builder dismissable(boolean value){
            this.onGoing = !value;
            return this;
        }

        public Builder setNotificationLowPriority(){
            notificationPriority = NotificationCompat.PRIORITY_LOW;
            return this;
        }

        public Builder setNotificationHighPriority(){
            notificationPriority = NotificationCompat.PRIORITY_HIGH;
            return this;
        }

        public Builder setNotificationDefaultPriority(){
            notificationPriority = NotificationCompat.PRIORITY_DEFAULT;
            return this;
        }

        public Builder setNotificationMinimumPriority(){
            notificationPriority = NotificationCompat.PRIORITY_MIN;
            return this;
        }

        public Builder setNotificationMaximumPriority(){
            notificationPriority = NotificationCompat.PRIORITY_MAX;
            return this;
        }

        public Builder setChannelLowPriority(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                channelPriority = NotificationManager.IMPORTANCE_LOW;

            return this;
        }

        public Builder setChannelHighPriority(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                channelPriority = NotificationManager.IMPORTANCE_HIGH;

            return this;
        }

        public Builder setChannelDefaultPriority(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                channelPriority = NotificationManager.IMPORTANCE_DEFAULT;

            return this;
        }

        public Builder setChannelMinimumPriority(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                channelPriority = NotificationManager.IMPORTANCE_MIN;

            return this;
        }

        public Builder setChannelMaximumPriority(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                channelPriority = NotificationManager.IMPORTANCE_MAX;

            return this;
        }

        public Builder setChannelNonePriority(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                channelPriority = NotificationManager.IMPORTANCE_NONE;

            return this;
        }

        public Builder setChannelName(String name){
            channelName = name;
            return this;
        }

        public Builder setChannelID(String id){
            this.channelID = id;
            return this;
        }

        //Terminal Operations
        public void show() throws Exception {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(id, createNotification().build());
        }

        private NotificationCompat.Builder createNotification() throws Exception {
            if(channelID == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                throw new Exception("You must indicate a notification channel if you are targetting >= 8.0");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createChannel(context, channelID, channelName, channelPriority);

            final NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channelID);

            if(title != null)
                notification.setContentTitle(title);

            if(icon != -1)
                notification.setSmallIcon(icon);

            if(description != null)
                notification.setContentText(description);

            if(onClick != null)
                notification.setContentIntent(PendingIntent.getActivity(context, id, onClick, 0));

            notification.setChannelId(channelID)
                        .setAutoCancel(dismissOnTouch)
                        .setOngoing(onGoing)
                        .setPriority(notificationPriority);

            return notification;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void createChannel(Context context, String id, String title, int priority){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            if(notificationManager != null && notificationManager.getNotificationChannel(id) != null)
                return;

            NotificationChannel channel = new NotificationChannel(id, title, priority);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
