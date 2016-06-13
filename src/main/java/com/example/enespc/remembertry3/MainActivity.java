package com.example.enespc.remembertry3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static int RESULT_LOAD_IMAGE = 1;
    private static final String TWITTER_KEY = "XZ4KUEqVLn32LCqA748XfP6zz";
    private static final String TWITTER_SECRET = "zJ6dCETF2XJmYzgHoQDlEhbsp0FU6pPQy5R3j6RdIDVY8xkdtS";
    private TwitterLoginButton loginButton;


    SenseCard user, memoryUser;
    long userId;
    String twitterUsername, notes;
    TwitterSession session;
    EditText nameEdit, emailEdit, phoneEdit;
    ImageView avatar;
    Button change, note;
    final Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_main);

        avatar = (ImageView) findViewById(R.id.imageView);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE );
            }
        });

        nameEdit = (EditText) findViewById(R.id.editTextName);
        emailEdit = (EditText) findViewById(R.id.editTextEmail);
        phoneEdit = (EditText) findViewById(R.id.editTextPhone);
        change = (Button) findViewById(R.id.change);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        //Note codes
        note = (Button) findViewById(R.id.imageButton);


            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflater = LayoutInflater.from(context);

                    View promptView = layoutInflater.inflate(R.layout.note_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setView(promptView);

                    final EditText input = (EditText) promptView.findViewById(R.id.noteEdit);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    // get user input and set it to result

                                    notes = input.getText().toString();
                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();

                                        }
                                    });
                    AlertDialog alertD = alertDialogBuilder.create();
                    alertD.show();
                }
            });

        //Setting every variable to null
        twitterUsername = "";
        userId = 0;

        FileInputStream ins = null;
        try {
            ins = openFileInput("SenseCard");
            ObjectInputStream os = new ObjectInputStream(ins);
            Object a = os.readObject();
            memoryUser = (SenseCard)a;
            os.close();
            ins.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(memoryUser != null){
        nameEdit.setText( memoryUser.getName());
        emailEdit.setText(memoryUser.getEmail());
        phoneEdit.setText(memoryUser.getPhoneNumber());
        }


        Thread twitter = new Thread(new Runnable() {
            @Override
            public void run() {
                loginButton.setCallback(new Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> result) {
                        // Do something with result, which provides a TwitterSession for making API calls
                        session =
                                Twitter.getSessionManager().getActiveSession();
                        TwitterAuthToken authToken = session.getAuthToken();
                        String token = authToken.token;
                        String secret = authToken.secret;
                        userId = session.getUserId();
                        twitterUsername = session.getUserName();


                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                    }
                });
            }
        });
        twitter.run();


        Thread save = new Thread(new Runnable() {
            @Override
            public void run() {
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        user = new SenseCard(twitterUsername, emailEdit.getText().toString(), userId, phoneEdit.getText().toString(), nameEdit.getText().toString() );


                                try {
                                    FileOutputStream fos = openFileOutput("SenseCard", Context.MODE_PRIVATE);
                                    ObjectOutputStream os = new ObjectOutputStream(fos);
                                    os.writeObject(user);
                                    os.flush();
                                    os.close();
                                    fos.close();
                                } catch (IOException e) {
                                    Log.v("Exception", "IOException is catched!");
                                    e.printStackTrace();
                                }


                        Intent i = new Intent(MainActivity.this, Location.class);
                        MainActivity.this.startActivity(i);
                    }
                });
            }
        });
        save.run();



//        session2 =
//                Twitter.getSessionManager().getActiveSession();
//        TwitterAuthClient authClient = new TwitterAuthClient();
//        authClient.requestEmail(session2, new Callback() {
//            @Override
//            public void success(Result result) {
//                // Do something with the result, which provides
//                // the email address
//                email = result.toString();
//                emailView.setText(result.toString());
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                // Do something on failure
//                emailView.setText("We cant get your email!");
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            avatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
