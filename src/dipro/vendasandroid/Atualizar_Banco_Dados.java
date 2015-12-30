package dipro.vendasandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Atualizar_Banco_Dados extends Activity {

	Button btnVoltar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atualizar_banco_dados);
		
		btnVoltar = (Button) findViewById(R.AtualizarBancoDados.btnVoltar);
		
		btnVoltar.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {}
}
