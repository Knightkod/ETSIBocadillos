package dl;

import java.util.List;

/*
 *Clase para poder enviar objetos JSON con arrays tipo NAMED, de NAME=results
 */

public class APIResponseBoc {
	private List<Bocata> results;
	
	public APIResponseBoc(List<Bocata> results){
		this.results=results;
	}

	public List<Bocata> getResults() {
		return results;
	}

	public void setResults(List<Bocata> results) {
		this.results = results;
	}
	
	

}
