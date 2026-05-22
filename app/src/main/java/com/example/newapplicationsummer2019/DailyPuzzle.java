package com.example.newapplicationsummer2019;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class DailyPuzzle extends AppCompatActivity {
    Button guessButton; //stores reference to button for the whole class
    Button newRound;
    EditText guess;
    TextView guessNumberTV;
    TextView elapsedTimeTV;
    int mysteryNumber;
    int guessNumber = 1;
    int minutes = 0;
    int seconds = 0;
    String modMinutes;
    String modSeconds;
    TimerTask waitTimer;
    Boolean timerBool = false;
    int gameCounter = 0;
    TextView firstCell;
    TextView secondCell;
    TextView thirdCell;
    int tempGuess;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView testTV;
    String mnFromDB;
    int count = 0;
    private AdView dpBanner;
    private InterstitialAd dpInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_puzzle);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        dpBanner = findViewById(R.id.dpBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        dpBanner.loadAd(adRequest);
     /* OUTDATED CODE, LOOK AT ClassicMode.java for new code

        dpInterstitialAd = new InterstitialAd(this);
        dpInterstitialAd.setAdUnitId("ca-app-pub-8356994243159260/5272864671");
        dpInterstitialAd.loadAd(new AdRequest.Builder().build());
        dpInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                dpInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });*/
        guessButton = findViewById((R.id.guessButton));
        guessButton.setEnabled(false);
        //get reference to button
        newRound = findViewById((R.id.startButton));

        //change the buttons caption
        //(usually done in the XML or via a string resource)
        //attach the click listener
        guessButton.setOnClickListener(new HandleClick());
        newRound.setOnClickListener(new HandleClick2());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("dp");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("a", "Value is: " + value);
                mnFromDB = value;
                if (count == 10) {
                    alertView("New Daily Puzzle", "A new daily puzzle is available. You'll be switched over to that puzzle.");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("e", "Failed to read value.", error.toException());
            }
        });


    }

    class HandleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            guessNumberTV = findViewById((R.id.guessNumberTV));
            //Toast.makeText(getApplicationContext(), "guess button pressed", Toast.LENGTH_SHORT ).show();
            guess = findViewById((R.id.guess));
            String formatted = guess.getText().toString();
            if (formatted.length() == 3) {
                if (Integer.parseInt(formatted) > 100) {
                    if (guessNumber <= 6) {
                        int a = Integer.parseInt(String.valueOf(guess.getText()));
                        tempGuess = Integer.parseInt(String.valueOf(guess.getText()));
                        int b = mysteryNumber;
                        int digitsCorrect = 0;
                        int placesCorrect = 0;
                        guessNumber = guessNumber + 1;
                        if (a == b) {
                            timerBool = false;
                            placesCorrect = 3;
                            digitsCorrect = 3;
                            guessButton.setEnabled(false);
                            alertView("Congrats!", "The mystery number was " + mysteryNumber + ". You guessed it in " + (guessNumber - 1) + " guess(es)");
                          /*  if (dpInterstitialAd.isLoaded()) {
                                dpInterstitialAd.show();
                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                            }*/
                        } else {
                            List<Integer> mn = new ArrayList<Integer>();
                            List<Integer> gu = new ArrayList<Integer>();
                            int d1 = guess.getText().toString().charAt(0);
                            int d2 = guess.getText().toString().charAt(1);
                            int d3 = guess.getText().toString().charAt(2);
                            int mn1 = Integer.toString(mysteryNumber).charAt(0);
                            int mn2 = Integer.toString(mysteryNumber).charAt(1);
                            int mn3 = Integer.toString(mysteryNumber).charAt(2);
                            mn.add(mn1);
                            mn.add(mn2);
                            mn.add(mn3);
                            gu.add(d1);
                            gu.add(d2);
                            gu.add(d3);
                            for (int i = 1; i < 4; i++) {
                                if (mn.get(i - 1) == gu.get(i - 1)) {
                                    digitsCorrect = digitsCorrect + 1;
                                    placesCorrect = placesCorrect + 1;
                                } else {
                                    if (mn.indexOf(gu.get(i - 1)) != -1) {
                                        digitsCorrect = digitsCorrect + 1;
                                    }
                                }
                            }
                            //Toast.makeText(getApplicationContext(), "Digits Correct: " + Integer.toString(digitsCorrect) + " Places Correct: " + Integer.toString(placesCorrect), Toast.LENGTH_LONG).show();
                            if (guessNumber != 7) {
                                guessNumberTV.setText("Guess Number: " + guessNumber);
                            }
                            if (guessNumber == 6) {
                                Toast.makeText(getApplicationContext(), "Last Guess!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        guess.setText("");
                        if (guessNumber == 2) {
                            firstCell = findViewById(R.id.oneone);
                            secondCell = findViewById(R.id.onetwo);
                            thirdCell = findViewById(R.id.onethree);
                        } else if (guessNumber == 3) {
                            firstCell = findViewById(R.id.twoone);
                            secondCell = findViewById(R.id.twotwo);
                            thirdCell = findViewById(R.id.twothree);
                        } else if (guessNumber == 4) {
                            firstCell = findViewById(R.id.threeone);
                            secondCell = findViewById(R.id.threetwo);
                            thirdCell = findViewById(R.id.threethree);
                        } else if (guessNumber == 5) {
                            firstCell = findViewById(R.id.fourone);
                            secondCell = findViewById(R.id.fourtwo);
                            thirdCell = findViewById(R.id.fourthree);
                        } else if (guessNumber == 6) {
                            firstCell = findViewById(R.id.fiveone);
                            secondCell = findViewById(R.id.fivetwo);
                            thirdCell = findViewById(R.id.fivethree);
                        } else if (guessNumber == 7) {
                            firstCell = findViewById(R.id.sixone);
                            secondCell = findViewById(R.id.sixtwo);
                            thirdCell = findViewById(R.id.sixthree);
                        }
                        firstCell.setText("(" + (guessNumber - 1) + ") " + tempGuess);
                        secondCell.setText(Integer.toString(digitsCorrect));
                        thirdCell.setText(Integer.toString(placesCorrect));
                        if (guessNumber == 7 && placesCorrect != 3) {
                            AlertDialog alertDialog = new AlertDialog.Builder(DailyPuzzle.this).create();
                            alertDialog.setTitle("Out of Guesses");
                            alertDialog.setMessage("You are out of guesses.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Give Up",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                          /*  if (dpInterstitialAd.isLoaded()) {
                                                dpInterstitialAd.show();
                                            } else {
                                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                                            }*/
                                            AlertDialog alertDialog2 = new AlertDialog.Builder(DailyPuzzle.this).create();
                                            alertDialog2.setTitle("Mystery Nmuber Answer");
                                            alertDialog2.setMessage("The mystery number was " + mysteryNumber + ".");
                                            alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            guessButton.setEnabled(false);

                                                        }
                                                    });
                                            alertDialog2.show();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Try Again",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            newRound.callOnClick();
                                        }
                                    });
                            alertDialog.show();
                            //Toast.makeText(getApplicationContext(), "You are out of guesses. The mystery number was " + Integer.toString(mysteryNumber) + ".", Toast.LENGTH_SHORT ).show();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(DailyPuzzle.this).create();
                        alertDialog.setTitle("Out of Guesses");
                        alertDialog.setMessage("You are out of guesses.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Give Up",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                     /*   if (dpInterstitialAd.isLoaded()) {
                                            dpInterstitialAd.show();
                                        } else {
                                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                                        }*/
                                        AlertDialog alertDialog2 = new AlertDialog.Builder(DailyPuzzle.this).create();
                                        alertDialog2.setTitle("Mystery Nmuber Answer");
                                        alertDialog2.setMessage("The mystery number was " + mysteryNumber + ".");
                                        alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        guessButton.setEnabled(false);

                                                    }
                                                });
                                        alertDialog2.show();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Try Again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        newRound.callOnClick();
                                    }
                                });
                        alertDialog.show();
                        //Toast.makeText(getApplicationContext(), "You are out of guesses. The mystery number was " + Integer.toString(mysteryNumber) + ".", Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Guess", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Guess", Toast.LENGTH_SHORT).show();
            }


        }
    }

    class HandleClick2 implements View.OnClickListener {
        public void onClick(View arg0) {
            guessNumberTV = findViewById((R.id.guessNumberTV));
            mysteryNumber = Integer.parseInt(mnFromDB);
            System.out.println(mysteryNumber);
            guessNumber = 1;
            guessNumberTV.setText("Guess Number: " + guessNumber);
            elapsedTimeTV = findViewById((R.id.elapsedTimeTV));
            minutes = 0;
            seconds = 0;
            if (gameCounter == 0) {
                t.start();
                gameCounter++;
            }
            firstCell = findViewById(R.id.oneone);
            secondCell = findViewById(R.id.onetwo);
            thirdCell = findViewById(R.id.onethree);
            firstCell.setText("-");
            secondCell.setText("-");
            thirdCell.setText("-");
            firstCell = findViewById(R.id.twoone);
            secondCell = findViewById(R.id.twotwo);
            thirdCell = findViewById(R.id.twothree);
            firstCell.setText("-");
            secondCell.setText("-");
            thirdCell.setText("-");
            firstCell = findViewById(R.id.threeone);
            secondCell = findViewById(R.id.threetwo);
            thirdCell = findViewById(R.id.threethree);
            firstCell.setText("-");
            secondCell.setText("-");
            thirdCell.setText("-");
            firstCell = findViewById(R.id.fourone);
            secondCell = findViewById(R.id.fourtwo);
            thirdCell = findViewById(R.id.fourthree);
            firstCell.setText("-");
            secondCell.setText("-");
            thirdCell.setText("-");
            firstCell = findViewById(R.id.fiveone);
            secondCell = findViewById(R.id.fivetwo);
            thirdCell = findViewById(R.id.fivethree);
            firstCell.setText("-");
            secondCell.setText("-");
            thirdCell.setText("-");
            firstCell = findViewById(R.id.sixone);
            secondCell = findViewById(R.id.sixtwo);
            thirdCell = findViewById(R.id.sixthree);
            firstCell.setText("-");
            secondCell.setText("-");
            thirdCell.setText("-");
            timerBool = true;
            newRound.setEnabled(false);
            guessButton.setEnabled(true);
            /*new java.util.Timer().schedule(
                    waitTimer = new TimerTask() {
                        @Override
                        public void run() {
                            if (seconds<59) {
                                seconds=seconds+1;
                            } else if (seconds==59) {
                                minutes = minutes+1;
                                seconds=0;
                            }
                            if (minutes<10) {
                                modMinutes = "0" + Integer.toString(minutes);
                            } else if (minutes>=10) {
                                modMinutes = Integer.toString(minutes);
                            }
                            if (seconds<10) {
                                modSeconds = "0" + Integer.toString(seconds);
                            } else if (seconds>=10) {
                                modSeconds = Integer.toString(seconds);
                            }
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    elapsedTimeTV.setText("Elapsed Time: " + modMinutes + ":" + modSeconds);
                                }
                            });

                        }
                    },
                    10, 1000000000
            );*/

        }
    }

    Thread t = new Thread() {
        public void run() {

            while (true) {

                try {
                    if (timerBool) {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (seconds < 59) {
                                    seconds++;
                                } else if (seconds == 59) {
                                    minutes++;
                                    seconds = 0;
                                }
                                if (minutes < 10) {
                                    modMinutes = "0" + minutes;
                                } else if (minutes >= 10) {
                                    modMinutes = Integer.toString(minutes);
                                }
                                if (seconds < 10) {
                                    modSeconds = "0" + seconds;
                                } else if (seconds >= 10) {
                                    modSeconds = Integer.toString(seconds);
                                }
                                elapsedTimeTV.setText("Elapsed Time: " + modMinutes + ":" + modSeconds);
                            }
                        });
                    }
                    if (count < 10) {
                        count++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private void alertView(final String title, final String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(DailyPuzzle.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (title == "New Daily Puzzle") {
                            newRound.callOnClick();
                            guess.setText("");
                        }
                    }
                });
        alertDialog.show();
    }

}
