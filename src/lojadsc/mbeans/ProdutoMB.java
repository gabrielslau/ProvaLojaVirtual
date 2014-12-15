package lojadsc.mbeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import lojadsc.daos.ProdutoDAORemote;
import lojadsc.entidades.Produto;

@ManagedBean(name = "produtoMB")
@ApplicationScoped
public class ProdutoMB {
	private List<Produto> produtos;
	private Produto produto = new Produto();

	@EJB
	private ProdutoDAORemote dao;

	public ProdutoMB() {
		super();
		produto = new Produto();
		produtos = new ArrayList<Produto>();
	}

	public List<Produto> getProdutos() {
		this.produtos = dao.getProdutosEmEstoque();
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public boolean isPossuiEmEstoque() {
		getProdutos();
		return !produtos.isEmpty();
	}

	public boolean isSemItensNoEstoque() {
		getProdutos();
		return produtos.isEmpty();
	}

	public String cadastrar() {
		// TODO: verificar se já está cadastrado
		// se estiver, lança mensagem e retorna para a págian de cadastro
		// senão, cadastra o produto e retorna para a página de listagem

		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			dao.addProduto(produto);
			produtos.add(produto);
			this.getProdutos();

			externalContext.redirect(externalContext
					.getRequestContextPath() + "/app/index.xhtml");
		} catch (IOException e) {
			throw new FacesException(e);
		}
		return "produto_add.xhtml";
	}
	public void remover() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		try {
			//Verificar se o produto existe
			Produto prod = dao.getProduto(produto.getId());
			if(prod != null){
				dao.removeProduto(produto);
			}
			externalContext.redirect(externalContext.getRequestContextPath() + "/app/index.xhtml");
		} catch (IOException e) {
			throw new FacesException(e);
		}
	}
	public String editar() {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			//Verificar se o produto existe
			Produto prod = dao.getProduto(produto.getId());
			if(prod != null){
				dao.editarProduto(produto);
			}
			externalContext.redirect(externalContext.getRequestContextPath() + "/app/index.xhtml");
		} catch (IOException e) {
			throw new FacesException(e);
		}
		return "produto_edit.xhtml";
	}

}
