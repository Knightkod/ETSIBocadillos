package dl;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Gestion database table.
 * 
 */
@Entity
@NamedQuery(name="Gestion.findAll", query="SELECT g FROM Gestion g")
public class Gestion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int version;

	public Gestion() {
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}