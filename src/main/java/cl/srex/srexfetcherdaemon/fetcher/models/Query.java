/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.srex.srexfetcherdaemon.fetcher.models;

public class Query {

	private String query;
	private String[] terms;
	private float[] values;
	
	/**
	 * Constructor de la clase.
	 * 
	 * <p>Recibe como parametros una query, una lista de terminos relacionados
	 *  a la query mas los pesos asociados a los terminos.
	 * 
	 * 
	 * @param query
	 * @param terms
	 * @param values
	 */
	public Query(String query, String[] terms, float[] values) {
		super();
		this.query = query;
		this.terms = terms;
		this.values = values;
	}
	
	/**
	 * Constructor de la clase.
	 * 
	 * <p>La clase puede ser construida sin los parametros de pesos. 
	 * 
	 * @param query
	 */
	public Query(String query) {
		super();
		this.query = query;
	}
	
	/**
	 * Obtiene la query de la consulta.
	 * @return
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Obtiene los terminos.
	 * @return Arreglo de terminos.
	 */
	public String[] getTerms() {
		return terms;
	}
	
	/**
	 * Obtiene los pesos de sus respectivos terminos.
	 * @return Arreglo de pesos.
	 */
	public float[] getValues() {
		return values;
	}
	
	
	public void setTerms(String[] terms) {
		this.terms = terms;
	}

	public void setValues(float[] values) {
		this.values = values;
	}

	@Override
	public String toString()
	{
		return this.query;
	}

}