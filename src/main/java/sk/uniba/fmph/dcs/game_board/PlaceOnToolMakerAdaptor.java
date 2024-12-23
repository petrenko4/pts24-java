package sk.uniba.fmph.dcs.game_board;


import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;


public class PlaceOnToolMakerAdaptor implements InterfaceFigureLocationInternal {
    private final ToolMakerHutFields toolMaker;

    /**
     * Constructs a new {@code PlaceOnToolMakerAdaptor} with the specified {@link ToolMakerHutFields}.
     *
     * @param toolMakerHutFields using methods from ToolMakerHutsFields class
     */
    public PlaceOnToolMakerAdaptor(final ToolMakerHutFields toolMakerHutFields){
        this.toolMaker = toolMakerHutFields;
    }

    /**
     * Attempts to place the specified number of figures on the ToolMaker for the given player.
     * If successful, the player's figures are deducted accordingly.
     *
     * @param player the player attempting to place figures
     * @param figureCount the number of figures to place
     * @return {@code true} if the figures were successfully placed; {@code false} otherwise
     */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(tryToPlaceFigures(player, figureCount).equals(HasAction.AUTOMATIC_ACTION_DONE)){
            if(toolMaker.placeOnToolMaker(player)){
                player.playerBoard().takeFigures(figureCount);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the specified number of figures can be placed on the ToolMaker for the given player.
     *
     * @param player the player attempting to place figures
     * @param count the number of figures to check
     * @return a {@link HasAction} indicating whether the action is possible
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {

        if(!player.playerBoard().hasFigures(count)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(count != 1){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(!toolMaker.canPlaceOnToolMaker(player)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        return HasAction.AUTOMATIC_ACTION_DONE;
    }

    /**
     * Executes an action on the ToolMaker.
     *
     * @param player the player performing the action
     * @param inputResources the input resources required for the action
     * @param outputResources the output resources generated by the action
     * @return an {@link ActionResult} indicating the outcome of the action
     */
    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if(toolMaker.actionToolMaker(player)){
            return ActionResult.ACTION_DONE;
        }
        return ActionResult.FAILURE;
    }

    /**
     * Skips the current action for the given player. This implementation always returns {@code false},
     * indicating that skipping is not allowed for this location.
     *
     * @param player the player attempting to skip the action
     * @return {@code false}, as skipping is not supported
     */
    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    /**
     * Does nothing.
     *
     * @param player the player attempting to make an action
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        return null;
    }


    /**
     * Resets the ToolMaker location for a new turn. This implementation always returns {@code false},
     * indicating no reset behavior is required.
     *
     * @return {@code false}.
     */
    @Override
    public boolean newTurn() {
        return false;
    }

}