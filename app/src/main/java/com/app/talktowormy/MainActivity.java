package com.app.talktowormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.wormy.root.talktowormy.R;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    java.lang.String s = "Hi";
    public void SendMessage(View view) throws Exception {

        EditText userText = (EditText) findViewById(R.id.user_message);
        s = userText.getText().toString();
        hideKeyboard();

        EditText botText = (EditText) findViewById(R.id.bot_message);
        botText.setText("...");


        if (!checkOnlineState())
        {
            botText.setText("Hey, you're not connected to the internet :@");
            return;
        }


        Button sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setText(R.string.button_sending);
        sendButton.setEnabled(false);
        new AsyncThinkTask().execute(s);
    }
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private boolean checkOnlineState() {
        ConnectivityManager CManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        if (NInfo != null && NInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    //here goes the Async call to the bot api
    private class AsyncThinkTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            createSessions();
            try {
                return SendMessage(params[0]);
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            EditText botText = (EditText) findViewById(R.id.bot_message);
            botText.setText(result);

            EditText userText = (EditText) findViewById(R.id.user_message);
//            userText.setEnabled(true);
            userText.setText("");

            Button sendButton = (Button) findViewById(R.id.send_button);
            sendButton.setText(R.string.button_send);
            sendButton.setEnabled(true);

        }

        ChatterBotSession bot1session;
        ChatterBotSession bot2session;
        boolean created=false;
        //kinda singleton pattern
        public void createSessions()
        {
            if (created) return;
            ChatterBotFactory factory = new ChatterBotFactory();

            try {
                ChatterBot bot1 = factory.create(ChatterBotType.CLEVERBOT);
                bot1session = bot1.createSession();


//                ChatterBot bot2 = factory.create(ChatterBotType.CLEVERBOT, "b0dafd24ee35a477");
//                bot2session = bot2.createSession();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            created = true;
        }

//        boolean alternate= false;
        public String SendMessage(String s) throws Exception {
            createSessions();
//            if (alternate)
//                s = bot2session.think(s);
//            else
//                s = bot1session.think(s);
//            alternate = !alternate;

            s= bot1session.think(s);
            return CleanResponse(s);
        }
        private String CleanResponse(String input)
        {
            String s= input;
            if (s.toLowerCase().contains("cleverbot"))
                s=s.replace("Cleverbot","Wormy").replace("cleverbot","wormy");

            if (s.contains("SMS") || s.contains("iOS"))
                s = "Could you please repeat that?";

            return s;
        }


    }

}
