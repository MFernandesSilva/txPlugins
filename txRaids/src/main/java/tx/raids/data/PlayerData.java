package tx.raids.data;

public class PlayerData {
    private String uuid; // UUID do jogador
    private String name; // Nome do jogador
    private int totalRaids; // Total de raids completadas
    private int easyRaids; // Total de raids fáceis completadas
    private int mediumRaids; // Total de raids médias completadas
    private int hardRaids; // Total de raids difíceis completadas
    private int nightmareRaids; // Total de raids de pesadelo completadas

    public PlayerData(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.totalRaids = 0;
        this.easyRaids = 0;
        this.mediumRaids = 0;
        this.hardRaids = 0;
        this.nightmareRaids = 0;
    }

    // Métodos para incrementar contagens
    public void incrementTotalRaids() {
        totalRaids++;
    }

    public void incrementEasyRaids() {
        easyRaids++;
    }

    public void incrementMediumRaids() {
        mediumRaids++;
    }

    public void incrementHardRaids() {
        hardRaids++;
    }

    public void incrementNightmareRaids() {
        nightmareRaids++;
    }

    // Getters
    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getTotalRaids() {
        return totalRaids;
    }

    public int getEasyRaids() {
        return easyRaids;
    }

    public int getMediumRaids() {
        return mediumRaids;
    }

    public int getHardRaids() {
        return hardRaids;
    }

    public int getNightmareRaids() {
        return nightmareRaids;
    }
}