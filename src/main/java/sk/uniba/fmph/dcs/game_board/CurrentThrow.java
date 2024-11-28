package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;

public class CurrentThrow implements InterfaceToolUse {
    private Effect throwsFor;
    private int throwResult;
    private Player player;
    private int dices;
    private InterfacePlayerBoardGameBoard playerBoard;
    private int sumScoreOfDices;


    public void initiate(Player player, Effect effect, int dices){
        this.player = player;
        throwsFor = effect;
        this.dices = dices;
        this.playerBoard = this.player.playerBoard();
        sumScoreOfDices = sumDices();
        if(!canUseTools()){
            sumScoreOfDices /= throwsFor.points();
        }
    }

    private int sumDices(){
        Throw throw_ = new Throw();
        int[] score = throw_.throw_(dices);
        int finalScore = 0;
        for (int i = 0; i < score.length; i++) {
            finalScore += score[i];
        }

        return finalScore;
    }

    public String state(){

        return "";
    }

    @Override
    public boolean useTool(int idx) {
        playerBoard.useTool(idx);

        return false;
    }

    @Override
    public boolean canUseTools() {
        return false;
    }

    @Override
    public boolean finishUsingTools() {
        return false;
    }
}
