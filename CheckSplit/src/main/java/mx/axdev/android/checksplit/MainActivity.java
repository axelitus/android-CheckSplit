package mx.axdev.android.checksplit;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class MainActivity extends Activity {
    // region Attributes

    // region Widgets

    // region Labels
    protected TextView lbl_discount_value;
    protected TextView lbl_tip_value;
    protected TextView lbl_amount_per_split_value;
    protected TextView lbl_total_to_pay;
    // endregion

    // region Textboxes
    protected EditText txt_check_total;
    protected EditText txt_split_count;
    protected EditText txt_already_paid;
    // endregion

    // region Buttons
    protected Button btn_split_count_minus;
    protected Button btn_split_count_plus;
    // endregion

    // region Seekbars
    protected SeekBar skb_discount;
    protected SeekBar skb_tip;
    // endregion

    // region Spinners
    protected Spinner dpd_calculate_tip_options;
    protected Spinner dpd_round_to_options;
    // endregion

    // endregion

    protected Engine engine;

    // region Formatters
    protected DecimalFormat decimal_formatter;
    protected DecimalFormat currency_formatter;
    // endregion

    // region Format Control
    protected boolean check_total_formatting = false;
    protected boolean split_count_formatting = false;
    protected boolean already_paid_formatting = false;
    // endregion

    // endregion

    // region Activity events
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        //return true;
        return false;
    }
    // endregion

    protected void initialize() {
        // Create the engine
        this.engine = new Engine();

        // Register the activity widgets we need to interact with
        this.register_widgets();

        // Setup the number formatters
        this.setup_formatters();

        // Setup widget event listeners
        this.setup_listeners();

        // Display the default results
        this.display_results();
    }

    protected void register_widgets() {
        this.txt_check_total = (EditText) findViewById(R.id.main_txt_check_total);
        this.btn_split_count_minus = (Button) findViewById(R.id.main_btn_split_minus);
        this.txt_split_count = (EditText) findViewById(R.id.main_txt_split_count);
        this.btn_split_count_plus = (Button) findViewById(R.id.main_btn_split_plus);
        this.skb_discount = (SeekBar) findViewById(R.id.main_skb_discount);
        this.lbl_discount_value = (TextView) findViewById(R.id.main_lbl_discount_value);
        this.txt_already_paid = (EditText) findViewById(R.id.main_txt_already_paid);
        this.skb_tip = (SeekBar) findViewById(R.id.main_skb_tip);
        this.lbl_tip_value = (TextView) findViewById(R.id.main_lbl_tip_value);
        this.dpd_calculate_tip_options = (Spinner) findViewById(R.id.main_dpd_calculate_tip);
        this.dpd_round_to_options = (Spinner) findViewById(R.id.main_dpd_round_to);
        this.lbl_amount_per_split_value = (TextView) findViewById(R.id.main_lbl_amount_per_person_value);
        this.lbl_total_to_pay = (TextView) findViewById(R.id.main_lbl_total_to_pay_value);
    }

    protected void setup_formatters() {
        // Setup decimal formatter
        try {
            DecimalFormatSymbols decimal_format_symbols = new DecimalFormatSymbols();
            decimal_format_symbols.setDecimalSeparator('.');
            decimal_format_symbols.setMonetaryDecimalSeparator('.');
            decimal_format_symbols.setGroupingSeparator(',');

            this.decimal_formatter = new DecimalFormat("#,##0.00", decimal_format_symbols);
        } catch(Exception ex) {
            this.decimal_formatter = (DecimalFormat) NumberFormat.getNumberInstance();
        }

        // Setup currency formatter
        try {
            DecimalFormatSymbols currency_format_symbols = new DecimalFormatSymbols();
            currency_format_symbols.setCurrencySymbol("$ ");
            currency_format_symbols.setInternationalCurrencySymbol("$ ");
            currency_format_symbols.setDecimalSeparator('.');
            currency_format_symbols.setMonetaryDecimalSeparator('.');
            currency_format_symbols.setGroupingSeparator(',');

            this.currency_formatter = new DecimalFormat("$ #,##0.00", currency_format_symbols);
        } catch(Exception ex) {
            this.currency_formatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
        }
    }

    protected void setup_listeners() {
        // region txt_check_total
        this.txt_check_total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!check_total_formatting) {
                    try {
                        engine.setCheckTotal(Float.parseFloat(editable.toString()));
                    } catch(Exception ex) {
                        engine.setCheckTotal(0);
                    }
                    display_results();
                }

                check_total_formatting = false;
            }
        });

        this.txt_check_total.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                check_total_formatting = true;
                DecimalFormat formatter;
                if(hasFocus) {
                    formatter = decimal_formatter;
                } else {
                    formatter = currency_formatter;
                }

                txt_check_total.setText(((engine.getCheckTotal() > 0)? formatter.format(engine.getCheckTotal()) : "" ));
            }
        });
        // endregion

        // region txt_split_count
        this.txt_split_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!split_count_formatting) {
                    try {
                        engine.setSplitCount(Integer.parseInt(editable.toString()));
                    } catch (Exception ex) {
                        engine.setSplitCount(0);
                    }

                    display_results();
                }

                split_count_formatting = false;
            }
        });
        // endregion

        // region btn_split_count_minus
        this.btn_split_count_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.splitCountDecrease();
                split_count_formatting = true;
                txt_split_count.setText(Integer.toString(engine.getSplitCount()));

                display_results();
            }
        });
        // endregion

        // region btn_split_count_plus
        this.btn_split_count_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.splitCountIncrease();
                split_count_formatting = true;
                txt_split_count.setText(Integer.toString(engine.getSplitCount()));

                display_results();
            }
        });
        // endregion

        // region skb_discount
        this.skb_discount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                engine.setDiscountPercent(i / 100f);
                lbl_discount_value.setText(Integer.toString(i) + "%");
                display_results();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // endregion

        // region txt_already_paid
        this.txt_already_paid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!already_paid_formatting) {
                    try {
                        engine.setAlreadyPaid(Float.parseFloat(editable.toString()));
                    } catch(Exception ex) {
                        engine.setAlreadyPaid(0);
                    }

                    display_results();
                }

                already_paid_formatting = false;
            }
        });

        this.txt_already_paid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                already_paid_formatting = true;
                DecimalFormat formatter;
                if(hasFocus) {
                    formatter = decimal_formatter;
                } else {
                    formatter = currency_formatter;
                }

                txt_already_paid.setText(((engine.getAlreadyPaid() > 0)? formatter.format(engine.getAlreadyPaid()) : "" ));
            }
        });
        // endregion

        // region for skb_tip
        this.skb_tip.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                engine.setTipPercent(i / 100f);
                lbl_tip_value.setText(Integer.toString(i) + "%");
                display_results();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // endregion

        // region dpd_calculate_tip_options
        this.dpd_calculate_tip_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                engine.setCalculateTipOption(Engine.CalculateTipOptions.fromInteger(position));
                display_results();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // endregion

        // region dpd_round_to_options
        this.dpd_round_to_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                engine.setRoundToOption(Engine.RoundToOptions.fromInteger(position));
                display_results();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // endregion
    }

    protected void display_results() {
        this.lbl_amount_per_split_value.setText(this.monetize(engine.getAmountPerSplit()));
        this.lbl_total_to_pay.setText(this.monetize(engine.getTotalToPay()));
    }

    protected String monetize(float result) {
        if(currency_formatter == null) {
            this.setup_formatters();
        }

        return currency_formatter.format(result);
    }
}
