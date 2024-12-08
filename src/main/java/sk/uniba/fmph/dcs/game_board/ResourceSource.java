package sk.uniba.fmph.dcs.game_board;

import java.util.*;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

public final class ResourceSource implements InterfaceFigureLocationInternal {
    private final String name;
    private final Effect resource;
    private final int maxFigures;
    private final int maxFigureColors;
    private final ArrayList<PlayerOrder> figures;
    private final CurrentThrow currentThrow;

    public ResourceSource(String name, Effect resource, int maxFigures, int maxFigureColors) {
        if (!resource.isResourceOrFood()) {
            throw new IllegalArgumentException("Resource must be food or resource");
        }
        this.name = name;
        this.resource = resource;
        this.maxFigures = maxFigures;
        this.maxFigureColors = maxFigureColors;
        this.figures = new ArrayList<>();
        currentThrow = new CurrentThrow();
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        // Check if player can place figures here
        if (!canPlaceFigures(player, figureCount)) {
            return false;
        }

        // Add the figures
        for (int i = 0; i < figureCount; i++) {
            figures.add(player.playerOrder());
        }
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (!player.playerBoard().hasFigures(count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if (canPlaceFigures(player, count)) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if (!figures.contains(player.playerOrder())) {
            return ActionResult.FAILURE;
        }
        currentThrow.initiate(player, resource, Collections.frequency(figures, player.playerOrder()));
        currentThrow.finishUsingTools();
                                                                 //not sure how this works. When do players have to use their tools?
                                                                 //trying to use all arguments even if it's  not supposed to be like this
  /*                                                             //just for the sake of more complex and engaging testing
        if(currentThrow.canUseTools()){
            return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
        }
    */
        List<Effect>  resEffects = currentThrow.effects;
        outputResources.addAll(resEffects);

        return ActionResult.ACTION_DONE;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if (figures.contains(player.playerOrder())) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean skipAction(Player player) {
        return false; // Can't skip resource gathering
    }

    @Override
    public boolean newTurn() {
        figures.clear();
        return false; // Resource sources don't trigger game end
    }

    private boolean canPlaceFigures(Player player, int figureCount) {
        // Check if player has enough figures
        if (!player.playerBoard().hasFigures(figureCount)) {
            return false;
        }

        // Check if space available
        if (figures.size() + figureCount > maxFigures) {
            return false;
        }

        // Check if player already has figures here
        if (figures.contains(player.playerOrder())) {
            return false;
        }

        // Check number of different players
        Set<PlayerOrder> set = new HashSet<>(figures);
        return set.size() < maxFigureColors;
    }

    public String state() {
        Map<String, Object> state = Map.of(
                "name", name,
                "resource", resource,
                "maxFigures", maxFigures,
                "maxFigureColors", maxFigureColors,
                "figures", figures.stream().map(PlayerOrder::getOrder).toList()
        );
        return new JSONObject(state).toString();
    }
}
