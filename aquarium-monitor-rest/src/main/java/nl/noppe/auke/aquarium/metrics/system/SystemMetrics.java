package nl.noppe.auke.aquarium.metrics.system;

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
@Table(name="tbl_system_metrics")
public class SystemMetrics implements Metrics {

	@Id
	@GeneratedValue
	@Column(name="system_metrics_id")
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="occured_datetime")
	private Date occuredDatetime;
	@Column(name="used_memory")
	private Long usedMemory;
	@Column(name="free_memory")
	private Long freeMemory;
	@Column(name="cpu_utilization")
	private Double cpuUtilization;
	@Column(name="used_swap")
	private Long usedSwap;
	@Column(name="available_swap")
	private Long availableSwap;

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

	public Long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public Long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(Long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public Double getCpuUtilization() {
		return cpuUtilization;
	}

	public void setCpuUtilization(Double cpuUtilization) {
		this.cpuUtilization = cpuUtilization;
	}

	public Long getUsedSwap() {
		return usedSwap;
	}

	public void setUsedSwap(Long usedSwap) {
		this.usedSwap = usedSwap;
	}

	public Long getAvailableSwap() {
		return availableSwap;
	}

	public void setAvailableSwap(Long availableSwap) {
		this.availableSwap = availableSwap;
	}

	@Override
	public String toString() {
		return "SystemMetrics [id=" + id + ", occuredDatetime=" + occuredDatetime + ", usedMemory=" + usedMemory + ", freeMemory=" + freeMemory + ", cpuUtilization=" + cpuUtilization + ", usedSwap="
				+ usedSwap + ", availableSwap=" + availableSwap + "]";
	}
	}
