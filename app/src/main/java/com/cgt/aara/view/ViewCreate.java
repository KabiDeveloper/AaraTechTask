package com.cgt.aara.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.cgt.aara.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ViewCreate {

    private int paddingTen = 10;


    public EditText createEditText(Context context, ViewGroup parentLayout, String hint) {
        EditText editText = new EditText(context);

        try {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/poppins_medium.ttf");

            // Set the custom font to the EditText
            editText.setTypeface(customFont);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    610, 130
            );
            params.setMargins(paddingTen, paddingTen, paddingTen, paddingTen);
            editText.setLayoutParams(params);
            editText.setHint(hint);
            editText.setPadding(paddingTen, paddingTen, paddingTen, paddingTen);
            editText.setTextSize(15);

            editText.setBackgroundResource(R.drawable.bg_edittext); // Set the custom background drawable


            if (hint.equals(context.getResources().getString(R.string.date_of_birth))) {
                // Prevent manual editing of the date field
                editText.setKeyListener(null);
                hideKeyboard(context, editText);
                // Add click listener to show date picker dialog
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog(context, editText);
                    }
                });

            } else {
                showKeyboard(context, editText);
            }
            parentLayout.addView(editText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return editText;

    }

    private void showDatePickerDialog(Context context, final EditText editText) {
        try {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year1, month1, dayOfMonth) -> {
                        // Update the Date of Birth EditText with the selected date
                        calendar.set(Calendar.YEAR, year1);
                        calendar.set(Calendar.MONTH, month1);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String selectedDate = dateFormat.format(calendar.getTime());
                        editText.setText(selectedDate);
                    }, year, month, day);

            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextView createTextView(Context context, ViewGroup parentLayout, String textValue) {
        TextView textView = new TextView(context);
        try {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/poppins_semi_bold.ttf");
            textView.setTypeface(customFont);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 130
            );
            params.setMargins(paddingTen, paddingTen, paddingTen, paddingTen);
            textView.setLayoutParams(params);
            textView.setText(textValue);
            textView.setTextColor(ContextCompat.getColor(context, R.color.red));
            textView.setTextSize(24);
            textView.setGravity(Gravity.CENTER);


            textView.setPadding(paddingTen, paddingTen, paddingTen, paddingTen);


            parentLayout.addView(textView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textView;
    }

    public ImageView createImageView(Context context, ViewGroup parentLayout, String contentDescription) {
        ImageView imageView = new ImageView(context);
        try {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 300);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            imageView.setLayoutParams(params);
            params.setMargins(paddingTen, paddingTen, paddingTen, paddingTen);

            imageView.setImageResource(R.drawable.ic_profile_pic);
            imageView.setPadding(0, 16, 0, 16);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setContentDescription(contentDescription);

            parentLayout.addView(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }

    public Spinner createSpinner(Context context, ViewGroup parentLayout, String hint) {
        TextView labelTextView = new TextView(context);
        Spinner spinner = new Spinner(context);

        try {
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            labelParams.setMargins(0, paddingTen, 0, paddingTen); // Set margins for the label (left, top, right, bottom)
            labelTextView.setLayoutParams(labelParams);
            labelTextView.setText(context.getResources().getString(R.string.income_salary)); // Set the label text
            parentLayout.addView(labelTextView);

            LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                    600,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            spinner.setLayoutParams(spinnerParams);
            spinner.setPrompt(hint);

            // Set the custom background drawable
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                spinner.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_spinner));
            } else {
                spinner.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_spinner));
            }

            parentLayout.addView(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spinner;
    }

    public ImageView createSortImageView(Context context, LinearLayout llyImage, ViewGroup parentLayout, String contentDescription) {

        TextView labelTextView = new TextView(context);
        ImageView imageView = new ImageView(context);

        try {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/poppins_semi_bold.ttf");
            // Set the custom font to the EditText
            labelTextView.setTypeface(customFont);
            llyImage.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            llyImage.setOrientation(LinearLayout.HORIZONTAL);
            llyImage.setPadding(16, 16, 16, 16);
            llyImage.setGravity(Gravity.END);
            llyImage.setWeightSum(2.0f);
            labelTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2.0f // Set the weight for the first TextView
            ));
            labelTextView.setText(context.getResources().getString(R.string.customer_list));
            labelTextView.setTextSize(18);
            labelTextView.setTextColor(ContextCompat.getColor(context, R.color.white));

            llyImage.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_lly));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
            imageView.setLayoutParams(params);
            params.setMargins(paddingTen, paddingTen, paddingTen, paddingTen);

            imageView.setImageResource(R.drawable.ic_sort);
            imageView.setPadding(0, 16, 0, 16);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setContentDescription(contentDescription);
            llyImage.addView(labelTextView);
            llyImage.addView(imageView);
            parentLayout.addView(llyImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }

    public Button createButton(Context context, ViewGroup parentLayout, String text) {
        Button button = new Button(context);
        try {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(paddingTen, paddingTen, paddingTen, paddingTen);

            params.gravity = Gravity.CENTER_HORIZONTAL;
            button.setLayoutParams(params);
            button.setBackgroundResource(R.drawable.bg_button); // Set the custom background drawable
            button.setText(text);
            button.setTextColor(ContextCompat.getColor(context, R.color.white));
            parentLayout.addView(button);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return button;
    }

    private void hideKeyboard(Context context, EditText editText) {
        try {
            if (editText != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showKeyboard(Context context, EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
