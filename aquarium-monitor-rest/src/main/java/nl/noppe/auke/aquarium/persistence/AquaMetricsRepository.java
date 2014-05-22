package nl.noppe.auke.aquarium.persistence;

import nl.noppe.auke.aquarium.metrics.aqua.AquaMetrics;

import org.springframework.data.repository.CrudRepository;

public interface AquaMetricsRepository extends CrudRepository<AquaMetrics, String> {

}
