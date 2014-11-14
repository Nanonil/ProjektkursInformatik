package projektkurs.cutscene;

import javax.swing.JFrame;
import javax.swing.JPanel;

import projektkurs.Main;
import projektkurs.cutscene.render.CutsceneRender;
import projektkurs.cutscene.render.CutsceneRenderHelper;
import projektkurs.lib.Integers;
import projektkurs.lib.Strings;
import projektkurs.util.Logger;
import projektkurs.world.Spielfeld;

/**
 * Managt die aktuell laufende CutScene
 */
public final class CutSceneManager {

	public static class CutSceneFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		/**
		 * Hauptkonstruktor
		 */
		public CutSceneFrame() {
			super(Strings.NAME + " - CutScene");

			JPanel panel = (JPanel) getContentPane();
			panel.setLayout(null);
			panel.setPreferredSize(currCutSceneRender.getCutSceneCanvas()
					.getPreferredSize());
			panel.add(currCutSceneRender.getCutSceneCanvas());

			setUndecorated(true);
			setResizable(false);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			pack();

		}
	}

	private static CutScene currCutScene;
	private static CutsceneRender currCutSceneRender;
	private static CutsceneRenderHelper currCutSceneRenderHelper;
	private static Spielfeld currSpielfeld;
	private static JFrame cutSceneFrame;
	private static double delta;

	private static int fps, ups;

	public static CutScene getCurrentCutScene() {
		return currCutScene;
	}

	public static CutsceneRender getCurrentCutSceneRender() {
		return currCutSceneRender;
	}

	public static CutsceneRenderHelper getCurrentCutSceneRenderHelper() {
		return currCutSceneRenderHelper;
	}

	public static Spielfeld getCurrSpielfeld() {
		return currSpielfeld;
	}

	public static double getDelta() {
		return delta;
	}

	public static int getFPS() {
		return fps;
	}

	public static int getUPS() {
		return ups;
	}

	public static boolean isRunning() {
		return currCutScene != null;
	}

	public static void startCutScene(CutScene cutScene) {

		if (!isRunning()) {
			Logger.info("Starting CutScene");
			Main.pause();
			Main.hide();

			currCutScene = cutScene;
			currCutSceneRenderHelper = new CutsceneRenderHelper();
			currCutSceneRender = new CutsceneRender();
			currSpielfeld = Main.getLevel().getCurrMap().copy();

			cutSceneFrame = new CutSceneFrame();
			cutSceneFrame.setVisible(true);

			currCutSceneRender.initBuffers();

			final double nsPerTick = 1000000000D / Integers.UPS;
			fps = 0;
			ups = Integers.UPS;
			int loops = 0, frames = 0;
			delta = 0D;
			long lastTime = System.nanoTime();
			long lastTimer = System.nanoTime();

			while (!currCutScene.isFinished()) {
				long time = System.nanoTime();
				delta += (time - lastTime) / nsPerTick;
				lastTime = time;

				while (delta >= 1) {
					loops++;
					currCutScene.update();
					Main.getRenderHelper().addRenderTick();
					delta--;
				}

				frames++;
				currCutSceneRender.update();

				if (System.nanoTime() - lastTimer >= 1000000000) {
					lastTimer += 1000000000;
					ups = loops;
					fps = frames;
					frames = 0;
					loops = 0;
				}

			}

			cutSceneFrame.dispose();
			currCutScene = null;
			cutSceneFrame = null;
			currCutSceneRenderHelper = null;
			currCutSceneRender = null;
			currSpielfeld = null;
			Main.show();
			Main.resume();
			Logger.info("Finished CutScene");
		}

	}

	private CutSceneManager() {
	}

}
