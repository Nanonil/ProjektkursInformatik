package projektkurs.story.trigger;

import projektkurs.Main;
import projektkurs.item.ItemStack;

/**
 * Prüft, ob sich ein bestimmter ItemStack im Spielerinventar befindet.
 */
public class InventoryHasItemStackTrigger extends Trigger {

    /**
     * Der ItemStack.
     */
    private final ItemStack stack;

    /**
     * Konstruktor.
     *
     * @param stack
     *            zu prüfender ItemStack.
     */
    public InventoryHasItemStackTrigger(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean isTriggerActive() {
        return Main.getPlayer().getInventory().containsIgnoreStackSize(stack);
    }

}