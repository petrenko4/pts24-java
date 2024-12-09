package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.ImmediateEffect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetCard implements  EvaluateCivilizationCardImmediateEffect {
    private CivilizationCardDeck deck;

    public GetCard(CivilizationCardDeck deck) {
        this.deck = deck;
    }

    //Player gets endOfGameEffect from a card on top of the deck
    @Override
    public boolean performEffect(Player player, Effect effect) {
        Optional<CivilizationCard> card = deck.getTop();

        if (card.isPresent()) {
            List<Effect> convert = new ArrayList<>();
            for(ImmediateEffect xd: card.get().getImmediateEffectType()){               // testing on food, dont care
                convert.add(Effect.FOOD);
            }
            player.playerBoard().giveEffect(convert);   // endgame effect, let it be here
            return true;
        }
        return false;

    }
}
