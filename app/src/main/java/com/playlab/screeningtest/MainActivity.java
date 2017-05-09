package com.playlab.screeningtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TCPClient mTcpClient;
    EditText edt_userid, edt_password, edt_console;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_userid = (EditText) findViewById(R.id.edt_userid);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_console = (EditText) findViewById(R.id.edt_console);
        btn_send = (Button) findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String userID = edt_userid.getText().toString();
                String password = edt_password.getText().toString();

                if (userID.length() != 0 && password.length() != 0) {
                    edt_userid.setText("");
                    edt_password.setText("");
                    //edt_console.setText("");
                    String requestString = "{\"action\":\"get_user\",\"params\":{\"passwd\":\"" + password + "\",\"user_id\":\"" + userID + "\"}}\n";
                    mTcpClient.sendMessage(requestString);

                } else {
                    Toast.makeText(MainActivity.this, "User ID and Password cannot be blank", Toast.LENGTH_LONG).show();

                }

            }
        });

        new ConnectTask().execute("");

        if (mTcpClient != null) {
            mTcpClient.sendMessage("testing");
        }


    }

    public class ConnectTask extends AsyncTask<String, String, TCPClient> {
        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
            edt_console.append(values[0]);
        }
    }

//    public String resultParser(String jsonString) {
//        // searchResult refers to the current element in the array "search_result"
//        String result = "" ;
//
//        try {
//            JSONObject questionMark = new JSONObject(jsonString);
//            Iterator keys = questionMark.keys();
//
//            while (keys.hasNext()) {
//                String currentDynamicKey = (String) keys.next();
//                String currentDynamicValue = "\"" + questionMark.getString(currentDynamicKey) + "\"" ;
//                result = result + currentDynamicKey + " = " + currentDynamicValue + "\n" ;
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return result ;
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTcpClient != null) {
            mTcpClient.stopClient();
        }
    }

}
