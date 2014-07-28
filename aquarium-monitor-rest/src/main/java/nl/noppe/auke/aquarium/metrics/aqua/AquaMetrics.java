package nl.noppe.auke.aquarium.metrics.aqua;

import java.util.Date;

import nl.noppe.auke.aquarium.metrics.Metrics;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class AquaMetrics implements Metrics {

	@Id
	private String id;
	@Field(value="occuredDatetime")
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

	public Date getOccuredDatetime() {
		return occuredDatetime;
	}

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

	@Override
	public String toString() {
		return "AquaMetrics [id=" + id + ", occuredDatetime=" + occuredDatetime + ", ph=" + ph + ", temperature=" + temperature + "]";
	}

}
