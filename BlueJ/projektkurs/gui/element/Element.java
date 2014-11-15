package projektkurs.gui.element;

import java.awt.Graphics2D;

public abstract class Element {

	protected int posX, posY, sizeX, sizeY, id;

	public Element(int posX, int posY, int sizeX, int sizeY, int id) {
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public boolean isInside(Element e) {
		return isInside(e.posX, e.posY, e.sizeX, e.sizeY);
	}

	public boolean isInside(int posX, int posY) {
		return isInside(posX, posY, 1, 1);
	}

	public boolean isInside(int posX, int posY, int sizeX, int sizeY) {
		return (Math.max(posX, this.posX) < Math.min((posX + sizeX),
				(this.posX + this.sizeX)))
				&& ((Math.max(posY, this.posY) < Math.min((posY + sizeY),
						(this.posY + this.sizeY))));
	}

	public void onKeyPressed(int key) {
		// NO-OP
	}

	public void onLeftClick(int x, int y) {
		// NO-OP
	}

	public void onRightClick(int x, int y) {
		// NO-OP
	}

	public abstract void render(Graphics2D g);

}