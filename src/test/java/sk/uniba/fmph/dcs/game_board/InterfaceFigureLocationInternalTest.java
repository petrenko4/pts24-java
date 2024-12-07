package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InterfaceFigureLocationInternalTest {


    class TopsideIntegrationFactory {
        public InterfaceFigureLocationInternal initializeForFigureLocationInternalTest(String what, ToolMakerHutFields toolMakerHutFields){
            PlaceOnFieldsAdaptor placeOnFieldsAdaptor = new PlaceOnFieldsAdaptor(toolMakerHutFields);
            PlaceOnHutAdaptor placeOnHutAdaptor = new PlaceOnHutAdaptor(toolMakerHutFields);
            PlaceOnToolMakerAdaptor placeOnToolMakerAdaptor = new PlaceOnToolMakerAdaptor(toolMakerHutFields);
            ResourceSource resourceSource = new ResourceSource("clay", Effect.CLAY, 7, 4);
            return switch (what) {
                case "f" -> placeOnFieldsAdaptor;
                case "h" -> placeOnHutAdaptor;
                case "t" -> placeOnToolMakerAdaptor;
                case "r" -> resourceSource;
                default -> null;
            };
        }
    }

    private TopsideIntegrationFactory factory;
    private ToolMakerHutFields toolMakerHutFields;
    private InterfaceFigureLocationInternal placeOnHutAdaptor;
    private InterfaceFigureLocationInternal placeOnFieldsAdaptor;
    private InterfaceFigureLocationInternal placeOnToolMakerAdaptor;
    private InterfaceFigureLocationInternal resourceSource;
    private Player player;
    private ArrayList<Effect> funcRes;

    @Before
    public void setUp() {
        factory = new TopsideIntegrationFactory();
        toolMakerHutFields = mock(ToolMakerHutFields.class);
        placeOnHutAdaptor = factory.initializeForFigureLocationInternalTest("h", toolMakerHutFields);
        placeOnFieldsAdaptor = factory.initializeForFigureLocationInternalTest("f", toolMakerHutFields);
        placeOnToolMakerAdaptor = factory.initializeForFigureLocationInternalTest("t", toolMakerHutFields);
        resourceSource = factory.initializeForFigureLocationInternalTest("r", toolMakerHutFields);
        player = mock(Player.class);
        funcRes = new ArrayList<>();
    }
    //testing makeAction(), placeFigures(), tryToMakeAction()
    @Test
    //everything is true
    public void actionDoneWhenAllConditionsMetH() {
        when(toolMakerHutFields.actionHut(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnHut(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnToolMaker(player)).thenReturn(true);

        assertEquals(ActionResult.ACTION_DONE, placeOnHutAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, placeOnHutAdaptor.tryToMakeAction(player));
        assertTrue(placeOnHutAdaptor.placeFigures(player, 4));
    }

    //something fails (each test something different)
    @Test
    public void actionDoneWhenOneConditionFailsH() {

        when(toolMakerHutFields.actionHut(player)).thenReturn(false);
        when(toolMakerHutFields.canPlaceOnHut(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnToolMaker(player)).thenReturn(true);

        assertEquals(ActionResult.FAILURE, placeOnHutAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, placeOnHutAdaptor.tryToMakeAction(player));
        assertTrue(placeOnHutAdaptor.placeFigures(player, 4));
    }

    @Test
    public void actionDoneWhenOneConditionFailsH1() {

        when(toolMakerHutFields.actionHut(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnHut(player)).thenReturn(false);
        when(toolMakerHutFields.canPlaceOnToolMaker(player)).thenReturn(true);

        assertEquals(ActionResult.ACTION_DONE, placeOnHutAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, placeOnHutAdaptor.tryToMakeAction(player));
        assertFalse(placeOnHutAdaptor.placeFigures(player, 4));
    }

    @Test
    public void actionDoneWhenOneConditionFailsH2() {

        when(toolMakerHutFields.actionHut(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnHut(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnToolMaker(player)).thenReturn(false);

        assertEquals(ActionResult.FAILURE, placeOnHutAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, placeOnHutAdaptor.tryToMakeAction(player));
        assertTrue(placeOnHutAdaptor.placeFigures(player, 4));
    }









    //testing makeAction(), tryToPlaceFigures()
    @Test
    //everything is true
    public void actionDoneWhenAllConditionsMetF() {
        when(toolMakerHutFields.actionFields(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnFields(player)).thenReturn(true);
        when(player.playerBoard()).thenReturn(mock(InterfacePlayerBoardGameBoard.class));
        when(player.playerBoard().hasFigures(anyInt())).thenReturn(true);

        assertEquals(ActionResult.ACTION_DONE, placeOnFieldsAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, placeOnFieldsAdaptor.tryToPlaceFigures(player, 1));
    }

    //something fails (each test something different)
    @Test
    public void actionDoneWhenOneConditionFailsF() {

        when(toolMakerHutFields.actionFields(player)).thenReturn(false);
        when(toolMakerHutFields.canPlaceOnFields(player)).thenReturn(true);
        when(player.playerBoard()).thenReturn(mock(InterfacePlayerBoardGameBoard.class));
        when(player.playerBoard().hasFigures(anyInt())).thenReturn(true);

        assertEquals(ActionResult.FAILURE, placeOnFieldsAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, placeOnFieldsAdaptor.tryToPlaceFigures(player, 1));

    }

    @Test
    public void actionDoneWhenOneConditionFailsF1() {

        when(toolMakerHutFields.actionFields(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnFields(player)).thenReturn(false);
        when(player.playerBoard()).thenReturn(mock(InterfacePlayerBoardGameBoard.class));
        when(player.playerBoard().hasFigures(anyInt())).thenReturn(true);

        assertEquals(ActionResult.ACTION_DONE, placeOnFieldsAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, placeOnFieldsAdaptor.tryToPlaceFigures(player, 1));

    }

    @Test
    public void actionDoneWhenOneConditionFailsF2() {

        when(toolMakerHutFields.actionFields(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnFields(player)).thenReturn(true);
        when(player.playerBoard()).thenReturn(mock(InterfacePlayerBoardGameBoard.class));
        when(player.playerBoard().hasFigures(anyInt())).thenReturn(true);

        assertEquals(ActionResult.ACTION_DONE, placeOnFieldsAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, placeOnFieldsAdaptor.tryToPlaceFigures(player, 2));

    }
    //quite similar tests to T
}
