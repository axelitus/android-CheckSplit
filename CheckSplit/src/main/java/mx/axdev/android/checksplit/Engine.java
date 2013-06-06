package mx.axdev.android.checksplit;

/**
 * Created by Axel on 06/06/2013.
 */
public class Engine {
    // region Attributes
    protected float check_total = 0;
    protected int split_count = 1;
    protected float discount_percent = 0;       // Expressed as a float between 0 and 1
    protected float already_paid = 0;
    protected float tip_percent = 0;            // Expressed as a float between 0 and 1
    protected CalculateTipOptions calculate_tip_option = CalculateTipOptions.BEFORE_DISCOUNT;
    protected RoundToOptions round_to_option = RoundToOptions.NONE;

    protected float amount_per_split = 0;
    protected float total_to_pay = 0;
    // endregion

    // region Enums
    public enum CalculateTipOptions {
        BEFORE_DISCOUNT,
        AFTER_DISCOUNT,
        AFTER_DISCOUNT_AND_ALREADY_PAID;

        public static CalculateTipOptions fromInteger(int i) {
            switch(i) {
                case 0:
                    return BEFORE_DISCOUNT;
                case 1:
                    return AFTER_DISCOUNT;
                case 2:
                    return AFTER_DISCOUNT_AND_ALREADY_PAID;
            }

            return null;
        }
    }

    public enum RoundToOptions {
        NONE,
        ONE_UNIT,
        FIVE_UNITS,
        TEN_UNITS;

        public static RoundToOptions fromInteger(int i) {
            switch(i) {
                case 0:
                    return NONE;
                case 1:
                    return ONE_UNIT;
                case 2:
                    return FIVE_UNITS;
                case 3:
                    return TEN_UNITS;
            }

            return null;
        }

        public static int toValue(RoundToOptions option) {
            switch(option) {
                case NONE:
                    return 0;
                case ONE_UNIT:
                    return 1;
                case FIVE_UNITS:
                    return 5;
                case TEN_UNITS:
                    return 10;
            }

            return 0;
        }
    }
    // endregion

    // region Constructors
    public Engine() {
        this(0);
    }

    public Engine(float check_total) {
        this.check_total = check_total;
    }
    // endregion

    // region Getters
    public float getCheckTotal() {
        return this.check_total;
    }

    public int getSplitCount() {
        return this.split_count;
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

    public CalculateTipOptions getCalculateTipOption() {
        return this.calculate_tip_option;
    }

    public RoundToOptions getRoundToOption() {
        return this.round_to_option;
    }

    public float getAmountPerSplit() {
        return this.amount_per_split;
    }

    public float getTotalToPay() {
        return this.total_to_pay;
    }
    // endregion

    // region Setters
    public void setCheckTotal(float value) {
        this.check_total = value;
        this.calculate();
    }

    public void setSplitCount(int value) {
        this.split_count = Math.max(value, 1);
        this.calculate();
    }

    public void setDiscountPercent(float value) {
        this.discount_percent = value;
        this.calculate();
    }

    public void setAlreadyPaid(float value) {
        this.already_paid = value;
        this.calculate();
    }

    public void setTipPercent(float value) {
        this.tip_percent = value;
        this.calculate();
    }

    public void setCalculateTipOption(CalculateTipOptions value) {
        this.calculate_tip_option = value;
        this.calculate();
    }

    public void setRoundToOption(RoundToOptions value) {
        this.round_to_option = value;
        this.calculate();
    }
    // endregion

    // region Methods
    public void splitCountIncrease() {
        this.split_count = Math.min(this.split_count + 1, Integer.MAX_VALUE);
        this.calculate();
    }

    public void splitCountDecrease() {
        this.split_count = Math.max(this.split_count - 1, 0);
        this.calculate();
    }

    public void calculate() {
        // Apply discount
        float sub_total = this.check_total * (1 - this.discount_percent);

        // Calculate tip and apply already paid
        switch(this.calculate_tip_option) {
            case BEFORE_DISCOUNT:
                sub_total += (this.check_total * this.tip_percent);
                sub_total -= this.already_paid;
                break;
            case AFTER_DISCOUNT_AND_ALREADY_PAID:
                sub_total -= this.already_paid;
                sub_total += (sub_total * this.tip_percent);
                break;
            case AFTER_DISCOUNT:
                sub_total += (sub_total * this.tip_percent);
                sub_total -= this.already_paid;
                break;
        }

        // Apply rounding
        this.amount_per_split = sub_total / this.split_count;
        int round_factor = RoundToOptions.toValue(this.round_to_option);
        if(round_factor > 0) {
            this.amount_per_split = (float) Math.ceil(this.amount_per_split / round_factor) * round_factor;
        }

        // Calculate total to pay
        this.total_to_pay = this.amount_per_split * this.split_count;
    }
    // endregion
}
