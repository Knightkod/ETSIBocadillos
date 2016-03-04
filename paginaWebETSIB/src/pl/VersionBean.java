package pl;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import bl.ProveedorEJB;
import dl.Gestion;



@ManagedBean
@RequestScoped
public class VersionBean implements Serializable{
//MEJOR NO EXTENDER, ENCAPSULAR COMO ABAJO
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private final ProveedorEJB ejb = new ProveedorEJB();
	private List <Gestion> listaVers;
	
	
	////////////////////////////////////////////////////	
	//GETTERS Y LOS SETTERS

	public List <Gestion> getListaVers(){		
		listaVers = ejb.getLVers();	
		return listaVers;
	}

	////////////////////////////////////////////////////

	
	
}
