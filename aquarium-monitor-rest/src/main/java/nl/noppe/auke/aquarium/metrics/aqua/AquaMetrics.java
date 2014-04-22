package nl.noppe.auke.aquarium.metrics.aqua;

import java.util.Date;

import nl.noppe.auke.aquarium.metrics.Metrics;

public class AquaMetrics implements Metrics {

	private Long id;
	private Date occuredDateTime;
	private Double ph;
	
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
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
