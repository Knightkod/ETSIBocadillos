package pl;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import bl.ProveedorEJB;
import dl.Cliente;


@ManagedBean
@RequestScoped
public class ClienteBean implements Serializable{
//MEJOR NO EXTENDER, ENCAPSULAR COMO ABAJO
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean envCorrect = false;
	public boolean envCorrect2 = false;
	public boolean mensAuth = false;
	public boolean remCorrect = false;
	
	
	public boolean envCorrectDNI = true;
	@EJB
	private final ProveedorEJB ejb = new ProveedorEJB();
	
	private  Cliente cliente = new Cliente();
	private  Cliente cliente2 = new Cliente();
	private  Cliente cliente3 = new Cliente();
	private double cantidadIntro = 0.00;
	private String costeTotal="";
	private String authResp ="";
	private String mensIntroCli="";
	List <Cliente> listaCli;


	public String toString2(){
		String s = "Cliente con DNI "+mensIntroCli + " dado de alta";
		return s;
	}
	
	public String respuesta(){
		return authResp;
	}

	//Administrador
	public void addCli(){
		cliente.setSaldo("0.00");
		ejb.setCli(cliente);
		mensIntroCli=cliente.getDni();
		cliente = new Cliente();
		envCorrect2 = true;
	}

	public void borraCli(String DNI){
		ejb.remCli(DNI);
	}
	
	public void searchCli(String DNI){
		cliente2 = ejb.getOneCli(DNI);
		if(cliente2 != null){
			envCorrectDNI = true;
		}
	}
	
	public String addCash(String DNI){
		cliente2 = ejb.getOneCli(DNI);
		ejb.remCli(DNI);		
		double aux = Double.parseDouble(cliente2.getSaldo()) + cantidadIntro;
		String respuesta;
		cliente2.setSaldo(String.valueOf(aux));		
		ejb.setCli(cliente2);
		respuesta="Saldo introducido:"+aux;
		envCorrectDNI=false;
		cliente2 = new Cliente();
		cantidadIntro=0.00;
		return respuesta;		
	}
	
	////////////////////////////////////////////////////
	//Cafeteria
	public String borraPedido(String DNI){
		String respuesta;
		cliente2 = ejb.getOneCli(DNI);
		ejb.remCli(DNI);				
		cliente2.setPedidoActual(null);		
		ejb.setCli(cliente2);
		respuesta="Pedido servido correctamente";
		return respuesta;
	}
	////////////////////////////////////////////////////
	//Cliente
	public void comprAndBuy(String dni,String password,String horaRecogida,String pedidoActual){		
		double aux;					
		if(ejb.comprExistencia(dni,password)){
			if(ejb.comprFormatoHora(horaRecogida)){
					cliente3=ejb.getOneCli(dni);				
					aux = Double.parseDouble(cliente3.getSaldo()) - Double.parseDouble(costeTotal);
					if(aux > 0){
						ejb.remCli(dni);
						cliente3.setHoraRecogida(horaRecogida);
						cliente3.setSaldo(String.valueOf(aux));	
						cliente3.setPedidoActual(pedidoActual);
						ejb.setCli(cliente3);
						authResp="Pedido pagado y enviado";
					}else{
						authResp="saldo insuficiente, por favor pase por el local para recargar";
					}
			}else{
				authResp="Formato de hora incorrecto. Introduzca uno correcto por favor.";
			}
		}else{
			authResp="Fallo en la autenticación";
		}
		mensAuth = true;		
	}	
	////////////////////////////////////////////////////	
	//ACORDARSE DE LOS GETTERS Y LOS SETTERS en caso de necesitar

	public Cliente getCliente(){
		return cliente;
	}
	
	public Cliente getCliente2() {
		return cliente2;
	}


	public void setCliente2(Cliente cliente2) {
		this.cliente2 = cliente2;
	}	
	

	public Cliente getCliente3() {
		return cliente3;
	}

	public void setCliente3(Cliente cliente3) {
		this.cliente3 = cliente3;
	}

	public double getCantidadIntro(){
		return cantidadIntro;
	}
	
	public void setCantidadIntro(double cantidadIntro){
		this.cantidadIntro = cantidadIntro;
	}
	public String getCosteTotal() {
		return costeTotal;
	}


	public void setCosteTotal(String costeTotal) {
		this.costeTotal = costeTotal;
	}

	public List <Cliente> getlistaCli(){
		
		listaCli = ejb.getCli();
	
	return listaCli;
	}	

	public List <Cliente> getListaCliReducida(){
		listaCli = ejb.getCli();
		for(int i=0;i<listaCli.size();i++){			
			if(listaCli.get(i).getPedidoActual()==null){
				listaCli.remove(i);
				i--;
			}
		}
		if((listaCli.size()==1) && (listaCli.get(0).getPedidoActual()==null)){
			listaCli.remove(0);
		}
	
	return listaCli;
	}

	public boolean getCorrect2(){
		return envCorrect2;		
	}
	
	public void setCorrect2(boolean envCorrec2){
		this.envCorrect2 = envCorrec2;		
	}	
	public boolean getmensAuth(){
		return mensAuth;		
	}
	
	public void setmensAuth(boolean mensAuth){
		this.mensAuth = mensAuth;		
	}	
	public boolean getCorrectDNI(){
		return envCorrectDNI;		
	}
	
	public void setCorrectDNI(boolean envCorrecDNI){
		this.envCorrectDNI = envCorrecDNI;		
	}
	////////////////////////////////////////////////////

	
	
}
