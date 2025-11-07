public abstract class Site {
    public static final double TAX_RATE = 0.13;
    protected double _units;
    protected double _rate;

    public Site(double _units, double _rate) {
        this._units = _units;
        this._rate = _rate;
    }

    public double getBillableAmount() {
        return getBaseAmount() + getTaxAmount();
    }

    protected abstract double getTaxAmount();

    protected abstract double getBaseAmount();
}
