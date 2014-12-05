package projektkurs.gui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import projektkurs.Main;
import projektkurs.gui.element.Button;
import projektkurs.gui.element.IButtonListener;
import projektkurs.gui.element.TextView;
import projektkurs.lib.KeyBindings;
import projektkurs.util.I18n;

/**
 * Das Sprachauswahl-GUI.
 */
public class GuiLangChooser extends Gui implements IButtonListener {

    /**
     * Eltern-Gui.
     */
    private final Gui parent;

    /**
     * Konstruktor.
     *
     * @param parent
     *            Eltern-Gui
     */
    public GuiLangChooser(Gui parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        addElement(new TextView(34, 34, 256, 64, 0, "info.lang.current", I18n.getLocale()));
        Button b;
        for (int i = 0; i < I18n.getSupportedLocales().length; i++) {
            b = new Button(34, 34 + (i + 1) * 64, 256, 64, i + 1, this, I18n.getSupportedLocales()[i].getUnlocalizedName());
            if (I18n.getSupportedLocales()[i].equals(I18n.getLocale())) {
                b.setEnabled(false);
            }
            addElement(b);
        }
    }

    @Override
    public void onButtonLeftClick(Button button, MouseEvent e) {
        I18n.changeLocale(I18n.getSupportedLocales()[button.getID() - 1]);
        Main.openGui(parent);
    }

    @Override
    public void onButtonRightClick(Button button, MouseEvent e) {
        // NO-OP
    }

    @Override
    public void onKeyTyped(char keyChar, KeyEvent e) {
        super.onKeyTyped(keyChar, e);
        if (keyChar == KeyBindings.KEY_OPTION) {
            Main.openGui(parent);
        }
    }

}