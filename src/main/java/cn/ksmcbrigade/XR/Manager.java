package cn.ksmcbrigade.XR;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class Manager {

    public static KeyMapping keyMapping = new KeyMapping("key.xr", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X,"key.xr");

    public static void run(Player player){
        if(!Xray.ENabled){
            Minecraft.getInstance().reloadResourcePacks();
            Xray.ENabled = true;
        }
        //Use Mixin
    }

    public static KeyMapping getKey(){
        return keyMapping;
    }
}
