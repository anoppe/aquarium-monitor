package nl.noppe.auke.aquarium.metrics.aqua;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import nl.noppe.auke.aquarium.metrics.Metrics;

@Document
public class AquaMetrics implements Metrics {

	@Id
	private String id;
	private Date occuredDatetime;
	private Double ph;
	private Double temperature;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Date getOccuredDatetime() {
		return occuredDatetime;
	}

	@Override
	public void setOccuredDatetime(Date occuredDatetime) {
		this.occuredDatetime = occuredDatetime;
	}
	
	public Double getPh() {
		return ph;
	}
	
	public void setPh(Double ph) {
		this.ph = ph;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

}
