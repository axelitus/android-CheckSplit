package mx.axdev.android.checksplit;

/**
 * Created by Axel on 06/06/2013.
 */
public class Engine {
    //region Attributes
    protected float check_total = 0;
    protected int person_count = 1;
    protected float discount_percent = 0;     // Expressed as a float between 0 and 1
    protected float already_paid = 0;
    protected float tip_percent = 0;          // Expressed as a float between 0 and 1
    //endregion

    //region Enums

    public enum CalculateTipOptions {
        BEFORE_DISCOUNTS,
        AFTER_DISCOUNTS
    }

    public enum RoundToOptions {
        NONE,
        ONE_UNIT,
        FIVE_UNIT,
        TEN_UNIT
    }

    //endregion

    //region Constructors

    public Engine() {
        this(0);
    }

    public Engine(float check_total) {
        this.check_total = check_total;
    }

    //endregion

    //region Getters

    public float getCheckTotal() {
        return this.check_total;
    }

    public int getPersonCount() {
        return this.person_count;
    }

    public float getDiscountPercent() {
        return this.discount_percent;
    }

    public float getAlreadyPaid() {
        return this.already_paid;
    }

    public float getTipPercent() {
        return this.tip_percent;
    }

    //endregion

    //region Setters

    public void setCheckTotal(float value) {
        this.check_total = value;
    }

    public void setPersonCount(int value) {
        this.person_count = Math.max(value, 0);
    }

    public void setDiscountPercent(float value) {
        this.discount_percent = value;
    }

    public void setAlreadyPaid(float value) {
        this.already_paid = value;
    }

    public void setTipPErcent(float value) {
        this.tip_percent = value;
    }

    //endregion

    //region Methods
    public void personCountIncrease() {
        this.person_count = Math.min(this.person_count + 1, Integer.MAX_VALUE);
    }

    public void personCountDecrease() {
        this.person_count = Math.max(this.person_count - 1, 0);
    }
    //endregion
}
