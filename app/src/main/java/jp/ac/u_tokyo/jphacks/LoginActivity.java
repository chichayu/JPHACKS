package jp.ac.u_tokyo.jphacks;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


import entity.UserRequest;

/**
 * Created by xixi on 10/20/16.
 */
public class LoginActivity extends AppCompatActivity {
    UserRequest user;

    final int CONNECT_TIMEOUT = 10 * 1000;  //接続タイムアウト[ms]
    final int READ_TIMEOUT = 5 * 1000;  //読み取りタイムアウト[ms]
    final String ENCORDING = "UTF-8";  //エンコードは固定

    String phpUrl = "http://www.jigenji.biz/server/controller/UserRequestController.php";  //JSON 受信用 PHPのURL

    // データの読み込み方法
    static final String readLinesText(InputStream is) throws IOException {
        final StringBuilder str = new StringBuilder();
        final String sep = System.getProperty("line.separator");  //改行コード
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                str.append(line);  //終端文字は含まない
                str.append(sep);  //改行コード追加
            }
            return str.toString();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            str.setLength(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText nameEditText = (EditText) findViewById(R.id.getNameL);
        final EditText passwordEditText = (EditText) findViewById(R.id.getPasswordL);

        //登録Button取得
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            private final Handler handler = new Handler();

            @Override
            public void onClick(View v) {
                user = new UserRequest();
                String name = nameEditText.getText().toString();
                user.setName(name);
                String password = passwordEditText.getText().toString();
                user.setPassword(password);
                taskExe();
            }
        });
    }

    private void taskExe() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            @Override
            protected String doInBackground(Void... params) {
                String res_number = "";
                try {
                    URL url = new URL(phpUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(CONNECT_TIMEOUT);  //接続タイムアウト
                    con.setReadTimeout(READ_TIMEOUT);  //読み取りタイムアウト
                    con.setRequestMethod("POST");  //"GET"でも可
                    con.setDoOutput(true);  //出力用接続フラグON
                    con.setRequestProperty("Content-Type", "application/json");
                    con.connect();
                    //System.out.println("Success");
                    Log.i("OSA030", "doPost start.:" + con.toString());

                    //データ用クラス から、JSON 形式に変換
                    Gson gson = new Gson();
                    UserRequest ur = new UserRequest("YamashitaKeisuke", "yamayama9159");
                    String urtext = gson.toJson(ur);

                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), ENCORDING)));
                    pw.write(urtext);  //出力
                    pw.close();
                    //フラッシュして閉じる

                    //PHP からの応答(確認用)
                    String res = readLinesText(con.getInputStream());
                    res_number = res.substring(0, 3);
                    System.out.println(res_number);

                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return res_number;
            }

            @Override
            protected void onPostExecute(String res_number) {
                //if response is true
                if (res_number.equals("211")) {
                        /*
                        con.connect();
                        //System.out.println("Success");
                        PrintWriter pw2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), ENCORDING)));
                        pw2.write("SR");  //出力
                        pw2.close();  //フラッシュして閉じ
                        con.disconnect();
                        */
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("ご登録内容を照合できません。内容をご確認ください。");
                    builder.show();
                }
            }
        };
        task.execute();
    }
}