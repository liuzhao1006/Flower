package com.lz.flower;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lz.flower.activity.FlowerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_flower)
    Button btnFlower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Button btn_flower = findViewById(R.id.btn_flower);
        //下面两行,任选其一,其中第一行不带参数,第二行携带参数.
//        btn_flower.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FlowerActivity.class)));
//        btn_flower.setOnClickListener(v -> FlowerActivity.launch(this, "Flower"));


    }

    @OnClick(R.id.btn_flower)
    public void onViewClicked() {
        FlowerActivity.launch(this, "Flower");
    }
}
