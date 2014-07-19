package nl.noppe.auke.aquarium.persistence;

import java.util.Date;
import java.util.List;

import nl.noppe.auke.aquarium.metrics.aqua.AquaMetrics;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AquaMetricsRepository extends CrudRepository<AquaMetrics, String> {

	@Query("{ 'occuredDatetime' : {'$gt' : ?0} }")
	List<AquaMetrics> findByDateTimeGreaterThan(Date from);
}
