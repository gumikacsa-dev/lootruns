package me.gumikacsa.lootruns.logic;

import me.gumikacsa.lootruns.Lootruns;
import me.gumikacsa.lootruns.structure.Lootrun;
import me.gumikacsa.lootruns.structure.Point;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class LootrunCommand extends CommandBase implements IClientCommand {

    private Lootruns mod;

    public LootrunCommand(@Nonnull Lootruns mod) {
        this.mod = mod;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String... parameters) throws CommandException {
        if (parameters.length > 0) {
            if (parameters[0].equalsIgnoreCase("load")) {
                if (parameters.length < 2) {
                    throw new WrongUsageException("lootrun load <filename>");
                }
                try {
                    Lootrun lootrun = mod.getManager().load(parameters[1]);
                    for (Point first: lootrun.points) {
                        sender.sendMessage(new TextComponentString("Lootrun loaded. First point is at " + (first.x + ", " + first.y + ", " + first.z) + "."));
                        break;
                    }
                } catch (Exception exception) {
                    throw new CommandException(exception.getMessage());
                }
                return;
            }
            if (parameters[0].equalsIgnoreCase("clear")) {
                mod.getManager().load(null);
                sender.sendMessage(new TextComponentString("Path cleared."));
                return;
            }
        }
        throw new WrongUsageException(getUsage(sender));
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    public @Nonnull String getName() {
        return "lootruns";
    }

    @Override
    public @Nonnull List<String> getAliases() {
        return Arrays.asList("lootrun", "loot", "lr");
    }

    @Override
    public @Nonnull String getUsage(ICommandSender sender) {
        return "/lootruns <load|clear>";
    }

}
