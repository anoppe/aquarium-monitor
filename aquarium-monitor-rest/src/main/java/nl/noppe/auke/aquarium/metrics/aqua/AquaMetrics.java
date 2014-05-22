package nl.noppe.auke.aquarium.metrics.aqua;

import java.util.Date;

import org.springframework.data.annotation.Id;

import nl.noppe.auke.aquarium.metrics.Metrics;

public class AquaMetrics implements Metrics {

	@Id
	private String id;
	private Date occuredDateTime;
	private Double ph;
	
	
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
		return occuredDateTime;
	}

	@Override
	public void setOccuredDatetime(Date occuredDatetime) {
		this.occuredDateTime = occuredDatetime;
	}
	
	public Double getPh() {
		return ph;
	}
	
	public void setPh(Double ph) {
		this.ph = ph;
	}

}
