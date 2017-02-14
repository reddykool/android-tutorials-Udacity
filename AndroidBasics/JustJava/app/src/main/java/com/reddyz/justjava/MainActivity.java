/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 *
 * package com.example.android.justjava;
 */
package com.reddyz.justjava;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.NotActiveException;
import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int numberOfCoffees = 0;
    private final static int coffeePrice = 10;

    boolean hasWhipCream = false;
    int whipCreamPrice = 1;

    boolean hasChocolate = false;
    int chocoPrice = 2;

    EditText nameInputText;
    CheckBox creamCheckbox;
    CheckBox chocCheckbox;
    TextView orderSummaryTextView;
    TextView quantityTextView;

    String orderMsg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processXmlViews();
    }

    private void processXmlViews() {
        nameInputText = (EditText) findViewById(R.id.name_input_text);
        creamCheckbox = (CheckBox) findViewById(R.id.WhipCream_checkbox);
        chocCheckbox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
    }

    private String createOrderSummary(int totalPrice, String name) {
        String msg;
        msg =  getString(R.string.order_summary_name, name) + "\n";
        msg += getString(R.string.order_summary_quantity, numberOfCoffees) + "\n" ;
        msg += getString(R.string.order_summary_coffee_price, NumberFormat.getCurrencyInstance().format(coffeePrice)) + "\n";
        if(hasWhipCream)
            msg += getString(R.string.order_summary_wc_price, NumberFormat.getCurrencyInstance().format(whipCreamPrice)) + "\n";
        if(hasChocolate)
            msg += getString(R.string.order_summary_choc_price, NumberFormat.getCurrencyInstance().format(chocoPrice)) + " \n";
        msg += getString(R.string.order_summary_total, NumberFormat.getCurrencyInstance().format(totalPrice)) + "\n";
        msg += "\n" + getString(R.string.thank_you);

        return msg;
    }

//    private String createOrderSummary(int totalPrice, String name) {
//        String msg;
//        msg =  getString(R.string.order_summary_name) + name + "\n";
//        msg += "Quantity     : " + numberOfCoffees + "\n" ;
//        msg += " + Coffee($" + coffeePrice + " each)\n";
//        if(hasWhipCream)
//            msg += " + Whipped Cream($" + whipCreamPrice + " each)\n";
//        if(hasChocolate)
//            msg += " + Chocolate($" + chocoPrice + " each)\n";
//        msg += "\n TOTAL        : $" + totalPrice + "\n";
//        msg += "\n" + R.string.thank_you;
//
//        return msg;
//    }

    private int calculatePrice(int quantity, int price) {
        int totalPrice;
        totalPrice = quantity*price;
        if(hasWhipCream)
            totalPrice += quantity*whipCreamPrice;
        if(hasChocolate)
            totalPrice += quantity*chocoPrice;
        return totalPrice;
    }

    public void increment(View view) {
        //Maximum 100 cups per order :)
        if(numberOfCoffees <100) {
            numberOfCoffees++;
            display(numberOfCoffees);
        }else
            Toast.makeText(this, getString(R.string.maxLimit), Toast.LENGTH_SHORT).show();
    }

    public void decrement(View view) {
        // Lets not go negetive on quantity :)
        if(numberOfCoffees > 1) {
            numberOfCoffees--;
            display(numberOfCoffees);
        }else
            Toast.makeText(this, getString(R.string.minOrder), Toast.LENGTH_SHORT).show();
    }

    /**
     * This method displays the Order Summary on the screen.
     */
    private void displayMessage(String msg) {
        orderSummaryTextView.setText(msg);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        quantityTextView.setText("" + number);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void processOrder(View view) {
        if(numberOfCoffees == 0) {
            Toast.makeText(this, R.string.toast_select_quantity, Toast.LENGTH_SHORT).show();
            return;
        }

        String name = nameInputText.getText().toString();
        if(name.isEmpty()) {
            Toast.makeText(this, R.string.toast_enter_name, Toast.LENGTH_SHORT).show();
            return;
        }

        hasWhipCream = creamCheckbox.isChecked();
        hasChocolate = chocCheckbox.isChecked();

        int totalPrice = calculatePrice(numberOfCoffees, coffeePrice);
        orderMsg = createOrderSummary(totalPrice, name);

        displayMessage(orderMsg);
    }

    public void resetOrder(View view) {
        hasChocolate = hasWhipCream = false;
        creamCheckbox.setChecked(false);
        chocCheckbox.setChecked(false);
        nameInputText.setText("");
        orderSummaryTextView.setText("");
        quantityTextView.setText("0");
        numberOfCoffees = 0;
        orderMsg="";
    }

    public void mailOrder(View view) {
        if(orderMsg.isEmpty())
        {
            Toast.makeText(this, R.string.toast_checkout, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent orderMailIntent = new Intent(Intent.ACTION_SENDTO);
        orderMailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        orderMailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject_text) + numberOfCoffees);
        orderMailIntent.putExtra(Intent.EXTRA_TEXT, orderMsg);
        //orderMailIntent.setType("plain/text");

        //Two ways to start an intent with correct handling.
        //1. Try-catch to handle the exception from intended application(if any)
        try {
            //startActivity(Intent.createChooser(orderMailIntent, "Mail Via...")); // This also works...
            startActivity(orderMailIntent);
            Toast.makeText(this, R.string.mail_success_msg, Toast.LENGTH_SHORT).show();
        }catch (ActivityNotFoundException ex) {
            Toast.makeText(this, R.string.mail_fail_msg, Toast.LENGTH_SHORT).show();
        }

        //2. pre check if the intended application is available to process request.
//        if(orderMailIntent.resolveActivity(getPackageManager()) != null) {
//            startActivity(orderMailIntent);
//            Toast.makeText(this,"Processing Order!", Toast.LENGTH_SHORT).show();
//        } else {
//            //startActivity(Intent.createChooser(orderMailIntent, "Mail Via..."));
//            Toast.makeText(this, "Error : Processing Order!", Toast.LENGTH_SHORT).show();
//        }

    }
}