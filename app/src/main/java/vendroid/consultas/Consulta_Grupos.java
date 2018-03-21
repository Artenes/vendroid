package vendroid.consultas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Grupos;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.cadastro.GroupsActivity;
import vendroid.database.AppDatabase;
import vendroid.database.entities.Group;

public class Consulta_Grupos extends Activity {

    private SimpleCursorAdapter dataSource, change;
    private static final String campos[] = {"_id", "grup_descricao", "_id"};
    ListView listview;
    Button btnVoltar, btnBusca, btnCadastrar;
    Tabela_Grupos helper;
    EditText edtCodigo, edtBusca;
    Context context = this;
    Tabela_Usuario_Atual tblUsuarioAtual;
    Tabela_Administrador tblAdministrador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_grupos);

        btnCadastrar = (Button) findViewById(R.id.ConsultaGrupos_btnCadastrar);
        edtBusca = (EditText) findViewById(R.id.ConsultaGrupos_edtBusca);
        edtCodigo = (EditText) findViewById(R.id.ConsultaGrupos_edtCodigo);
        btnBusca = (Button) findViewById(R.id.ConsultaGrupos_btnBusca);
        btnVoltar = (Button) findViewById(R.id.ConsultaGrupos_btnVoltar);
        listview = (ListView) findViewById(R.id.ConsultaGrupos_list);
        helper = new Tabela_Grupos(this);
        helper.open();

        tblAdministrador = new Tabela_Administrador(this);
        tblAdministrador.open();

        tblUsuarioAtual = new Tabela_Usuario_Atual(this);
        tblUsuarioAtual.open();

        change = (SimpleCursorAdapter) getLastNonConfigurationInstance();

        if (change != null) {
            dataSource = change;
            listview.setAdapter(dataSource);
        }

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Consulta_Grupos.this);
                dialog.setContentView(R.layout.caixa_dialogo);
                dialog.setTitle("Permissão de administrador");
                TextView aviso = (TextView) dialog.findViewById(R.id.CaixaDialogo_txvAviso);
                aviso.setText("Deseja continuar?");
                final EditText senha = (EditText) dialog.findViewById(R.id.CaixaDialogo_edtSenha);
                final Button sim = (Button) dialog.findViewById(R.id.CaixaDialogo_btnSim);
                Button nao = (Button) dialog.findViewById(R.id.CaixaDialogo_btnNao);

                sim.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
                        if (password.moveToFirst()) {
                            startActivity(new Intent(Consulta_Grupos.this, GroupsActivity.class));
                            senha.setText("");
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

                nao.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });

                if (tblUsuarioAtual.NivelAcesso("1") == 3 || tblUsuarioAtual.NivelAcesso("1") == 2) {
                    dialog.show();
                } else {
                    startActivity(new Intent(Consulta_Grupos.this, GroupsActivity.class));
                }
            }
        });

        btnBusca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = edtCodigo.getText().toString();
                String grupo = edtBusca.getText().toString();
                ArrayAdapter adapter = new SimpleAdapter(Consulta_Grupos.this, AppDatabase.getInstance(Consulta_Grupos.this).groupDAO().getAll());
                listview.setAdapter(adapter);
                if (helper.BuscaFiltro(id, grupo).getCount() <= 0) {
                    Toast.makeText(context, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.close();
                finish();
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String text = (String) ((TextView) arg1.findViewById(R.id.ConsultaLinhas_txvCodigo)).getText();
                Cursor cur = helper.BuscaID(text);
                cur.moveToFirst();
                if (cur.getCount() <= 0) {
                    //Toast.makeText(context, "O cursor cur na consulta grupos está nulo", Toast.LENGTH_SHORT).show();
                    return;
                }
                String grupo = cur.getString(cur.getColumnIndex("_id"));
                Intent i = new Intent(getApplicationContext(), vendroid.editar.Editar_Grupo.class);
                i.putExtra("id_grupo", grupo);
                helper.close();
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return dataSource;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
        tblUsuarioAtual.close();
        tblAdministrador.close();
        AppDatabase.getInstance(this).close();
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * @TODO remove temporary solution
     */
    private class SimpleAdapter extends ArrayAdapter<Group> {

        public SimpleAdapter(@NonNull Context context, List<Group> groups) {
            super(context, android.R.layout.simple_list_item_2, groups);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Group group = getItem(position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
            }

            ((TextView) convertView.findViewById(android.R.id.text1)).setText(group.getDescription());
            ((TextView) convertView.findViewById(android.R.id.text2)).setText(group.getCode());

            return convertView;

        }
    }

}
