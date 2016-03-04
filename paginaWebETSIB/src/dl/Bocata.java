package dl;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Bocatas database table.
 * 
 */
@Entity
@Table(name="Bocatas")
@NamedQuery(name="Bocata.findAll", query="SELECT b FROM Bocata b")
public class Bocata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="Nombre")
	private String nombre;

	@Column(name="Antiguedad")
	private int antiguedad;

	@Column(name="Fav")
	private float fav;

	@Column(name="Ingredientes")
	private String ingredientes;

	@Column(name="Precio")
	private float precio;

	@Column(name="Rate")
	private float rate;

	public Bocata() {
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getAntiguedad() {
		return this.antiguedad;
	}

	public void setAntiguedad(int antiguedad) {
		this.antiguedad = antiguedad;
	}

	public float getFav() {
		return this.fav;
	}

	public void setFav(float fav) {
		this.fav = fav;
	}

	public String getIngredientes() {
		return this.ingredientes;
	}

	public void setIngredientes(String ingredientes) {
		this.ingredientes = ingredientes;
	}

	public float getPrecio() {
		return this.precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public float getRate() {
		return this.rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

}