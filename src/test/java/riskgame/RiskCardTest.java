package riskgame;

import org.testng.annotations.Test;
import riskgame.gameobject.RiskCard;
import riskgame.gameobject.Territory;
import riskgame.gameobject.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RiskCardTest {
    @Test
    public void isValidCardCombinationTest() throws Exception {
        Player Evelyn = new Player();
        RiskCard rc1 = new RiskCard(new Territory(Evelyn), RiskCard.RISK_CARD_TYPE.ARTILLERY);
        RiskCard rc2 = new RiskCard(new Territory(Evelyn), RiskCard.RISK_CARD_TYPE.ARTILLERY);
        RiskCard rc3 = new RiskCard(new Territory(Evelyn), RiskCard.RISK_CARD_TYPE.CAVALRY);
        List<RiskCard> cardList = new ArrayList<>();
        Collections.addAll(cardList,rc1,rc2,rc3);
        assertFalse(Evelyn.isValidCardCombination(cardList));

    }

    @Test
    public void isValidCardCombinationTest2() throws Exception {
        Player p2 = new Player();
        RiskCard rc1 = new RiskCard(new Territory(p2), RiskCard.RISK_CARD_TYPE.WILD);
        RiskCard rc2 = new RiskCard(new Territory(p2), RiskCard.RISK_CARD_TYPE.ARTILLERY);
        RiskCard rc3 = new RiskCard(new Territory(p2), RiskCard.RISK_CARD_TYPE.CAVALRY);
        List<RiskCard> cardList = new ArrayList<>();
        Collections.addAll(cardList, rc1, rc2, rc3);
        assertTrue(p2.isValidCardCombination(cardList));
    }


}
