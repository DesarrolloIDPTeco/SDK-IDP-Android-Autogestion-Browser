package com.example.flowmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.forgerock.android.auth.FRAuth;
import org.forgerock.android.auth.FRListener;
import org.forgerock.android.auth.FRUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private ConstraintLayout clWelcome;
    private ConstraintLayout clProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FRAuth.start(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnIngresar = findViewById(R.id.btnIngresar);
        clWelcome = findViewById(R.id.clWelcome);
        clProgressBar = findViewById(R.id.clProgressBar);

        btnIngresar.setOnClickListener(v -> {
            if ( FRUser.getCurrentUser() != null ){
                userinfo();
            } else {
                clWelcome.setVisibility(View.INVISIBLE);
                clProgressBar.setVisibility(View.VISIBLE);
                launchBrowser();
            }
        });
    }

    public void launchBrowser() {
        FRUser.browser().appAuthConfigurer()
                .authorizationRequest(r -> {
                    Map<String, String> additionalParameters = new HashMap<>();
                    additionalParameters.put("acr_values", "sdkAuthenticationTree");
                    additionalParameters.put("KEY2", "VALUE2");
                })
                .customTabsIntent(t -> {
                    t.setShowTitle(false);
                    // Seteo de color de browser
                    t.setToolbarColor(R.drawable.tb_gradient);
                }).done()
                .login(this, new FRListener<FRUser>() {
                    @Override
                    public void onSuccess(FRUser result) {
                        userinfo();
                    }

                    @Override
                    public void onException(Exception e) {
                    }
                });
    }

    public void userinfo() {
        Intent intent = new Intent(this, UserinfoActivity.class);
        startActivity(intent);
    }
}