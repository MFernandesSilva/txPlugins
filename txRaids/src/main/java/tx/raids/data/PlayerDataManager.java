package tx.raids.data;

import com.sk89q.worldedit.internal.gson.Gson;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {
    private Map<String, PlayerData> playerDataMap;
    private File dataFile;

    public PlayerDataManager(File dataFile) {
        this.playerDataMap = new HashMap<>();
        this.dataFile = dataFile;
        loadPlayerData();
    }

    public void loadPlayerData() {
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            String json = new String(Files.readAllBytes(Paths.get(dataFile.getPath())));
            // Use uma biblioteca JSON para deserializar (ex: Gson)
            Gson gson = new Gson();
            PlayerData[] data = gson.fromJson(json, PlayerData[].class);
            for (PlayerData playerData : data) {
                 playerDataMap.put(playerData.getUuid(), playerData);
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerData() {
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (PlayerData data : playerDataMap.values()) {
            // Use uma biblioteca JSON para serializar (ex: Gson)
            // jsonBuilder.append(gson.toJson(data)).append(",");
        }
        jsonBuilder.append("]");
        try {
            Files.write(Paths.get(dataFile.getPath()), jsonBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayerData(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!playerDataMap.containsKey(uuid)) {
            playerDataMap.put(uuid, new PlayerData(uuid, player.getName()));
        }
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId().toString());
    }
}
