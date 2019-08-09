package android.example.theaterapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //This global variable will be used to keep track of the quantity of popcorn tubs ordered
    private int popcornQuantity = 0;

    //This global variable will be used to keep track of the quantity of sodas ordered
    private int sodaQuantity = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign the Respective view(s) to a variable
        Button popcornDecrementButton = findViewById(R.id.popcorn_decrement_button);

        Button popcornIncrementButton = findViewById(R.id.popcorn_increment_button);

        Button sodaDecrementButton = findViewById(R.id.soda_decrement_button);

        Button sodaIncrementButton = findViewById(R.id.soda_increment_button);

        Button submitOrderButton = findViewById(R.id.submit_order_button);

        Button submitOrderSummaryViaEmail = findViewById(R.id.submit_order_button_via_email);


        //Attach an OnClickListener to all of the appropriate views
        popcornDecrementButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //When the button is pressed that it
                if(popcornQuantity == 0){
                    //No need for an error message. Just don't allow the popcornQuantity variable counter to go below the value of zero
                    //Thus so that no negative value in turn is to not be displayed to the user of the program
                    //Do not decrease the popcornQuantity variable counter (Thus ending the method early)
                    return;
                }

                //Decrement popcornQuantity  value
                popcornQuantity--;

                //display the updated popcornQuantity value to the user (which has been decreased of course)
                displayPopcornQuantity();
            }
        });

        popcornIncrementButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( popcornQuantity >= 5){
                    //Let the user known that only 5 tubs of popcorn can be ordered per customer order
                    Toast.makeText(MainActivity.this, "Only 5 tubs per customer order", Toast.LENGTH_SHORT).show();

                    //do not increment the popcorn quantity
                    return;
                }
                //If the limit order of popcorn tubs has not been reached updated the popcornQuantity counter
                popcornQuantity++;

                //display the updated popcornQuantity value to the user (which has been increased of course)
                displayPopcornQuantity();
            }
        });

        sodaDecrementButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sodaQuantity == 0){
                    return;
                }

                //Decrement the quantity value
                sodaQuantity--;

                //Display the updated Soda quantity to the user
                displaySodaQuantity();
            }
        });

        sodaIncrementButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sodaQuantity == 10){
                    //Let the user know that only ten sodas can be ordered per customer order
                    Toast.makeText(MainActivity.this, "Only 10 sodas can be ordered per customer order", Toast.LENGTH_SHORT).show();

                    //end this method early to prevent the user from decreasing the value of the sodaQuantity variable
                    return;
                }

                //Decrement the sodaQuantity value
                sodaQuantity++;

                //Display to the user the updated sodaQuantity value
                displaySodaQuantity();
            }
        });

        submitOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Get the name that the user entered and assign to a variable
                EditText nameEditText = findViewById(R.id.name_field);
                String name = nameEditText.getText().toString();

                //**** Begin by getting the value of the CheckBoxes, meaning let the program kniw if the user checked a certain CheckBox(You know, the program doesn't)
                //--ButterCheck Box
                CheckBox butterCheckBox = findViewById(R.id.butter_checkbox);
                Boolean hasButter = butterCheckBox.isChecked();

                String butterToppingResponse;
                if(hasButter){
                    butterToppingResponse = "Yes";
                }
                else{
                    butterToppingResponse = "No";
                }

                //--Jalapenos CheckBox
                CheckBox jalapenosCheckBox = findViewById(R.id.jalapenos_checkbox);
                boolean hasJalapenos = jalapenosCheckBox.isChecked();

                String jalapenosToppingResponse;
                if(hasJalapenos){
                    jalapenosToppingResponse = "Yes";
                }
                else{
                    jalapenosToppingResponse = "No";
                }

                //Calculate the the total price
                int price = calculatePrice(hasButter, hasJalapenos);

                //Create the order summary given all the things the user entered or picked
                String orderSummary = createOrderSummary(name, butterToppingResponse, jalapenosToppingResponse, price);

                //Display the order summary to the user
                displayOrderSummary(orderSummary);
            }
        });

        submitOrderSummaryViaEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Get the name that the user entered and assign to a variable
                EditText nameEditText = findViewById(R.id.name_field);
                String name = nameEditText.getText().toString();

                //**** Begin by getting the value of the CheckBoxes, meaning let the program kniw if the user checked a certain CheckBox(You know, the program doesn't)
                //--ButterCheck Box
                CheckBox butterCheckBox = findViewById(R.id.butter_checkbox);
                Boolean hasButter = butterCheckBox.isChecked();

                String butterToppingResponse;
                if(hasButter){
                    butterToppingResponse = "Yes";
                }
                else{
                    butterToppingResponse = "No";
                }

                //--Jalapenos CheckBox
                CheckBox jalapenosCheckBox = findViewById(R.id.jalapenos_checkbox);
                boolean hasJalapenos = jalapenosCheckBox.isChecked();

                String jalapenosToppingResponse;
                if(hasJalapenos){
                    jalapenosToppingResponse = "Yes";
                }
                else{
                    jalapenosToppingResponse = "No";
                }

                //Calculate the the total price
                int price = calculatePrice(hasButter, hasJalapenos);

                //Create the order summary given all the things the user entered or picked
                String orderSummary = createOrderSummary(name, butterToppingResponse, jalapenosToppingResponse, price);


                //******* These next few lines of code creates and intent and is sent to an app that can handle the intent. The intent is sent only if there is an app tha can handle it(Execute the desired task requested)
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                //intent.putExtra(Intent.EXTRA_EMAIL, addresses); //This Line of code specifies the to: field of the email to be composed (But will not be used for this application
                intent.putExtra(Intent.EXTRA_SUBJECT, "Theater items order for " + name);//This line of code specified the subject of email being composed
                intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
                //This selection statement determines if there is an app(app activity to be specific) that exists on the device, that can handle the intent
                //But note, if there is not an app can handle the intent to be sent. Than this block of code will be skipped thus, the intent will not be sent(even tho it was created)
                //Therefore, if there is an app on the device that can handle the intent than the if statement's code will execute
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);//This code statement enable the intent to be sent
                }

            }
        });

    }


    private void displayPopcornQuantity(){
        TextView popcornQuantityTextView = findViewById(R.id.popcorn_quantity_text_view);
        popcornQuantityTextView.setText("" + popcornQuantity);
    }

    private void displaySodaQuantity(){
        TextView sodaTextView = findViewById(R.id.soda_text_view);
        sodaTextView.setText("" + sodaQuantity);
    }



    private int calculatePrice(boolean hasButter, boolean hasJalapenos){
        //Calculate the base price for a butter with the additional toppings if included (this goes for all popcorn tubs ordered)
        int popcornBasePrice = 5;

        //Add additional topping price(Butter Topping) to popcorn base price if user checked it
        if(hasButter){
            popcornBasePrice += 2;
        }
        //Add additional topping price(Jalapenos Topping) to popcorn base price if user checked it
        if(hasJalapenos){
            popcornBasePrice += 1;
        }

        //Calculate how many tubs of popcorn the customer wanted
        int totalPopcornPrice = popcornBasePrice * popcornQuantity;



        //Set the bas price for a Soda
        int sodaBasePrice = 3;

        //Calculate how many sodas the user wanted
        int totalSodaPrice = sodaBasePrice * sodaQuantity;

        //Return to the user the total price that the user will be charged
        return totalPopcornPrice + totalSodaPrice;
    }


    /**This method creates the order summary given everything the user entered or picked
     *
     * @param name respresents the name of the user
     * @param butterToppingResponse tells you if the user wanted this topping, in the form a response
     * @param jalapenosToppingResponse tells you if the user wanted this topping, in the form a response
     * @return this code statement returns the order summary created to the caller
     */
    private String createOrderSummary(String name, String butterToppingResponse, String jalapenosToppingResponse, int price){
        String summary = "Name: " + name;
        summary += "\n\nPopcorn quantity: " + popcornQuantity;
        summary += "\nCharge for Butter Topping: " + butterToppingResponse;
        summary += "\nCharge for Jalapenos Topping: " + jalapenosToppingResponse;
        summary += "\n\nSoda quantity: " + sodaQuantity;
        summary += "\n\nTotal Price: $" + price;

        return summary;
    }



    //This method is used to display the order summary to the user when called
    private void displayOrderSummary(String orderSummary){
        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(orderSummary);
    }

}
