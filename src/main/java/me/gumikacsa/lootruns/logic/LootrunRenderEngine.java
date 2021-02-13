package me.gumikacsa.lootruns.logic;

import me.gumikacsa.lootruns.Lootruns;
import me.gumikacsa.lootruns.structure.Lootrun;
import me.gumikacsa.lootruns.structure.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class LootrunRenderEngine {

    private Lootruns mod;
    private LootrunManager manager;
    private Minecraft minecraft;

    public LootrunRenderEngine(Lootruns mod, LootrunManager manager) {
        this.mod = mod;
        this.manager = manager;
        this.minecraft = Minecraft.getMinecraft();
        if (System.getProperty("force-gamma") != null) {
            minecraft.gameSettings.gammaSetting = 10.0f; // forces brightness to 1000%
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        WorldClient world = minecraft.world;
        EntityPlayer player = minecraft.player;
        RenderManager renderManager = minecraft.getRenderManager();
        Lootrun loaded;
        Collection<Chunk> checked = new HashSet<>();
        Collection<BlockPos> chests = new HashSet<>();
        if ((loaded = manager.loaded()) != null) {
            GlStateManager.pushMatrix();
            Point last = null;
            GlStateManager.disableTexture2D();
            GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
            GlStateManager.glLineWidth(3f);
            int offset = 0;
            long time = Sys.getTime();
            for (Point point: loaded.points) {
                if (last != null) {
                    boolean far = Math.abs(last.x - point.x) > 500 || Math.abs(last.y - point.y) > 500 || Math.abs(last.z - point.z) > 500;
                    if (!far && player.getDistance(point.x, point.y, point.z) < 256) {
                        int rainbow = Math.abs(Color.HSBtoRGB((time + offset * 50) % 2500L / 2500.0F, 0.8F, 0.8F));
                        float red = (rainbow >> 16 & 255) / 255.0F;
                        float green = (rainbow >> 8 & 255) / 255.0F;
                        float blue = (rainbow & 255) / 255.0F;
                        GlStateManager.color(red, green, blue);
                        GlStateManager.glBegin(GL11.GL_LINES);
                        GlStateManager.glVertex3f(last.x, last.y, last.z);
                        GlStateManager.glVertex3f(point.x, point.y, point.z);
                        GlStateManager.glEnd();
                        offset++;
                    }
                }
                last = point;
                Chunk core = world.getChunk(point.toBlockPosition());
                for (int x = -1; x <= 1; x ++) {
                    for (int z = -1; z <= 1; z ++) {
                        Chunk chunk = world.getChunk(core.x + x, core.z + z);
                        if (!checked.add(chunk)) continue;
                        for (Map.Entry<BlockPos, TileEntity> entry: chunk.getTileEntityMap().entrySet()) {
                            BlockPos position = entry.getKey();
                            TileEntity entity = entry.getValue();
                            if (entity instanceof TileEntityChest) {
                                chests.add(position);
                            }
                        }
                    }
                }
            }
            for (BlockPos position: chests) {
                RenderGlobal.drawBoundingBox(position.getX(), position.getY(), position.getZ(), position.getX() + 1, position.getY() + 1, position.getZ() + 1, 1f, 0f, 0f, 1f);
            }
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
        }
    }

}
