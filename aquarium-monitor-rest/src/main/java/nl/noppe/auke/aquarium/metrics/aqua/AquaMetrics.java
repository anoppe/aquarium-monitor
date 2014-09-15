package nl.noppe.auke.aquarium.metrics.aqua;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.noppe.auke.aquarium.metrics.Metrics;

@Entity
@Table(name="tbl_aqua_metrics")
public class AquaMetrics implements Metrics {

	@Id
	@GeneratedValue
	@Column(name="aqua_metrics_id")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="occured_datetime")
	private Date occuredDatetime;
	@Column(name="ph")
	private Double ph;
	@Column(name="temperature")
	private Double temperature;
	@Column(name="current")
	private Double current;
	
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
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
	
	public Double getCurrent() {
		return current;
	}

	public void setCurrent(Double current) {
		this.current = current;
	}
	
	@Override
	public String toString() {
		return "AquaMetrics [id=" + id + ", occuredDatetime=" + occuredDatetime + ", ph=" + ph + ", temperature=" + temperature + "]";
	}

}
