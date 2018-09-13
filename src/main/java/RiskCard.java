public class RiskCard {
    enum RISK_CARD_TYPE {
        INFANTRY, CAVALRY, ARTILLERY
    }
//this assures that each card can only be of one type
    public RiskCard(Territory territory, RISK_CARD_TYPE type) {
        this.territory = territory;
        this.type = type;
    }

    final RISK_CARD_TYPE type;
    Territory territory;

    RiskCard(RISK_CARD_TYPE type) {
        this.type = type;
    }
}