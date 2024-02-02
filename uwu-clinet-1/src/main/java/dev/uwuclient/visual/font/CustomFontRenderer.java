package dev.uwuclient.visual.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class CustomFontRenderer {
 

 // THIS IS GIOHVNIGN MEYE BRAIN DAMAGE I WNA OTI TO SHOTO MYSELF
 public String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

/*
U IS THE X OFFSET
Y IS THE Y OFFSET
IF U OFFSET SURPASSES 17 CHARACTERS (one line in glyph/1024 pixels), IT WILL GO TO THE NEXT LINE IN THE GLYPH.
 */
  public void drawString(String text, int x, int y, int color){
    int offset = 0;
    for(char c : text.toCharArray()){
      int u = 0, v = 0;
      if(chars.indexOf(c) != -1){
        u = chars.indexOf(c)+1;
        if(u > 1024){
          u = 0;
          v += 75;
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("eagler:gui/uwu-font/unicode_page_00.png"));
        //Gui.drawModalRectWithCustomSizedTexture(x+offset, y, u, v, 20, 20, 20, 30);
        offset += 15;
      }
    }
  }


/* old way 26 different files */

//public HashMap<Character, String> paths = new HashMap<Character, String>();

 /*public CustomFontRenderer(){
	for(char c : chars.toCharArray()){
		
  	this.paths.put(c, "eagler:gui/uwu-font/fontLetter_" + String.valueOf(c) + ".png");
 }
 }*/
 
 /*public void drawString(String text, int x, int y, int color){
	int charOffset = 0;
  	for(char c : chars.toCharArray()){
    if(this.paths.get(c) != null){
     Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("eagler:gui/uwu-font/fontLetter_"+String.valueOf(c)+".png"));
     Gui.drawModalRectWithCustomSizedTexture(x+charOffset, y, 0, 0, 10, 10, 10, 10);
     charOffset += 12;
    }
   }
 }*/

 
}