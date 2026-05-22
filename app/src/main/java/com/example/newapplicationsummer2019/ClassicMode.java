package com.example.newapplicationsummer2019;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class ClassicMode extends AppCompatActivity {
    Button guessButton; //stores reference to button for the whole class
    Button newRound;
    EditText guess;
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
    int inputGuessAsNumber;
    private AdView cmBanner;
    private InterstitialAd cmInterstitialAd;
    private RewardedAd cmHintReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_mode);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        cmBanner = findViewById(R.id.cmBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        //cmBanner.loadAd(adRequest);

        AdRequest adRequest3 = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-8356994243159260/1325525588", adRequest3,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        cmInterstitialAd = interstitialAd;
                        Log.i("$$$", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("$$$", loadAdError.toString());
                        cmInterstitialAd = null;
                    }


                });

        AdRequest adRequest2 = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-8356994243159260/6839120060",
                adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("$$$", loadAdError.toString());
                        cmHintReward = null;
                    }

                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        cmHintReward = ad;

                        cmHintReward.setFullScreenContentCallback(new FullScreenContentCallback() {

                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d("$$$", "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d("$$$", "Ad dismissed fullscreen content.");
                                cmHintReward = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e("$$$", "Ad failed to show fullscreen content.");
                                cmHintReward = null;
                            }


                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d("$$$", "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("$$$", "Ad showed fullscreen content.");
                            }
                        });
                        Log.d("$$$", "Ad was loaded.");
                    }
                });


        guessButton = findViewById((R.id.guessButton));
        guessButton.setEnabled(false);
        //get reference to button
        newRound = findViewById((R.id.newRound));
        //change the buttons caption
        //(usually done in the XML or via a string resource)
        //attach the click listener
        guessButton.setOnClickListener(new handleClick());

        newRound.setOnClickListener(new handleClick2());
        newRound.performClick();
    }

    class handleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            //Toast.makeText(getApplicationContext(), "guess button pressed", Toast.LENGTH_SHORT ).show();
            guess = findViewById((R.id.guess));
            String formatted = guess.getText().toString();
            if (formatted.length() == 3 && Integer.parseInt(formatted) > 100) {
                if (guessNumber <= 6) {
                    int inputGuess = Integer.parseInt(String.valueOf(guess.getText()));
                    inputGuessAsNumber = Integer.parseInt(String.valueOf(guess.getText()));
                    int digitsCorrect = 0;
                    int placesCorrect = 0;
                    guessNumber = guessNumber + 1;
                    if (inputGuess == mysteryNumber) {
                        timerBool = false;
                        placesCorrect = 3;
                        digitsCorrect = 3;

                        AlertDialog alertDialog = new AlertDialog.Builder(ClassicMode.this).create();
                        alertDialog.setTitle("Congrats!");
                        alertDialog.setMessage("The mystery number was " + mysteryNumber + //
                                ". You guessed it in " + (guessNumber - 1) +
                                " guess(es).\nPress OK and then press PLAY AGAIN.");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        //No interstitial ad if you win
                                        newRound.setEnabled(true);
                                        newRound.setText("PLAY AGAIN");
                                        guessButton.setEnabled(false);
                                    }
                                });
                        alertDialog.show();
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
                    }

                    guess.setText("");

                    ArrayList<String> numbersAsStrings = new ArrayList<String>(
                            Arrays.asList("one",
                                    "two",
                                    "three",
                                    "four",
                                    "five",
                                    "six"));
                    String fcFind = "", scFind = "", tcFind = "";
                    if (guessNumber == 1) {
                        fcFind = numbersAsStrings.get(guessNumber - 1) + numbersAsStrings.get(0);
                        scFind = numbersAsStrings.get(guessNumber - 1) + numbersAsStrings.get(1);
                        tcFind = numbersAsStrings.get(guessNumber - 1) + numbersAsStrings.get(2);
                    } else if (guessNumber > 1) {
                        fcFind = numbersAsStrings.get(guessNumber - 2) + numbersAsStrings.get(0);
                        scFind = numbersAsStrings.get(guessNumber - 2) + numbersAsStrings.get(1);
                        tcFind = numbersAsStrings.get(guessNumber - 2) + numbersAsStrings.get(2);
                    }

                    int resID = getResources().getIdentifier(fcFind, "id", getPackageName());
                    firstCell = findViewById(resID);
                    int resID2 = getResources().getIdentifier(scFind, "id", getPackageName());
                    secondCell = findViewById(resID2);
                    int resID3 = getResources().getIdentifier(tcFind, "id", getPackageName());
                    thirdCell = findViewById(resID3);


                    firstCell.setText("(" + (guessNumber - 1) + ") " + inputGuessAsNumber);
                    secondCell.setText(Integer.toString(digitsCorrect));
                    thirdCell.setText(Integer.toString(placesCorrect));


                    if (guessNumber == 7 && placesCorrect != 3) {

                        timerBool = false;
                        AlertDialog alertDialog2 = new AlertDialog.Builder(ClassicMode.this).create();
                        alertDialog2.setTitle("Great Try!");
                        alertDialog2.setMessage("The mystery number was " + mysteryNumber + ".\nPress OK and then press PLAY AGAIN.");
                        alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        final boolean showAd = new Random().nextBoolean();
                                        if (cmInterstitialAd!=null && showAd==true) {
                                            //50% chance
                                            cmInterstitialAd.show(ClassicMode.this);
                                        } else {
                                            Log.d("$$$", "The interstitial wasn't loaded yet.");
                                        }
                                        newRound.setEnabled(true);
                                        newRound.setText("PLAY AGAIN");
                                        guessButton.setEnabled(false);
                                    }
                                });
                        alertDialog2.show();


                        //Toast.makeText(getApplicationContext(), "You are out of guesses. The mystery number was " + Integer.toString(mysteryNumber) + ".", Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    timerBool = false;
                    AlertDialog alertDialog2 = new AlertDialog.Builder(ClassicMode.this).create();
                    alertDialog2.setTitle("Great Try!");
                    alertDialog2.setMessage("The mystery number was " + mysteryNumber + ".\nPress OK and then press PLAY AGAIN.");
                    alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    final boolean showAd = new Random().nextBoolean();
                                    if (cmInterstitialAd!=null && showAd==true) {
                                        //50% chance
                                        cmInterstitialAd.show(ClassicMode.this);
                                    } else {
                                        Log.d("$$$", "The interstitial wasn't loaded yet.");
                                    }
                                    newRound.setEnabled(true);
                                    newRound.setText("PLAY AGAIN");
                                    guessButton.setEnabled(false);
                                }
                            });
                    alertDialog2.show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Your guess must be 3 digits long and can't start with a 0.", Toast.LENGTH_SHORT).show();
            }


        }
    }

    class handleClick2 implements View.OnClickListener {
        public void onClick(View arg0) {
            if (newRound.getText().toString() == "PLAY AGAIN" || seconds == 0) {
                if (gameCounter == 0) {
                    AlertDialog alertDialog2 = new AlertDialog.Builder(ClassicMode.this).create();
                    alertDialog2.setTitle("A Mystery Number has been Chosen");
                    alertDialog2.setMessage("Press OK and begin guessing.");
                    alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (gameCounter == 0) {
                                        t.start();
                                    }
                                    timerBool = true;
                                    gameCounter++;

                                }
                            });
                    alertDialog2.show();
                    newRound.setEnabled(true);
                    newRound.setText("HINT");
                    List<Integer> l23 = new ArrayList<Integer>();
                    l23.add(0);
                    l23.add(1);
                    l23.add(2);
                    l23.add(3);
                    l23.add(4);
                    l23.add(5);
                    l23.add(6);
                    l23.add(7);
                    l23.add(8);
                    l23.add(9);
                    Random rand = new Random();
                    int digit1 = rand.nextInt(9) + 1;
                    l23.remove(l23.indexOf(digit1));
                    int digit2 = l23.get(rand.nextInt(9));
                    l23.remove(l23.indexOf(digit2));
                    int digit3 = l23.get(rand.nextInt(8));
                    mysteryNumber = (digit1 * 100) + (digit2 * 10) + digit3;
                    System.out.println("Mystery Number: " + mysteryNumber);
                    guessNumber = 1;
                    elapsedTimeTV = findViewById((R.id.elapsedTimeTV));
                    minutes = 0;
                    seconds = 0;


                    clearTable();


                    guessButton.setEnabled(true);

                } else {
                    openClassicMode();
                }

            } else {
                //HINT


                AlertDialog alertDialog2 = new AlertDialog.Builder(ClassicMode.this).create();
                alertDialog2.setTitle("Stuck? Get a hint!");
                alertDialog2.setMessage("Watch a video advertisement to get a free hint!");
                alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d("$$$", "User wants a rewarded ad for a hint.");

                                if (cmHintReward != null) {
                                    Activity activityContext = ClassicMode.this;
                                    cmHintReward.show(activityContext, new OnUserEarnedRewardListener() {
                                        @Override
                                        public void onUserEarnedReward(RewardItem rewardItem) {
                                            // Handle the reward.
                                            Log.d("$$$", "The user earned the reward.");
                                            newRound.setEnabled(false);  // No more hints for this round

                                            AlertDialog alertDialog4 = new AlertDialog.Builder(ClassicMode.this).create();
                                            alertDialog4.setTitle("Hint");
                                            if (mysteryNumber<500) {
                                                alertDialog4.setMessage("The mystery number is LESS than 500.");
                                                newRound.setText("<500");
                                            } else {
                                                alertDialog4.setMessage("The mystery number is MORE than 500.");
                                                newRound.setText(">500");
                                            }
                                            alertDialog4.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog4.show();

                                        }
                                    });
                                } else {
                                    Log.d("$$$", "The rewarded ad wasn't ready yet.");

                                    AlertDialog alertDialog3 = new AlertDialog.Builder(ClassicMode.this).create();
                                    alertDialog3.setTitle("Hint Unavailable");
                                    alertDialog3.setMessage("We've encountered an error retrieving a video. Please try again in a few seconds.");
                                    alertDialog3.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog3.show();
                                }
                            }
                        });
                alertDialog2.setButton(AlertDialog.BUTTON_NEGATIVE, "NO THANKS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d("$$$", "User pressed 'NO THANKS'");
                            }
                        });
                alertDialog2.show();

            }
        }
    }

    public void openClassicMode() {
        Intent myIntent = new Intent(this, ClassicMode.class);
        startActivity(myIntent);
    }

    public void clearTable() {
        ArrayList<String> numbersAsStrings = new ArrayList<String>(
                Arrays.asList("one",
                        "two",
                        "three",
                        "four",
                        "five",
                        "six"));
        for (int i = 1; i < 7; i++) {
            String fcFind = numbersAsStrings.get(i - 1) + "one";
            int resID = getResources().getIdentifier(fcFind, "id", getPackageName());
            firstCell = findViewById(resID);
            String scFind = numbersAsStrings.get(i - 1) + "two";
            int resID2 = getResources().getIdentifier(scFind, "id", getPackageName());
            secondCell = findViewById(resID2);
            String tcFind = numbersAsStrings.get(i - 1) + "three";
            int resID3 = getResources().getIdentifier(tcFind, "id", getPackageName());
            thirdCell = findViewById(resID3);
            firstCell.setText("-");
            secondCell.setText("-");
            thirdCell.setText("-");
        }
    }

    Thread t = new Thread() {
        public void run() {

            while (true) {

                try {
                    if (timerBool) {
                        //TODO: use string.format() or simpleDateFormat
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private void alertView(final String title, final String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ClassicMode.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}



