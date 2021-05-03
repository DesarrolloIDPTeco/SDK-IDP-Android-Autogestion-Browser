package com.example.flowmusic;

import androidx.appcompat.app.AppCompatActivity;

import com.nimbusds.jwt.JWTParser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.forgerock.android.auth.AccessToken;
import org.forgerock.android.auth.FRListener;
import org.forgerock.android.auth.FRUser;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class UserinfoActivity extends AppCompatActivity {

    public static long PERIODO = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

//      Elementos
        TextView userInfoTxt = findViewById(R.id.textViewUserinfo);
        View divider = findViewById(R.id.divider);
        View divider2 = findViewById(R.id.divider2);
        TextView textAccessToken = findViewById(R.id.textAccessToken);
        TextView textIdToken = findViewById(R.id.textIdToken);

//      Botones
        FloatingActionButton btnLogout = findViewById(R.id.btnLogout);
        FloatingActionButton btnRefresh = findViewById(R.id.btnRefresh);


//      Cuando inicia sesion
        FRUser.getCurrentUser().getAccessToken(new FRListener<AccessToken>() {
            @Override
            public void onSuccess(AccessToken result) {
                PERIODO = result.getExpiresIn() * 1000;
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        try {
                            JSONObject accesToken = new JSONObject(result.toJson());
                            textAccessToken.setText(accesToken.toString(4));
                            JSONObject idToken = new JSONObject(JWTParser.parse(result.getIdToken()).getJWTClaimsSet().toString());
                            textIdToken.setText(idToken.toString(4));
                            String sub = idToken.getString("sub");
                            userInfoTxt.setText("Bienvenido: \n" + sub);

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                        textIdToken.postDelayed(() -> btnRefresh.setVisibility(View.VISIBLE), PERIODO);
                    }
                });
            }

            @Override
            public void onException(Exception e) {
            }
        });


//      Refresh Token
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.GONE);
                FRUser.getCurrentUser().getAccessToken(new FRListener<AccessToken>() {
                    @Override
                    public void onSuccess(AccessToken result) {
                        PERIODO = result.getExpiresIn() * 1000;
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                try {
                                    JSONObject accesToken = new JSONObject(result.toJson());
                                    textAccessToken.setText(accesToken.toString(4));
                                    JSONObject idToken = new JSONObject(JWTParser.parse(result.getIdToken()).getJWTClaimsSet().toString());
                                    textIdToken.setText(idToken.toString(4));
                                    String sub = idToken.getString("sub");
                                    userInfoTxt.setText("Bienvenido: \n" + sub);
                                } catch (JSONException | ParseException e) {
                                    e.printStackTrace();
                                }
                                textIdToken.postDelayed(() -> btnRefresh.setVisibility(View.VISIBLE), PERIODO);
                            }
                        });
                    }

                    @Override
                    public void onException(Exception e) {
                    }
                });
            }
        });

//      Logout
        btnLogout.setOnClickListener(v -> {
            FRUser.getCurrentUser().logout();
            Intent intent = new Intent(UserinfoActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}