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
public class FragmentAll extends Fragment {
    ArrayList<Baggage> bagList = new ArrayList<Baggage>();

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
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            // アイコンタップでTextViewに拡大表示する
            textView.setText(il.getName(position));
        }
        */

    }
}
