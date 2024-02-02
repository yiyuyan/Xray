package cn.ksmcbrigade.XR;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Mod("xr")
@Mod.EventBusSubscriber
public class Xray {

    public static Logger LOGGER = LogManager.getLogger();
    public static Path path = Paths.get("config/xr-config.json");
    public static ArrayList<String> blocks = new ArrayList<>();
    public static Player player = Minecraft.getInstance().player;
    public static boolean ENabled = false;

    public Xray() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info(Blocks.DIAMOND_ORE.getDescriptionId());
        LOGGER.info("X-Ray loading...");
        init();
        LOGGER.info("X-Ray loaded.");
    }

    public static void init() throws IOException {
        if(!new File("config/vm/mods").exists()){
            new File("config/vm/mods").mkdirs();
        }
        if(!new File("config/vm/mods/Xray.json").exists()){
            JsonObject object = new JsonObject();
            object.addProperty("name","key.xr");
            object.addProperty("id","xr");
            object.addProperty("main","cn.ksmcbrigade.XR.Manager");
            object.addProperty("function","getKey");
            object.addProperty("function_2","run");
            object.addProperty("gui_main","NONE");
            object.addProperty("gui_function","NONE");
            Files.write(Paths.get("config/vm/mods/XRay.json"),object.toString().getBytes());
        }
        if(!new File("config/xr-config.json").exists()){
            JsonObject object = new JsonObject();
            object.add("minecraft:diamond_ore",null);
            object.add("minecraft:deepslate_diamond_ore",null);
            object.add("minecraft:diamond_block",null);

            object.add("minecraft:gold_ore",null);
            object.add("minecraft:deepslate_gold_ore",null);
            object.add("minecraft:minecraft:nether_gold_ore",null);
            object.add("minecraft:gold_block",null);
            object.add("minecraft:raw_gold_block",null);

            object.add("minecraft:coal_ore",null);
            object.add("minecraft:deepslate_coal_ore",null);
            object.add("minecraft:coal_block",null);

            object.add("minecraft:iron_ore",null);
            object.add("minecraft:deepslate_iron_ore",null);
            object.add("minecraft:iron_block",null);
            object.add("minecraft:raw_iron_block",null);

            object.add("minecraft:copper_ore",null);
            object.add("minecraft:deepslate_copper_ore",null);
            object.add("minecraft:copper_block",null);
            object.add("minecraft:raw_copper_block",null);

            object.add("minecraft:redstone_ore",null);
            object.add("minecraft:deepslate_redstone_ore",null);
            object.add("minecraft:redstone_block",null);

            object.add("minecraft:lapis_ore",null);
            object.add("minecraft:deepslate_lapis_ore",null);
            object.add("minecraft:lapis_block",null);

            object.add("minecraft:emerald_ore",null);
            object.add("minecraft:deepslate_emerald_ore",null);
            object.add("minecraft:emerald_block",null);

            object.add("minecraft:quartz_ore",null);
            object.add("minecraft:ancient_debris",null);

            object.add("minecraft:netherite_block",null);
            object.add("minecraft:quartz_block",null);

            object.add("minecraft:tnt",null);

            object.add("minecraft:chest",null);
            object.add("minecraft:ender_chest",null);
            object.add("minecraft:shulker_box",null);

            object.add("minecraft:furnace",null);
            object.add("minecraft:smoker",null);
            object.add("minecraft:blast_furnace",null);

            Files.write(path,object.toString().getBytes());
        }
        JsonObject json = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
        blocks.addAll(json.keySet());
    }

    public static Object is(String name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName("cn.ksmcbrigade.VM.Utils");
        Class<?>[] parameterTypes = new Class[]{String.class};
        Method method = clazz.getDeclaredMethod("isEnabledMod", parameterTypes);
        method.setAccessible(true);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        return method.invoke(instance, name);
    }

    @SubscribeEvent
    public static void OnTick(TickEvent.PlayerTickEvent event) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        player = event.player;
        Object o = is(I18n.get("key.xr"));
        if((o instanceof Boolean) && !((boolean) o) && ENabled){
            Minecraft.getInstance().reloadResourcePacks();
            ENabled = false;
        }
    }

    @SubscribeEvent
    public static void OnRegisterCommands(RegisterClientCommandsEvent event){
        event.getDispatcher().register(Commands.literal("xadd").executes(context -> {
            if (player != null) {
                Block block = player.getLevel().getBlockState(player.getOnPos()).getBlock();
                if(block!=Blocks.AIR){
                    try {
                        addBlock(getName(block));
                        player.sendMessage(Component.nullToEmpty("Done."),player.getUUID());
                    } catch (IOException e) {
                        e.printStackTrace();
                        player.sendMessage(Component.nullToEmpty("Error:"+e.getMessage()),player.getUUID());
                    }
                }
                else{
                    player.sendMessage(Component.nullToEmpty(I18n.get("chat.xr.air")),player.getUUID());
                }
            }
            return 0;
        }).then(Commands.argument("block", StringArgumentType.string()).executes(context -> {
            String block = StringArgumentType.getString(context,"block");
            try {
                addBlock(block);
                player.sendMessage(Component.nullToEmpty("Done."),player.getUUID());
            } catch (IOException e) {
                e.printStackTrace();
                player.sendMessage(Component.nullToEmpty("Error:"+e.getMessage()),player.getUUID());
            }
            return 0;
        })));

        event.getDispatcher().register(Commands.literal("xlist").executes(context -> {
            if (player != null) {
                player.sendMessage(Component.nullToEmpty("Blocks:"),player.getUUID());
                for(String block:blocks){
                    player.sendMessage(Component.nullToEmpty(block),player.getUUID());
                }
            }
            return 0;
        }));
    }

    public static String getName(Block block){
        String[] descriptionId = block.getDescriptionId().split("\\.");
        return descriptionId[1]+":"+descriptionId[2];
    }

    public static void addBlock(String name) throws IOException {
        blocks.add(name);
        JsonObject json = new JsonObject();
        for(String nam:blocks){
            json.add(nam,null);
        }
        Files.write(path,json.toString().getBytes());
    }
}