package ua.dp.radiator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.dp.radiator.domain.BuildState;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the BuildState entity.
 */
@SuppressWarnings("unused")
public interface BuildStateRepository extends JpaRepository<BuildState,Long> {

//    @Query("select distinct buildState from BuildState buildState left join fetch buildState.commiters")
//    List<BuildState> findAllWithEagerRelationships();

//    @Query("select buildState from BuildState buildState left join fetch buildState.commiters where buildState.id =:id")
//    BuildState findOneWithEagerRelationships(@Param("id") Long id);

    Optional<BuildState> findOneByInstancesNameAndStateAndLastRunTimestemp(String instancesName, String state, Long lastRunTimestemp);

//    @Query(value = "" +
//            "SELECT bs.ID, bs.INSTANCES_NAME, bs.STATE, bs.ERROR_MESSAGE, bs.LAST_RUN_TIMESTEMP, bs.EXTRACTING_DATE " +
//            /*", c.ID as commiters " +*/
//            /*", bsc.BUILD_STATES_ID, bsc.COMMITERS_ID" +*/
//            /*", bsc.COMMITERS_ID as commiter " +*/
//            "FROM BUILD_STATE  bs " +
//            "  INNER JOIN (" +
//            "    SELECT " +
//            "      MAX(ID) as ID," +
//            "      MAX(LAST_RUN_TIMESTEMP) " +
//            "    FROM BUILD_STATE " +
//            "    GROUP BY INSTANCES_NAME) jbs " +
//            "  ON bs.ID = jbs.ID" /*+
//            "   left join BUILD_STATE_COMMITER  bsc on bsc.BUILD_STATES_ID = bs.ID "*/ /*+
//            "   left join COMMITER c   on bsc.COMMITERS_ID  = c.ID "*/
//            , nativeQuery = true)
//    List<BuildState> findLastBuildStates();

    @Query("select MAX(buildState) from BuildState buildState GROUP BY buildState.instancesName")
    List<BuildState> findLastBuildStates();
}
