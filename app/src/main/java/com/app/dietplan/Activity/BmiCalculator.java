package com.app.dietplan.Activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dietplan.R;
import com.app.dietplan.Util.Method;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BmiCalculator extends AppCompatActivity {

    private float height = 0, weight = 0;
    private TextView textView_male, textView_female;
    private ImageView imageView_male, imageView_female;
    private boolean isMale = true;
    private Animation myAnim;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        Method.forceRTLIfSupported(getWindow(), BmiCalculator.this);

        final Method method = new Method(BmiCalculator.this);
        method.setStatusBarGradiant();

        myAnim = AnimationUtils.loadAnimation(BmiCalculator.this, R.anim.bounce);

        Toolbar toolbar = findViewById(R.id.toolbar_bmi);
        toolbar.setTitle(getResources().getString(R.string.bmi_calculator));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RulerValuePicker rulerValuePicker_height = findViewById(R.id.ruler_picker_height_bmi);
        RulerValuePicker rulerValuePicker_weight = findViewById(R.id.ruler_picker_weight_bmi);
        Button button_bmi = findViewById(R.id.button_bmi);

        LinearLayout linearLayout = findViewById(R.id.linearLayout_bmi);
        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, BmiCalculator.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, BmiCalculator.this);
        }

        rulerValuePicker_height.selectValue(140);//set selected value height
        rulerValuePicker_weight.selectValue(40);//set selected value weight

        textView_male = findViewById(R.id.textView_male_bmi);
        textView_female = findViewById(R.id.textView_female_bmi);
        imageView_male = findViewById(R.id.imageView_male_bmi);
        imageView_female = findViewById(R.id.imageView_female_bmi);
        final LinearLayout linearLayout_male = findViewById(R.id.linearLayout_male_bmi);
        final LinearLayout linearLayout_female = findViewById(R.id.linearLayout_female_bmi);

        rulerValuePicker_height.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                height = selectedValue;
                //Value changed and the user stopped scrolling the ruler.
                //Application can consider this value as final selected value.
            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                //Value changed but the user is still scrolling the ruler.
                //This value is not final value. Application can utilize this value to display the current selected value.
            }
        });

        rulerValuePicker_weight.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                weight = selectedValue;
                //Value changed and the user stopped scrolling the ruler.
                //Application can consider this value as final selected value.
            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                //Value changed but the user is still scrolling the ruler.
                //This value is not final value. Application can utilize this value to display the current selected value.
            }
        });

        linearLayout_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_male.startAnimation(myAnim);
                isMale = true;
                textView_male.setTextColor(getResources().getColor(R.color.white));
                imageView_male.setImageDrawable(getResources().getDrawable(R.drawable.male_se));
                textView_female.setTextColor(getResources().getColor(R.color.textView_bmi));
                imageView_female.setImageDrawable(getResources().getDrawable(R.drawable.female_un));
                linearLayout_male.setBackground(getResources().getDrawable(R.drawable.button_gender_bg));
                linearLayout_female.setBackground(getResources().getDrawable(R.drawable.button_unselect_gender_bg));
            }
        });

        linearLayout_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_female.startAnimation(myAnim);
                isMale = false;
                textView_male.setTextColor(getResources().getColor(R.color.textView_bmi));
                imageView_male.setImageDrawable(getResources().getDrawable(R.drawable.male_un));
                textView_female.setTextColor(getResources().getColor(R.color.white));
                imageView_female.setImageDrawable(getResources().getDrawable(R.drawable.female_se));
                linearLayout_male.setBackground(getResources().getDrawable(R.drawable.button_unselect_gender_bg));
                linearLayout_female.setBackground(getResources().getDrawable(R.drawable.button_gender_bg));
            }
        });

        button_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (height <= 0) {
                    method.alertBox(getResources().getString(R.string.please_select_height));
                } else if (weight <= 0) {
                    method.alertBox(getResources().getString(R.string.please_select_weight));
                } else {

                    //formula bmi calculation
                    float heightValue = height / 100;
                    float weightValue = weight;
                    final float bmi = weightValue / (heightValue * heightValue);

                    final Dialog dialog = new Dialog(BmiCalculator.this, R.style.Theme_AppCompat_Translucent);
                    dialog.setContentView(R.layout.bmi_result_dialog);

                    TextView textView_score = dialog.findViewById(R.id.textView_scoreBmi_dialog);
                    TextView textView_status = dialog.findViewById(R.id.textView_bmiStatus_dialog);
                    ImageView imageView_close = dialog.findViewById(R.id.imageView_close_dialog);
                    Button button_tryAgain = dialog.findViewById(R.id.button_tryAgain_dialog);
                    Button button_share = dialog.findViewById(R.id.button_share_dialog);

                    button_tryAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    button_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            method.share_bmi(String.valueOf(bmi), displayBMI(bmi));
                        }
                    });

                    textView_score.setText(String.valueOf(bmi));
                    textView_status.setText(displayBMI(bmi));

                    imageView_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }

            }
        });


    }

    //get bmi result
    private String displayBMI(float bmi) {

        if (isMale) {
            if (bmi < 18.5) {
                return getResources().getString(R.string.Underweight);
            } else if (bmi < 25) {
                return getResources().getString(R.string.Normal);
            } else if (bmi < 30) {
                return getResources().getString(R.string.Overweight);
            } else {
                return getResources().getString(R.string.Obese);
            }
        } else {
            if (bmi < 16.5) {
                return getResources().getString(R.string.Underweight);
            } else if (bmi < 22) {
                return getResources().getString(R.string.Normal);
            } else if (bmi < 27) {
                return getResources().getString(R.string.Overweight);
            } else {
                return getResources().getString(R.string.Obese);
            }
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
