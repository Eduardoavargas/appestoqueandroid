package br.com.appestoque.dao;

import java.io.Serializable;
import android.database.Cursor;

public interface IDAO<T, PK extends Serializable> {
	Cursor listar();
	T pesquisar(long id);
	void limpar();
	void fechar();
}
