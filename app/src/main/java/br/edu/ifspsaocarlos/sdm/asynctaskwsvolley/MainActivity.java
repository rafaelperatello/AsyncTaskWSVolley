package br.edu.ifspsaocarlos.sdm.asynctaskwsvolley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TEXT_URL = "http://www.nobile.pro.br/sdm/texto.php";
    private final String DATA_URL = "http://www.nobile.pro.br/sdm/data.php";

    private ProgressBar mProgress;
    private EditText editAddress;
    private TextView tvTexto;
    private TextView tvData;
    private TextView tvCustomTexto;
    private Button btAcessarWs;
    private Button btAcessarCustomWs;

    private RequestQueue requestQueue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        editAddress = (EditText) findViewById(R.id.edit_address);
        tvTexto = ((TextView) findViewById(R.id.tv_texto));
        tvData = ((TextView) findViewById(R.id.tv_data));
        tvCustomTexto = ((TextView) findViewById(R.id.tv_texto_custom_ws));
        mProgress = (ProgressBar) findViewById(R.id.pb_carregando);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarCustomWs = (Button) findViewById(R.id.bt_acessar_custom_ws);

        btAcessarWs.setOnClickListener(this);
        btAcessarCustomWs.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.bt_acessar_ws) {
            buscarTexto(TEXT_URL);
            buscarData(DATA_URL);
        } else if (v.getId() == R.id.bt_acessar_custom_ws) {
            buscarCustomData(editAddress.getText().toString());
        }
    }

    private void buscarTexto(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                tvTexto.setText(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(stringRequest);
    }

    private void buscarData(String url) {
        mProgress.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String data = null, hora = null, ds = null;

                try {
                    data = jsonObject.getInt("mday") + "/" + jsonObject.getInt("mon") + "/" + jsonObject.getInt("year");
                    hora = jsonObject.getInt("hours") + ":" + jsonObject.getInt("minutes") + ":" + jsonObject.getInt("seconds");
                    ds = jsonObject.getString("weekday");

                } catch (JSONException jsone) {
                    Log.e("SDM", "Erro no processamento do objeto JSON");
                }

                tvData.setText(data + "\n" + hora + "\n" + ds);
                mProgress.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tvCustomTexto.setText("Erro ao recuperar data");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void buscarCustomData(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                tvCustomTexto.setText(jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tvCustomTexto.setText("Erro ao recuperar dados");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
