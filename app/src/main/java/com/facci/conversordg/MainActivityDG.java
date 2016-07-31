package com.facci.conversordg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityDG extends AppCompatActivity {

    final String[] datos= new String[] {"DÓLAR","EURO","PESO MEXICANO"};
    private Spinner MonActSP;
    private Spinner MonCamSP;
    private EditText ValCamET;
    private TextView TotalTV;

    final private double ValorDolarEuro = 0.87;
    final private double ValorPesoDolar = 0.54;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_dg);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,datos);
        MonActSP=(Spinner) findViewById(R.id.MonActSP);

        MonActSP.setAdapter(adaptador);

        MonCamSP=(Spinner) findViewById(R.id.MonCamSP);

        SharedPreferences preferencia =getSharedPreferences("Mis preferencias", Context.MODE_PRIVATE);
        String tmpmonact=preferencia.getString("monact", "");
        String tmpmoncam=preferencia.getString("moncam","");
        if(!tmpmonact.equals("")){
            int indice=adaptador.getPosition(tmpmonact);
            MonActSP.setSelection(indice);
        }
        if(!tmpmoncam.equals("")){
            int indice=adaptador.getPosition(tmpmoncam);
            MonCamSP.setSelection(indice);
        }

    }
    public void onClickAcerca(View v){
        Intent intent = new Intent(MainActivityDG.this,AcercaActivityDG.class);
        startActivity(intent);
    }
    public void clickConvertidor(View v){
        MonActSP=(Spinner) findViewById(R.id.MonActSP);
        MonCamSP=(Spinner) findViewById(R.id.MonCamSP);
        ValCamET=(EditText) findViewById(R.id.ValCamET);
        TotalTV=(TextView)findViewById(R.id.TotalTV);

        String monact=MonActSP.getSelectedItem().toString();
        String moncam=MonCamSP.getSelectedItem().toString();
        double valcam=Double.parseDouble(ValCamET.getText().toString());
        double total=procesaConversion(monact,moncam,valcam);

        if(total>0){
            TotalTV.setText(String.format("Por %5.2f %s,usted recibirá %5.2f %s",valcam,monact,total,moncam));
            ValCamET.setText("");

            SharedPreferences preferencia =getSharedPreferences("Mis preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor edita=preferencia.edit();

            edita.putString("monact",monact);
            edita.putString("moncam",moncam);

            edita.commit();
        }else{
            TotalTV.setText(String.format("Usted recibirá"));
            Toast.makeText(MainActivityDG.this,"Las opciones que eligió no tienen un factor de conversión",Toast.LENGTH_SHORT).show();
        }

    }
    private double procesaConversion(String monact,String moncam,double valcam){
        double totalconverion=0;
        switch (monact){
            case "DÓLAR":
                if(moncam.equals("EURO"))
                    totalconverion=valcam *ValorDolarEuro;

                if(moncam.equals("PESO MEXICANO"))
                    totalconverion=valcam /ValorPesoDolar;
                break;
            case "EURO":
                if(moncam.equals("DÓLAR")){
                    totalconverion=valcam /ValorDolarEuro;
                }
                break;
            case "PESO MEXICANO":
                if(moncam.equals("DÓLAR")){
                    totalconverion=valcam *ValorPesoDolar;
                }
                break;

        }
        return totalconverion;
    }
}

