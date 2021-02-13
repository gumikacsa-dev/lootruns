package me.gumikacsa.lootruns.logic;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import me.gumikacsa.lootruns.Lootruns;
import me.gumikacsa.lootruns.structure.Lootrun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LootrunManager {

    private Lootruns mod;
    private Gson gson;
    private Map<String, Lootrun> lootruns;
    private Lootrun loadedLootrun;

    public LootrunManager(@Nonnull Lootruns mod) {
        this.mod = mod;
        this.gson = new Gson();
        this.lootruns = new ConcurrentHashMap<>();
    }

    public Lootrun load(@Nullable String name) {
        if (name == null) {
            this.loadedLootrun = null;
            return null;
        }
        Lootrun lootrun = null;
        if ((lootrun = fetch(name, true)) == null) throw new IllegalStateException("That lootrun does not exist!");
        if (loadedLootrun == lootrun) throw new IllegalStateException("That lootrun is already loaded.");
        this.loadedLootrun = lootrun;
        return lootrun;
    }

    public @Nullable Lootrun fetch(@Nonnull String name, boolean load) {
        Preconditions.checkNotNull(name, "name must not be null");
        Lootrun current = lootruns.get(name.toLowerCase());
        if (load && current == null) {
            File folder = new File("lootruns");
            if (folder.exists() || folder.mkdir()) {
                File file = new File(folder, name + ".json");
                if (file.exists()) {
                    try (Reader reader = new FileReader(file)) {
                        Lootrun lootrun = gson.fromJson(reader, Lootrun.class);
                        lootruns.put(name.toLowerCase(), current = lootrun);
                    } catch (IOException exception) {
                        System.err.println("Exception parsing lootrun");
                        exception.printStackTrace();
                    }
                }
            }
        }
        return current;
    }

    public @Nullable Lootrun loaded() {
        return loadedLootrun;
    }

}
