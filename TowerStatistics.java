package fi.joutsijoki;

/**
 * Created by Sami on 25.1.2016.
 */
public class TowerStatistics {

    public static Statistic getStatisticsObject(AssetLoader.TOWER_TEXTURE texture) {
        switch (texture) {
            case SHADOW:
                return new Statistic(20, 5, 4, 500);
            case LIGHTNING:
                return new Statistic(60, 10, 6, 2000);
            case ICE:
                return new Statistic(40, 3, 3, 1000);
            case POISON:
                return new Statistic(100, 3, 5, 1500);
            case SHADOW_MAGE_IDLE:
                return new Statistic(80, 10, 5, 50);
            case DWARF_IDLE:
                return new Statistic(200, 20, 10, 200);
            default:
                return null;
        }
    }

    public static class Statistic {
        public int cost;
        public int damage;      // Tower damage per each projectile
        public int radius;      // The range of the tower radius in cells
        public int cooldown;    // The time in milliseconds between each shot

        public Statistic(int cost, int damage, int radius, int cooldown) {
            this.cost = cost;
            this.damage = damage;
            this.radius = radius;
            this.cooldown = cooldown;
        }
    }
}
