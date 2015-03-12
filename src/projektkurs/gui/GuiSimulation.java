package projektkurs.gui;

import projektkurs.gui.element.ISimulationBoardListener;
import projektkurs.gui.element.SimulationBoard;
import projektkurs.render.Screen;
import projektkurs.simulation.AndGateRule;
import projektkurs.simulation.NotGateRule;
import projektkurs.simulation.OmniDirWireRule;
import projektkurs.simulation.OmniSourceRule;
import projektkurs.simulation.OrGateRule;
import projektkurs.simulation.SimpleWireRule;
import projektkurs.util.RenderUtil;

/**
 * Ein Simulations-GUI.
 */
public class GuiSimulation extends Gui implements ISimulationBoardListener {

    private SimulationBoard board;

    /**
     * Konstruktor.
     */
    public GuiSimulation() {
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void initGui() {
        super.initGui();
        board = new SimulationBoard(64, 64, 96, 48, 0, this);
        board.setRule(new OmniSourceRule(), 2, 2);
        for (int i = 3; i <= 30; i++) {
            board.setRule(new SimpleWireRule(true), i, 2);
        }
        board.setRule(new NotGateRule(2), 13, 2);
        board.setRule(new NotGateRule(2), 14, 2);
        board.setRule(new OmniDirWireRule(), 15, 2);
        for (int i = 3; i <= 30; i++) {
            board.setRule(new SimpleWireRule(false), 15, i);
        }

        // NOT
        board.setRule(new OmniSourceRule(), 18, 16);
        for (int i = 19; i <= 24; i++) {
            board.setRule(new SimpleWireRule(true), i, 16);
        }
        board.setRule(new NotGateRule(2), 25, 16);
        for (int i = 26; i <= 32; i++) {
            board.setRule(new SimpleWireRule(true), i, 16);
        }

        // OR
        board.setRule(new OmniSourceRule(), 18, 20);
        for (int i = 19; i <= 24; i++) {
            board.setRule(new SimpleWireRule(true), i, 20);
        }
        board.setRule(new OrGateRule(3), 25, 20);
        for (int i = 26; i <= 31; i++) {
            board.setRule(new SimpleWireRule(true), i, 20);
        }
        board.setRule(new OmniSourceRule(), 32, 20);
        for (int i = 21; i <= 24; i++) {
            board.setRule(new SimpleWireRule(false), 25, i);
        }

        // AND
        board.setRule(new OmniSourceRule(), 18, 26);
        for (int i = 19; i <= 24; i++) {
            board.setRule(new SimpleWireRule(true), i, 26);
        }
        board.setRule(new AndGateRule(3), 25, 26);
        for (int i = 26; i <= 31; i++) {
            board.setRule(new SimpleWireRule(true), i, 26);
        }
        board.setRule(new OmniSourceRule(), 32, 26);
        for (int i = 27; i <= 30; i++) {
            board.setRule(new SimpleWireRule(false), 25, i);
        }

        addElement(board);
    }

    @Override
    public void render(Screen screen) {
        RenderUtil.drawDefaultBackground(screen);
        super.render(screen);
    }

    @Override
    public void update() {
        super.update();
    }

}
