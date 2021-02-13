package me.gumikacsa.lootruns;

import me.gumikacsa.lootruns.logic.LootrunCommand;
import me.gumikacsa.lootruns.logic.LootrunManager;
import me.gumikacsa.lootruns.logic.LootrunRenderEngine;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

@Mod(
        modid = Lootruns.MOD_ID,
        name = Lootruns.MOD_NAME,
        version = Lootruns.VERSION,
        clientSideOnly = true
)
public class Lootruns {

    public static final String MOD_ID = "lootruns";
    public static final String MOD_NAME = "Lootruns";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.Instance(MOD_ID)
    public static Lootruns INSTANCE;

    private LootrunManager manager;
    private LootrunRenderEngine renderEngine;

    @Mod.EventHandler
    public void preinit(@Nonnull FMLPreInitializationEvent event) {
        this.manager = new LootrunManager(this);
        this.renderEngine = new LootrunRenderEngine(this, manager);
    }

    @Mod.EventHandler
    public void init(@Nonnull FMLInitializationEvent event) {
        Arrays.asList(manager, renderEngine).forEach(MinecraftForge.EVENT_BUS::register);
        ClientCommandHandler.instance.registerCommand(new LootrunCommand(this));
    }

    @Mod.EventHandler
    public void postinit(@Nonnull FMLPostInitializationEvent event) {}

    public @Nonnull LootrunManager getManager() {
        return manager;
    }

    public @Nonnull LootrunRenderEngine getRenderEngine() {
        return renderEngine;
    }

}
