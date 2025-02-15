package com.ftads.compromissos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ftads.compromissos.dataBase.DataBase;
import com.ftads.compromissos.dominio.entidades.Evento;
import com.ftads.compromissos.dominio.repositorioEventos;

public class detalhesEvento extends AppCompatActivity {


    private Evento evento;

    EditText etData;
    EditText etHora;
    EditText etTermino;
    EditText localEvent;
    EditText descricao;
    EditText participantes;
    EditText sitRepet;
    Spinner spinnerTipoEvent;
    String tipoEvent;

    private repositorioEventos RepositorioEventos;
    private DataBase dataBase;
    private SQLiteDatabase conn;



    Button btEditar;
    Button btVoltar;
    Button btAtualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento);

        etData = (EditText)findViewById(R.id.etData_cadEvent);
        etHora = (EditText)findViewById(R.id.etHora_cadEvent);
        etTermino = (EditText)findViewById(R.id.etTermino_cadEvent);
        localEvent = (EditText)findViewById(R.id.etLocal_cadEvent);
        descricao =(EditText)findViewById(R.id.etDescricao_cadEvent);
        participantes = (EditText)findViewById(R.id.etParticipante_cadEvent);


        //Configura SpinnerTipo Evento
        spinnerTipoEvent = (Spinner)findViewById(R.id.spinner_TipoEvento);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.tipoEvento, android.R.layout.simple_spinner_item);
        spinnerTipoEvent.setAdapter(adapter);
        tipoEvent = adapter.toString();
        //Fim configura SpinnerTIpo Evento

        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            RepositorioEventos = new repositorioEventos(conn);



        }catch (SQLException ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao criar banco" + ex.getMessage());
            dlg.setNeutralButton("Ok", null);
            dlg.show();
        }




        //Recupera dados
        Bundle bundle = getIntent().getExtras();
        if ((bundle != null) && (bundle.containsKey("EVENTOS")))
        {
            evento = (Evento)bundle.getSerializable("EVENTOS");
            preencheDados();
        }
        else
            evento = new Evento();

        etData.setEnabled(false);
        etHora.setEnabled(false);
        etTermino.setEnabled(false);
        localEvent.setEnabled(false);
        descricao.setEnabled(false);
        participantes.setEnabled(false);
        spinnerTipoEvent.setEnabled(false);

        btAtualizar= (Button)findViewById(R.id.btAtualizar_detalheEvent);
        btAtualizar.setEnabled(false);


        btEditar = (Button)findViewById(R.id.btEdit_detalheEvent);
        btEditar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                etData.setEnabled(true);
                etHora.setEnabled(true);
                etTermino.setEnabled(true);
                localEvent.setEnabled(true);
                descricao.setEnabled(true);
                participantes.setEnabled(true);
                spinnerTipoEvent.setEnabled(true);
                btAtualizar.setEnabled(true);

            }
        });



        btVoltar = (Button)findViewById(R.id.btVoltar_detalheEvent);
        btVoltar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iHome = new Intent(getBaseContext(), Home.class);
                startActivity(iHome);
            }
        });





    }

    private void preencheDados()
    {
        etData.setText(evento.getData());
        etHora.setText(evento.getHora());
        etTermino.setText(evento.getTermino());
        localEvent.setText(evento.getLocal());
        descricao.setText(evento.getDescricao());
        participantes.setText(evento.getPariticipantes());
        spinnerTipoEvent.setSelection(Integer.parseInt(evento.getTipoEvento()));

    }


    public void salvar() {
        try {

            RepositorioEventos.excluir(evento.getId());
            evento = new Evento();
            evento.setData(etData.getText().toString());
            evento.setHora(etHora.getText().toString());
            evento.setTermino(etTermino.getText().toString());
            evento.setLocal(localEvent.getText().toString());
            evento.setDescricao(descricao.getText().toString());
            evento.setPariticipantes(participantes.getText().toString());
            evento.setTipoEvento(String.valueOf(spinnerTipoEvent.getSelectedItemPosition()));
            //evento.setRepetir(String.valueOf(repet));

            if (evento.getId() == 0)
                RepositorioEventos.inserirEventos(evento);
            else
                RepositorioEventos.alterarEventos(evento);
        }catch (Exception ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao inserir dados" + ex.getMessage());
            dlg.setNeutralButton("Ok", null);
            dlg.show();
        }
    }
    public void setBtAtualizar_detalheEvent(View view)
    {
        salvar();
        Intent iHome = new Intent(getBaseContext(), Home.class);
        startActivity(iHome);
    }


    public void excluir_detalheEvent(View view)
    {
        try {
            RepositorioEventos.excluir(evento.getId());

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // Seta o Titulo do Dialog
            alertDialogBuilder.setTitle("Atenção");

            // seta a mensagem
            alertDialogBuilder.setMessage("Excluir o registro?").setCancelable(false).setPositiveButton("Sim",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // SIM
                            Intent iHome = new Intent(getBaseContext(), Home.class);
                            startActivity(iHome);

                            // Fecha o dialog
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Não",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // NAO
                                    // Fecha o dialog
                                    dialog.cancel();
                                }
                            });

            // Cria o alertDialog com o conteudo do alertDialogBuilder
            AlertDialog alertDialog = alertDialogBuilder.create();
            // Exibe o Dialog
            alertDialog.show();




        }catch (Exception ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao inserir dados" + ex.getMessage());
            dlg.setNeutralButton("Ok", null);
            dlg.show();
        }
    }


}
