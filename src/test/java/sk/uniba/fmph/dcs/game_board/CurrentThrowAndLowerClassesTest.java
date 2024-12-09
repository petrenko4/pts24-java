package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CurrentThrowAndLowerClassesTest {

    private GetSomethingThrow somethingThrow;
    private GetCard getCard;
    private CivilizationCardDeck civilizationCardDeck;
    private EvaluateCivilizationCardImmediateEffect eval;
    private CivilizationCardPlace civilizationCardPlace;

    public class InitializationFactory{

        public void init(int param, PlayerOrder playerOrder, CivilizationCard card, CurrentThrow currentThrow) {
            Stack<CivilizationCard> stack = new Stack<>();
            stack.push(card);
            civilizationCardDeck = new CivilizationCardDeck(stack);
            switch (param){
                case 0:{
                    somethingThrow = new GetSomethingThrow(Effect.FOOD, currentThrow);
                    break;
                }
                case 1: {
                    getCard = new GetCard(civilizationCardDeck);
                    break;
                }
            }
            if (param == 1){
                civilizationCardPlace = new CivilizationCardPlace(0, List.of(playerOrder),
                        civilizationCardDeck, getCard);
            } else {
                civilizationCardPlace = new CivilizationCardPlace(0, List.of(playerOrder),
                        civilizationCardDeck, somethingThrow);
            }


        }
    }

    @Test
    public void GetCardIntegrationTest(){
        InitializationFactory fact = new InitializationFactory();
        Player player = mock(Player.class);
        when(player.playerOrder()).thenReturn(mock(PlayerOrder.class));
        when(player.playerBoard()).thenReturn(mock(InterfacePlayerBoardGameBoard.class));
        when(player.playerBoard().takeResources(List.of())).thenReturn(true);
        CivilizationCard card = mock(CivilizationCard.class);
        when(card.getImmediateEffectType()).thenReturn(List.of(ImmediateEffect.FOOD, ImmediateEffect.CLAY));
        fact.init(1, player.playerOrder(), card, null);
        List<Effect> inputResources = new ArrayList<>();
        List<Effect> outputResources = new ArrayList<>();
        civilizationCardPlace.makeAction(player, inputResources, outputResources);

        assertEquals(List.of(Effect.FOOD, Effect.CLAY), outputResources);

    }
    @Test
    public void GetSomethingThrowTest(){
        InitializationFactory fact = new InitializationFactory();

        CurrentThrow currentThrow = mock(CurrentThrow.class);
        Player player = mock(Player.class);
        when(player.playerOrder()).thenReturn(mock(PlayerOrder.class));
        fact.init(1, player.playerOrder(), null, currentThrow);
        GetSomethingThrow somethingThrow = new GetSomethingThrow(Effect.FOOD, currentThrow);
        somethingThrow.performEffect(player, Effect.FOOD);
        verify(currentThrow).initiate(player, Effect.FOOD, 2);
        /* this test fails
        verify(currentThrow).initiate(player, Effect.CLAY, 2); */
    }
}
