package lojadsc.mbeans;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import lojadsc.entidades.Usuario;

@ManagedBean(name = "sessaoMB", eager = true)
@SessionScoped
public class SessaoMB {
	private Usuario usuario;
	private String nome, login, senha, mensagem;
	private boolean logado;

	public SessaoMB() {
		super();
		this.usuario = null;
		this.mensagem = "";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getMensagem() {
		String retorno = this.mensagem;
		this.mensagem = new String();
		return retorno;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isTemMensagem() {
		return !this.mensagem.isEmpty();
	}

	// TODO: manter os dados do usuário na sessão
	public boolean isLogado() {
		return usuario != null;
	}

	public boolean isNotLogado() {
		return usuario == null;
	}

	public Usuario getUser(String login, String senha) {
		if (login.equals("admin") && senha.equals("admin")) {
			return new Usuario("Administrador do sistema", login, senha);
		}
		return null;
	}

	// TODO: melhorar este código (tirar a gambiarra)
	public String autenticar() {
		if (!this.login.isEmpty() && !this.senha.isEmpty()) {
			Usuario autenticado = this.getUser(this.login, this.senha);
			if (autenticado != null) {
				this.setUsuario(autenticado);
				this.logado = true;
				ExternalContext externalContext = FacesContext.getCurrentInstance()
						.getExternalContext();
				try {
					externalContext.redirect(externalContext
							.getRequestContextPath() + "/app/index.xhtml");
				} catch (IOException e) {
					throw new FacesException(e);
				}
			} else {
				this.setMensagem("Login e senha não conferem!");
			}
		} else {
			this.setMensagem("É necessário informar login e senha!");
		}
		return "login.xhtml";
	}

	public void logout() {
		this.usuario = null;
		try {
			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			externalContext.redirect(externalContext.getRequestContextPath()
					+ "/app/index.xhtml");
		} catch (IOException e) {
			throw new FacesException(e);
		}
	}
}
