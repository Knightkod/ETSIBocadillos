package bl;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import dl.APIResponseBoc;
import dl.APIResponseVers;
import dl.Bocata;
import dl.Gestion;

@Path("/provider")
@Produces("application/json")//Genera todo JSON
public class RestServicesProvider {

	@EJB
	private final ProveedorEJB ejb = new ProveedorEJB();
	public RestServicesProvider(){
		
	}
	
	@GET
	@Path("/vers")
	public APIResponseVers getVersion(){
		List<Gestion>results=ejb.getLVers();
		APIResponseVers vers = new APIResponseVers(results);
		return vers;
	}
	
	@GET
	@Path("/bocatas")
	public APIResponseBoc getBocatas(){		
		List<Bocata> results=ejb.getLBoc();
		APIResponseBoc bocs=new APIResponseBoc(results);
		return bocs;
	}
	
	
	
}
