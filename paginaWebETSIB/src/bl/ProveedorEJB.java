package bl;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dl.Bocata;
import dl.Cliente;
import dl.Gestion;

/**
 * Session Bean implementation class ProveedorEJB
 */
@Stateless
@LocalBean
public class ProveedorEJB {
    /**
     * Default constructor. 
     */
	@PersistenceContext
	private EntityManager em;//Producto

    public ProveedorEJB() {
    
    }
    //Apartado de clientes
    public  List<Cliente> getCli(){
    	@SuppressWarnings("unchecked")    	
    	List<Cliente> listaDB= (List<Cliente>) em.createNamedQuery("Cliente.findAll").getResultList();
    	return listaDB;    	
    }
    public Cliente getOneCli(String DNI){
    	@SuppressWarnings("unchecked")
    	List<Cliente> listaDB = (List<Cliente>)em.createNamedQuery("Cliente.findOne").setParameter("dni",DNI).getResultList();
    	if(listaDB.isEmpty()){
    	Cliente cliente=new Cliente();
    	return cliente;	
    	}
    	return listaDB.get(0);
    }
    
    public  void setCli(Cliente lista){
		em.persist(lista);
    }
    
    public void remCli(String DNI){
    	
    	Cliente cli = em.find(Cliente.class,DNI);
    	em.remove(cli);
    	
    }

    
    public boolean comprExistencia(String dni, String pass){
    boolean compr = false;
    	
    Cliente cli = (Cliente)em.find(Cliente.class,dni);
    if(cli != null){
    	if(cli.getPassword().equals(pass) == true)
    	{
    		compr = true;
    		
    	}
    }
    	return compr;
    }
    public boolean comprFormatoHora(String hora){
        boolean compr = false;
       	     
        if(hora.charAt(2)==':'){
        	if((Integer.parseInt(""+hora.charAt(0))< 2 ||
        			Integer.parseInt(""+hora.charAt(0))== 2 && Integer.parseInt(""+hora.charAt(1)) < 4) &&
        			(Integer.parseInt(""+hora.charAt(3)) < 6)
        	  ){
        		compr = true;
        		
        	}
        }
        	return compr;
        }
    ///////////////////////////////////////////////////////////
    
    
    //Apartado de listado de productos
    
    public  List<Bocata> getLBoc(){
    	@SuppressWarnings("unchecked")    	
    	List<Bocata> listaBoc= (List<Bocata>) em.createNamedQuery("Bocata.findAll").getResultList();
    	return listaBoc;    	
    }
    public  void setBoc(Bocata boc){	
		em.persist(boc);
		
    }
    
    public void remBoc(String nombre){    	
    	Bocata boc = em.find(Bocata.class,nombre);
    	em.remove(boc);    	
    }
    
    ///////////////////////////////////////////////////////////
    //Apartado de la version
    public  List<Gestion> getLVers(){
    	@SuppressWarnings("unchecked")    	
    	List<Gestion> listaVers= (List<Gestion>) em.createNamedQuery("Gestion.findAll").getResultList();
    	return listaVers;    	
    }
    public  void setVers(Gestion vers){
		em.persist(vers);
    }
    public void remVers(int vers){    	
    	Gestion version = em.find(Gestion.class,vers);
    	em.remove(version);    	
    }
    public void actualizaVersion(){
		Gestion aux=new Gestion();
		List<Gestion>listaVers = getLVers();
		if(listaVers != null && listaVers.size()!=0){
		aux.setVersion(listaVers.get(0).getVersion()+1);
		remVers(listaVers.get(0).getVersion());
		}else{
			aux.setVersion(0);
		}
		setVers(aux);		
	}
    
    ///////////////////////////////////////////////////////////
}
