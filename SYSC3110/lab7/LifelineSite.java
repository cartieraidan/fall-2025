public class LifelineSite extends Site {

    public LifelineSite(int units, double rate) {
        super(units, rate);
    }

    public LifelineSite() {
        this(5, 11.3);
    }


    @Override
    protected double getTaxAmount() {
        return getBaseAmount() * Site.TAX_RATE * 0.2;
    }

    @Override
    protected double getBaseAmount() {
        return _units * _rate * 0.5;
    }
}
