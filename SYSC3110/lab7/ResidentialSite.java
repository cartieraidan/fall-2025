public class ResidentialSite extends Site {

    public ResidentialSite(int units, double rate) {
        super(units, rate);
    }

    public ResidentialSite() {
        this(5, 11.3);
    }


    @Override
    protected double getTaxAmount() {
        return getBaseAmount() * Site.TAX_RATE;
    }

    @Override
    protected double getBaseAmount() {
        return _units * _rate;
    }
}
