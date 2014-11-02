package projektkurs.entity;

import java.awt.image.BufferedImage;

import projektkurs.Main;
import projektkurs.lib.Integers;
import projektkurs.util.Inventory;

/**
 * Eine Spielerfigur
 * 
 * @author Niklas, Vladimir, Elena, Christopher
 * @version 11.09.2014
 */
public class Figur extends Entity {

	private Inventory inventar;

	/**
	 * Konstruktor für Figuren
	 * 
	 * @param posX
	 * @param posY
	 * @param image
	 */
	public Figur(int posX, int posY, BufferedImage image) {
		super(posX, posY, image);
		inventar = new Inventory(Integers.INVENTARGROESSE);
	}

	/**
	 * Das Inventar
	 * 
	 * @return
	 */
	public Inventory getInventory() {
		return inventar;
	}

	@Override
	public void moveBy(int dx, int dy) {
		if ((dx != 0 || dy != 0) && canMoveTo(posX + dx, posY + dy)) {
			posX += dx;
			posY += dy;
			Main.getRenderHelper().moveSight(dx, dy);
		}
	}

	@Override
	public void onCollideWith(Entity e) {
		super.onCollideWith(e);
		if (e instanceof EntityItem) {
			EntityItem item = (EntityItem) e;
			if (inventar.addItem(item.getStack())) {
				item.setDead();
			}
		}
	}
}
