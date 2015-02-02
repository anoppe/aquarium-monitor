package nl.noppe.auke.aquarium.persistence;

import java.util.Date;
import java.util.List;

import nl.noppe.auke.aquarium.metrics.aqua.AquaMetrics;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AquaMetricsRepository extends CrudRepository<AquaMetrics, String> {

	@Query("FROM AquaMetrics a where a.occuredDatetime > :from ORDER BY a.occuredDatetime")
	List<AquaMetrics> findByDateTimeGreaterThan(@Param("from") Date from);
}
