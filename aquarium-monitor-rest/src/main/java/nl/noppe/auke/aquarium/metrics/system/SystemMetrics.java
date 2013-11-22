package nl.noppe.auke.aquarium.metrics.system;

import java.util.Date;

import nl.noppe.auke.aquarium.metrics.Metrics;

public class SystemMetrics implements Metrics {

	private Long id;
	private Date occuredDatetime;
	private Long usedMemory;
	private Long freeMemory;
	private Double cpuUtilization;
	private Long usedSwap;
	private Long availableSwap;

	public Long getId() {
		return id;
	}

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
}
