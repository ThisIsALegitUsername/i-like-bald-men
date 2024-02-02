package dev.uwuclient.visual.clickguis.strike;

import java.util.ArrayList;
import java.util.List;

import dev.uwuclient.mod.base.Mod.Category;
import dev.uwuclient.visual.clickguis.ClientGui;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;

public class StrikeGUI extends GuiScreen implements ClientGui {

    public static float scrollHorizontal;
    private final List<ClickFrame> frames = new ArrayList<>();
    private float lastScrollHorizontal;

    public void updateScroll() {
        if (GuiInventory.isCtrlKeyDown()) {
            final float partialTicks = mc == null || mc.timer == null ? 1.0F : mc.timer.renderPartialTicks;

            final float lastLastScrollHorizontal = lastScrollHorizontal;
            lastScrollHorizontal = scrollHorizontal;
            final float wheel = Mouse.getDWheel();
            scrollHorizontal += wheel / 10.0F;
            if (wheel == 0) scrollHorizontal -= (lastLastScrollHorizontal - scrollHorizontal) * 0.6 * partialTicks;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        if (frames.size() <= 0) {
            int index = -1;
            for (final Category category : Category.values()) {
                final ClickFrame frame = new ClickFrame(category, 20 + (++index * (ClickFrame.entryWidth + 20)), 20);
                frames.add(frame);
            }
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        frames.forEach(frame -> frame.draw(this, mouseX, mouseY));
        frames.forEach(frame -> frame.drawDescriptions(mouseX, mouseY, partialTicks));
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        frames.forEach(frame -> frame.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        frames.forEach(frame -> frame.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        frames.forEach(frame -> frame.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
    }
}