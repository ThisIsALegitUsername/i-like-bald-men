package dev.uwuclient.visual.notifications;

import dev.uwuclient.util.Color;
import dev.uwuclient.util.RenderUtils;
import dev.uwuclient.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {
    private final String description;
    private final NotificationType type;
    private long delay, start, end;

    private final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    private float xVisual = sr.getScaledWidth();
    public float yVisual = sr.getScaledHeight() - 50;
    public float y = sr.getScaledHeight() - 50;

    private final TimeUtil timer = new TimeUtil();

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public Notification(final String description, final long delay, final NotificationType type) {
        this.description = description;
        this.delay = delay;
        this.type = type;

        start = System.currentTimeMillis();
        end = start + delay;
    }

    public String getDescription() {
        return description;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(final long delay) {
        this.delay = delay;
    }

    public void setStart(final long start) {
        this.start = start;
    }

    public void setEnd(final long end) {
        this.end = end;
    }

    public void render() {
        final String yes = type.name().toLowerCase();
        final String name = yes.substring(0, 1).toUpperCase() + yes.substring(1, yes.length());
        

        final float screenWidth = sr.getScaledWidth();
        float x = (screenWidth) - (Math.max(Minecraft.getMinecraft().fontRendererObj.getStringWidth(description), Minecraft.getMinecraft().fontRendererObj.getStringWidth(name))) - 2;

        final float curr = System.currentTimeMillis() - getStart();
        final float percentageLeft = curr / getDelay();

        if (percentageLeft > 0.9) x = screenWidth;

        if (timer.hasReached(1000 / 60)) {
            xVisual = lerp(xVisual, x, 0.2f);
            yVisual = lerp(yVisual, y, 0.2f);

            timer.reset();
        }

        final Color c = new Color(159, 24, 242);

        RenderUtils.drawRoundedRect(xVisual-10, yVisual-8, sr.getScaledWidth(), yVisual+25, 4, new Color(0, 0, 0, 170).getRGB());

        Gui.drawRect(xVisual-10 + (percentageLeft * (Minecraft.getMinecraft().fontRendererObj.getStringWidth(description)) + 8), yVisual + 20, screenWidth + 1, yVisual + 22, c.getRGB());

        final Color bright = new Color(Math.min(c.getRed() + 16, 255), Math.min(c.getGreen() + 35, 255), Math.min(c.getBlue() + 7, 255));

        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, xVisual - 2, yVisual - 2, /*bright.getRGB()*/ type.color);
        Minecraft.getMinecraft().fontRendererObj.drawString(description, xVisual  - 2, yVisual + 10, c.getRGB(), false);
    }

    public final float lerp(final float a, final float b, final float c) {
        return a + c * (b - a);
    }

    public enum NotificationType {
        NOTIFICATION(Color.pink.getRGB()),
        WARNING(Color.yellow.getRGB()),
        ERROR(new Color(255, 0, 0).darker().getRGB());

        private int color;
        NotificationType(int color){
            this.color = color;
        }
    }
}
