package net.minecraft.util;

import dev.uwuclient.event.impl.Button;
import dev.uwuclient.event.impl.MoveButtonEvent;
import net.minecraft.client.settings.GameSettings;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files are (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class MovementInputFromOptions extends MovementInput {
	private final GameSettings gameSettings;

	public MovementInputFromOptions(GameSettings gameSettingsIn) {
		this.gameSettings = gameSettingsIn;
	}

    public void updatePlayerMoveState() {
        final MoveButtonEvent event = new MoveButtonEvent(new Button(this.gameSettings.keyBindLeft.isKeyDown(), 90), new Button(this.gameSettings.keyBindRight.isKeyDown(), -90), new Button(this.gameSettings.keyBindBack.isKeyDown(), 180), new Button(this.gameSettings.keyBindForward.isKeyDown(), 0), this.gameSettings.keyBindSneak.isKeyDown(), this.gameSettings.keyBindJump.isKeyDown(), 0.3D);
        event.call();
        if (event.isCancelled()) return;

        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (event.getForward().isButton()) {
            ++this.moveForward;
        }

        if (event.getBackward().isButton()) {
            --this.moveForward;
        }

        if (event.getLeft().isButton()) {
            ++this.moveStrafe;
        }

        if (event.getRight().isButton()) {
            --this.moveStrafe;
        }

        final double sneakMultiplier = event.getSneakSlowDownMultiplier();

        this.jump = event.isJump();
        this.sneak = event.isSneak();

        if (this.sneak) {
            this.moveStrafe = (float) ((double) this.moveStrafe * sneakMultiplier);
            this.moveForward = (float) ((double) this.moveForward * sneakMultiplier);
        }
    }
}