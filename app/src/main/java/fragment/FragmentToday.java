package fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import java.util.ArrayList;

import adapter.GridAdapter;
import entity.Baggage;
import entity.TimeRequest;
import jp.ac.u_tokyo.jphacks.R;

/**
 * Created by xixi on 10/29/16.
 */
public class FragmentToday extends Fragment {
    ArrayList<Baggage> bagList = new ArrayList<Baggage>();

    /*
    TimePickerDialog dialog;
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // 設定した時間をトーストで表示
            Toast.makeText(
                    org.jpn.techbooster.sample.TimePickerDialog.MainActivity.this,
                    "time set to " + Integer.toString(hourOfDay) + " : "
                            + Integer.toString(minute), Toast.LENGTH_LONG)
                    .show();
        }
    };
    */

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
                e.printStackTrace();
            }
            str.setLength(0);
        }
    }

    // サーバーとの接続
    final int CONNECT_TIMEOUT = 10 * 1000;  //接続タイムアウト[ms]
    final int READ_TIMEOUT = 5 * 1000;  //読み取りタイムアウト[ms]
    final String ENCORDING = "UTF-8";  //loadText() 用

    String phpUrl = "http://www.jigenji.biz/server/controller/UserRequestController.php";  //JSON 受信用 PHPのURL

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        new Thread() {
            public void run() {
                try {
                    URL url = new URL(phpUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(CONNECT_TIMEOUT);  //接続タイムアウト
                    con.setReadTimeout(READ_TIMEOUT);  //読み取りタイムアウト
                    con.setRequestMethod("POST");  //"GET"でも可
                    con.setDoOutput(true);  //出力用接続フラグON
                    con.connect();

                    int response = con.getResponseCode();
                    System.out.println("The response is: " + response);

                    PrintWriter pw2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), ENCORDING)));
                    pw2.write("SR");  //出力
                    pw2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //商品情報を得る
        try {
            URL url = new URL(phpUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(CONNECT_TIMEOUT);  //接続タイムアウト
            con.setReadTimeout(READ_TIMEOUT);  //読み取りタイムアウト
            con.setRequestMethod("GET");
            con.connect();

            String text = readLinesText(con.getInputStream());

            con.disconnect();

            //JSON 解析して、データ用クラスに代入
            Gson gson = new Gson();
            Baggage bags = gson.fromJson(text, Baggage.class);
            bagList.add(bags);
            // GridViewの設定
            GridView gridview = (GridView) getActivity().findViewById(R.id.GridView);
            final GridAdapter adapter = new GridAdapter(this.getActivity(), R.layout.grid_items, bagList);
            gridview.setAdapter(adapter);
            //listview.setDivider(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        Calendar calendar = Calendar.getInstance();
        // 現在の時間の取得
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        // 現在の分の取得
        int minute = calendar.get(Calendar.MINUTE);

        // ダイアログの生成、及び初期値の設定
       // TimePickerDialog dialog = new TimePickerDialog(this, android.R.style.Theme_Black, onTimeSetListener, hourOfDay, minute, true);
        // ダイアログを表示する
       // dialog.show();


        /*
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            // アイコンタップでTextViewに拡大表示する
            textView.setText(il.getName(position));
        }
        */


        // 配達時間の指定
        final RadioGroup lmdradiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);

        // 送信ボタン
        Button btn_submit = (Button) view.findViewById(R.id.button);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = lmdradiogroup.getCheckedRadioButtonId();
                RadioButton lmdradiobtn = (RadioButton) view.findViewById(checkedId);
                String lmd = lmdradiobtn.getText().toString();
                //System.out.println(lmd);
                try {
                    URL url = new URL(phpUrl);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(CONNECT_TIMEOUT);  //接続タイムアウト
                    con.setReadTimeout(READ_TIMEOUT);  //読み取りタイムアウト
                    con.setRequestMethod("POST");
                    con.connect();

                    String text = readLinesText(con.getInputStream());

                    con.disconnect();
                    //System.out.println(text3);

                    //データ用クラス から、JSON 形式に変換
                    Gson gson = new Gson();
                    if (lmd == "本日受け取る") {
                        //make everything gray
                        //flag = 0
                        TimeRequest tr = new TimeRequest("YamashitaKeisuke", "yamayama9159", "0", "17:00:00", "19:00:00");
                        String tr1text = gson.toJson(tr);
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), ENCORDING)));
                        pw.write(tr1text);  //出力
                        pw.close();  //フラッシュして閉じる

                        //PHP からの応答(確認用)
                        //String res = readLinesText(con.getInputStream());
                        //System.out.println(res);
                        //System.out.println(bags.getCompany());
                    } else if (lmd == "本日受け取らない") {
                        TimeRequest tr = new TimeRequest("YamashitaKeisuke", "yamayama9159", "0", null, null);
                        String tr0text = gson.toJson(tr);
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), ENCORDING)));
                        pw.write(tr0text);  //出力
                        pw.close();  //フラッシュして閉じる
                    } else if (lmd == "後で考える") {
                        TimeRequest tr = new TimeRequest("YamashitaKeisuke", "yamayama9159", "2", null, null);
                        String tr2text = gson.toJson(tr);
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), ENCORDING)));
                        pw.write(tr2text);  //出力
                        pw.close();  //フラッシュして閉じる
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    URL url = new URL(phpUrl);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(CONNECT_TIMEOUT);  //接続タイムアウト
                    con.setReadTimeout(READ_TIMEOUT);  //読み取りタイムアウト
                    con.setRequestMethod("GET");
                    con.connect();

                    String text = readLinesText(con.getInputStream());

                    con.disconnect();
                    //System.out.println(text3);
                    if (text == "100") {
                        //print "Done" if get 100 from the server
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("送信しました。");
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("問題が発生しました。");
                        builder.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
