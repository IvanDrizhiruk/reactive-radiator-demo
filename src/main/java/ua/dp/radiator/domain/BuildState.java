package ua.dp.radiator.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BuildState.
 */
@Entity
@Table(name = "build_state")
public class BuildState implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "instances_name")
    private String instancesName;

    @Column(name = "state")
    private String state;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "last_run_timestemp")
    private Long lastRunTimestemp;

    @Column(name = "extracting_date")
    private ZonedDateTime extractingDate;

    @Column(name = "build_id")
    private Integer buildId;

    private boolean buildInProgress;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstancesName() {
        return instancesName;
    }

    public void setInstancesName(String instancesName) {
        this.instancesName = instancesName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getLastRunTimestemp() {
        return lastRunTimestemp;
    }

    public void setLastRunTimestemp(Long lastRunTimestemp) {
        this.lastRunTimestemp = lastRunTimestemp;
    }

    public ZonedDateTime getExtractingDate() {
        return extractingDate;
    }

    public void setExtractingDate(ZonedDateTime extractingDate) {
        this.extractingDate = extractingDate;
    }

    public boolean isBuildInProgress() {
        return buildInProgress;
    }

    public void setBuildInProgress(boolean buildInProgress) {
        this.buildInProgress = buildInProgress;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BuildState buildState = (BuildState) o;
        if(buildState.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, buildState.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BuildState{" +
            "id=" + id +
            ", instancesName='" + instancesName + "'" +
            ", state='" + state + "'" +
            ", errorMessage='" + errorMessage + "'" +
            ", lastRunTimestemp='" + lastRunTimestemp + "'" +
            ", extractingDate='" + extractingDate + "'" +
            '}';
    }
}
