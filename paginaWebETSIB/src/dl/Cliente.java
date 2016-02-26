package dl;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Cliente database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c"),
@NamedQuery(name="Cliente.findOne", query="SELECT c FROM Cliente c WHERE c.dni=:dni")
})
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DNI")
	private String dni;

	@Column(name="HoraRecogida")
	private String horaRecogida;

	@Column(name="Password")
	private String password;

	@Column(name="PedidoActual")
	private String pedidoActual;

	@Column(name="Saldo")
	private String saldo;

	public Cliente() {
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getHoraRecogida() {
		return this.horaRecogida;
	}

	public void setHoraRecogida(String horaRecogida) {
		this.horaRecogida = horaRecogida;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPedidoActual() {
		return this.pedidoActual;
	}

	public void setPedidoActual(String pedidoActual) {
		this.pedidoActual = pedidoActual;
	}

	public String getSaldo() {
		return this.saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

}