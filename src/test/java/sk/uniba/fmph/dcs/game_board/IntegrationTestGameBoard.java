package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntegrationTestGameBoard {

    class TopsideFactory {
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

    @Test
    public void interfaceFigureLocationInternalTest() {
        TopsideFactory factory = new TopsideFactory();
        ToolMakerHutFields toolMakerHutFields = mock(ToolMakerHutFields.class);
        InterfaceFigureLocationInternal placeOnHutAdaptor = factory.initializeForFigureLocationInternalTest("h", toolMakerHutFields);
        Player player = mock(Player.class);
        //if all conditions are met
        when(toolMakerHutFields.actionHut(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnHut(player)).thenReturn(true);
        when(toolMakerHutFields.canPlaceOnToolMaker(player)).thenReturn(true);
        ArrayList<Effect> funcRes = new ArrayList<>();
        assertEquals(ActionResult.ACTION_DONE, placeOnHutAdaptor.makeAction(player, new ArrayList<>(), funcRes));
        // if ...
        when(toolMakerHutFields.canPlaceOnFields(player)).thenReturn(false);
        assertEquals(ActionResult.ACTION_DONE, placeOnHutAdaptor.makeAction(player, new ArrayList<>(), funcRes));
    }
}
