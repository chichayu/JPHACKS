package jp.ac.u_tokyo.jphacks;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


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

import entity.User;

/**
 * Created by xixi on 10/20/16.
 */
public class LoginActivity extends AppCompatActivity {
    User user;
    // データの読み込み方法
    static final String readLinesText(InputStream is) throws IOException {
        final StringBuilder str = new StringBuilder();
        final String sep = System.getProperty("line.separator");  //改行コード
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while (br.ready()) {
                str.append(br.readLine());  //終端文字は含まない
                str.append(sep);  //改行コード追加
            }
            return str.toString();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                //IOException
            }
            str.setLength(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText nameEditText = (EditText)findViewById(R.id.getName);
        final EditText postalAddressEditText = (EditText)findViewById(R.id.getPostalAddress);
        final EditText passwordEditText = (EditText)findViewById(R.id.getPassword);
        final EditText creditCardEditText = (EditText)findViewById(R.id.getCreditCard);




        //登録Button取得
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                user = new User();

                String name = nameEditText.getText().toString();
                user.setName(name);

                String address = postalAddressEditText.getText().toString();
                user.setAddress(address);

                String password = passwordEditText.getText().toString();
                user.setPassword(password);

                String credit = creditCardEditText.getText().toString();
                user.setAddress(credit);

                //if the information is true
                //テストのメイン... (※例外処理は簡略)
                final int CONNECT_TIMEOUT = 10 * 1000;  //接続タイムアウト[ms]
                final int READ_TIMEOUT = 5 * 1000;  //読み取りタイムアウト[ms]
                final String ENCORDING = "UTF-8";  //エンコードは固定

                String phpUrl = "http://(テストするサーバー)/json_in.php";  //JSON 受信用 PHPのURL

                try {
                    URL url = new URL(phpUrl);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setConnectTimeout(CONNECT_TIMEOUT);  //接続タイムアウト
                    con.setReadTimeout(READ_TIMEOUT);  //読み取りタイムアウト
                    con.setRequestMethod("POST");  //"GET"でも可
                    con.setDoOutput(true);  //出力用接続フラグON
                    con.connect();

                    //データ用クラス から、JSON 形式に変換
                    Gson gson = new Gson();
                    String usertext = gson.toJson(user);

                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), ENCORDING)));
                    pw.write(usertext);  //出力
                    pw.close();  //フラッシュして閉じる

                    //PHP からの応答(確認用)
                    System.out.println("[Response]");
                    String res = readLinesText(con.getInputStream());  //※前回作ったもの
                    System.out.println(res);

                    //response
                    //if true
                    intent.putExtra("user", user);
                    startActivity(intent);
                    //else "ご登録内容を照合できません。内容をご確認ください。"

                    con.disconnect();

                } catch (Exception e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }


            }
        });
    }}
