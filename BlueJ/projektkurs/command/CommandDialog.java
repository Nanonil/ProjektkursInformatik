package projektkurs.command;

import projektkurs.Main;
import projektkurs.dialog.Dialog;
import projektkurs.gui.GuiDialogChooser;
import projektkurs.lib.Dialoge;

/**
 * Das Dialog-Start-Kommando.
 */
public class CommandDialog implements ICommand {

    @Override
    public EnumCommandResult execute(String[] args) {

        if (args.length != 1) {
            return EnumCommandResult.WRONG_USAGE;
        }

        Dialog dialog = Dialoge.MAPPINGS.get(args[0]);
        if (dialog == null) {
            return EnumCommandResult.OBJECT_NOT_FOUND;
        }

        Main.openGui(new GuiDialogChooser(dialog, Main.getPlayer()));

        return EnumCommandResult.SUCCESS;
    }

    @Override
    public String getCommand() {
        return "dialog";
    }

}