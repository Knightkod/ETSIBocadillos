package dl;

import java.util.List;

/*
 *Clase para poder enviar objetos JSON con arrays tipo NAMED, de NAME=results
 */

public class APIResponseVers {
	private List<Gestion> results;
	
	public APIResponseVers(List<Gestion> results){
		this.results=results;
	}

	public List<Gestion> getResults() {
		return results;
	}

	public void setResults(List<Gestion> results) {
		this.results = results;
	}

	
	

}
