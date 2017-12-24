package olof.sjoholm.game.shared;

public interface Damageable {

    int getCurrentHealth();

    int getMaxHealth();

    void damage(int damagePoints);
}
