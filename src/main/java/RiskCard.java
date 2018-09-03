public class RiskCard {
    enum RISK_CARD_TYPE {
        INFANTRY, CAVALRY, ARTILLERY
    }
//this assures that each card can only be of one type
    final RISK_CARD_TYPE type;

    RiskCard(RISK_CARD_TYPE type) {
        this.type = type;
    }
}