package dev.uwuclient.util;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;
import net.lax1dude.eaglercraft.v1_8.opengl.VertexFormat;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

// i removed some stuff bc i dont want to resent source leak
public class RenderUtils {
    
    public static void color(Color color) {
        if (color == null)
            color = Color.white;
        GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public static void gradient(final double x, final double y, final double width, final double height, final Color color1, final Color color2) {
        gradient(x, y, width, height, true, color1, color2);
    }

    public static void drawRect(double left, double top, double width, double height, int color){
        Gui.drawRect(left, top, left+width, top+height, color);
    }

    public static void gradient(final double x, final double y, final double width, final double height, final boolean filled, final Color color1, final Color color2) {

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.shadeModel(RealOpenGLEnums.GL_SMOOTH);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(RealOpenGLEnums.GL_GREATER, 0);
        if (color1 != null)
            color(color1);
        worldrenderer.begin(filled ? RealOpenGLEnums.GL_QUADS : RealOpenGLEnums.GL_LINES, VertexFormat.POSITION_TEX);
        {
            worldrenderer.pos(x, y, 0).endVertex();
            worldrenderer.pos(x + width, y, 0).endVertex();
            if (color2 != null)
                color(color2);
            worldrenderer.pos(x + width, y + height, 0).endVertex();
            worldrenderer.pos(x, y + height, 0).endVertex();
            if (!filled) {
                worldrenderer.pos(x, y, 0).endVertex();
                worldrenderer.pos(x, y + height, 0).endVertex();
                worldrenderer.pos(x + width, y, 0).endVertex();
                worldrenderer.pos(x + width, y + height, 0).endVertex();
            }
        }
        tessellator.draw();
        GlStateManager.alphaFunc(RealOpenGLEnums.GL_GREATER, 0.1f);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(RealOpenGLEnums.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRectOutline(double param1, double param2, double width1, double height1, int color) {
        Gui.drawRect(param1, param2, width1, param2 + 1, color);
        Gui.drawRect(param1, param2, param1 + 1, height1, color);
        Gui.drawRect(width1 - 1, param2, width1, height1, color);
        Gui.drawRect(param1, height1 - 1, width1, height1, color);
    }

    public static void startScale(float x, float y, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-x, -y, 0);
    }

    public static void drawCenteredStringVert(String s, float x, float y, int color){
        Minecraft.getMinecraft().fontRendererObj.drawString(s, x, y-5, -1, true);
    }

    public static int astolfoColorsDraw(int yOffset, int yTotal, float speed) {
        float hue = (float) (System.currentTimeMillis() % (int)speed) + ((yTotal - yOffset) * 9);
        while (hue > speed) {
           hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
           hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, 0.5f, 1F);
     }

    public static void drawChromaRectangle(int x, int y, int width, int height) {
		int i = x;
		while(true) {
			if(i+10 <= width) {
				Gui.drawRect(i, y, i+10, height,RenderUtils.astolfoColorsDraw(i, GuiScreen.width,10000f));
			} else {
				break;
			}
			i+=10;
		}
		if(width-i != 0) {
			for(int h = i; h < width; h++) {
				Gui.drawRect(h, y, h+1, height,RenderUtils.astolfoColorsDraw(h, GuiScreen.width,10000f));
			}
		}
	}

	public static void drawChromaString(final String string, final int x, final int y, final boolean shadow) {
        final Minecraft mc = Minecraft.getMinecraft();
        int xTmp = x;
        char[] charArray;
        for (int length = (charArray = string.toCharArray()).length, j = 0; j < length; ++j) {
            final char textChar = charArray[j];
            final long l = System.currentTimeMillis() - (xTmp * 10 - y * 10);
            final int i = Color.HSBtoRGB(l % 2000L / 2000.0f, 0.8f, 0.8f);
            final String tmp = String.valueOf(textChar);
            mc.fontRendererObj.drawString(tmp, (float) xTmp, (float) y, i, shadow);
            xTmp += mc.fontRendererObj.getCharWidth(textChar);
        }
    }

	/*public static void drawRoundedRect(final float left, final float top, final float right, final float bottom, final float radius, final int color) {
        final float f1 = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GlStateManager.color(f2, f3, f4, f1);
            drawRoundedRect(left, top, right, bottom, radius);
    }

    public static void drawRoundedRect(final float paramFloat1, final float paramFloat2, final float paramFloat3, final float paramFloat4, final float paramFloat5) {
        
    }*/

    public static void renderChromaString(String text, int x, int y) {
		int xPos = x;
		String[] r = text.split("");
		for(String s : r) {
			Minecraft.getMinecraft().fontRendererObj.drawString(s, xPos, y, RenderUtils.astolfoColorsDraw(xPos, GuiScreen.width, 10000f));
			xPos += Minecraft.getMinecraft().fontRendererObj.getStringWidth(s);
		}
	}

    public static Vec3 getRenderPos(double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();

        x -= mc.getRenderManager().renderPosX;
        y -= mc.getRenderManager().renderPosY;
        z -= mc.getRenderManager().renderPosZ;

        return new Vec3(x, y, z);
    }

}
