package vendroid.cadastro;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import dipro.vendasandroid.R;

public class Cadastro_Lista extends ListActivity{

	@Override
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastrolist);
		String [] itens = new String [] {"Cliente","Fornecedor","Marcas","Grupos","Produtos","Rotas","Vendedores","Formas de Pagamento","Usuários","Empresa","Voltar"};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itens);
		setListAdapter(adapter);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	switch (position) {
	case 0:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Cliente.class));
		break;
	case 1:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Fornecedor.class));
		break;
	case 2:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Marcas.class));
		break;
	case 3:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Grupos.class));
		break;
	case 4:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Produtos.class));
		break;
	case 5:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Rotas.class));
		break;
	case 6:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Vendedor.class));
		break;
	case 7:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_FormasPagamento.class));
		break;
	case 8:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Usuario.class));
		break;
	case 9:
		startActivity(new Intent(vendroid.cadastro.Cadastro_Lista.this,vendroid.cadastro.Cadastro_Empresa.class));
		break;
	case 10:
		finish();
		break;
	}
	
	}
	
	@Override
	public void onBackPressed() {}
	
}
