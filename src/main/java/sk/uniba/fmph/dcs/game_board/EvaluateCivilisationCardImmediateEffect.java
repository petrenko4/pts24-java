package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

public interface EvaluateCivilisationCardImmediateEffect {
    boolean performEffect(Player player, Effect choice);
}