package projektkurs.story.trigger;

import java.lang.reflect.Method;

import projektkurs.Main;
import projektkurs.item.ItemStack;

public class InventoryTrigger extends Trigger {

	private final ItemStack item;

	public InventoryTrigger(Method m, ItemStack item) {
		super(m);
		this.item = item;
	}

	@Override
	public boolean isTriggerActive() {
		return Main.getPlayer().getInventory().containsIgnoreStackSize(item);
	}

}