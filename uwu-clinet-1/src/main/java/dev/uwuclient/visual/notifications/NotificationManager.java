package dev.uwuclient.visual.notifications;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import dev.uwuclient.visual.notifications.Notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManager {
    
    public final Queue<Notification> notifications = new LinkedList<>();

    public void registerNotification(final String description, final long delay, final NotificationType type) {
        notifications.add(new Notification(description, delay, type));
    }

    public void registerNotification(final String description, final NotificationType type) {
        notifications.add(new Notification(description, (long) (Minecraft.getMinecraft().fontRendererObj.getStringWidth(description) * 30), type));
    }

    public void registerNotification(final String description) {
        notifications.add(new Notification(description, (long) (Minecraft.getMinecraft().fontRendererObj.getStringWidth(description) * 40), NotificationType.NOTIFICATION));
    }

    public void amongus() {
        if (!notifications.isEmpty()) {
            if (notifications.element().getEnd() > System.currentTimeMillis()) {
                notifications.element().y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 50;
                notifications.element().render();
            } else {
                notifications.remove();
            }
        }

        if (notifications.size() > 0) {
            int i = 0;


            for(Iterator<Notification> itr = notifications.iterator(); itr.hasNext();){
                Notification notification = itr.next();

                if (i == 0) {
                    i++;
                    continue;
                }

                notification.y = (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 18) - (35 * (i + 1));
                notification.render();
                i++;
            }
            
        }
    }

}
