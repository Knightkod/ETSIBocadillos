package pl;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import bl.ProveedorEJB;
import dl.Bocata;

@ManagedBean
@RequestScoped
public class BocadilloBean implements Serializable{
//MEJOR NO EXTENDER, ENCAPSULAR COMO ABAJO
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private final ProveedorEJB ejb = new ProveedorEJB();
	private boolean correctAlta = false;
	private Bocata bocadillo = new Bocata();
	private List <Bocata> listaBoc;
	
	public String envRespuestaAlta(){
		String s = "El bocadillo "+bocadillo.getNombre()+ " dado de alta.";
		return s;
	}
	
	public void addBoc(){			
		bocadillo.setFav(0);
		bocadillo.setRate(0);
		listaBoc=getListaBoc();
		if(listaBoc != null){
			for(int i=0;i<listaBoc.size();i++){
				modificaAntiguedadBoc(listaBoc.get(i));		
			}
		}
		bocadillo.setAntiguedad(0);	
		ejb.setBoc(bocadillo);
		ejb.actualizaVersion();
		bocadillo = new Bocata();
		correctAlta = true;
	}

	public void borraBoc(String nombre){
		ejb.remBoc(nombre);
		ejb.actualizaVersion();
	}
	
	public void modificaAntiguedadBoc(Bocata boc){
		int aux=boc.getAntiguedad();
		boc.setAntiguedad(aux+1);
		ejb.remBoc(boc.getNombre());
		ejb.setBoc(boc);		
	}

	
	////////////////////////////////////////////////////	
	//ACORDARSE DE LOS GETTERS Y LOS SETTERS en caso de necesitar
	public Bocata getBocadillo(){
		return bocadillo;
	}
	
	public List <Bocata> getListaBoc(){		
		listaBoc = ejb.getLBoc();
		
		return listaBoc;
	}

	public boolean getCorrectAlta(){
		return correctAlta;		
	}
	
	public void setCorrectAlta(boolean correcAlta){
		this.correctAlta = correcAlta;		
	}	
	////////////////////////////////////////////////////
}
