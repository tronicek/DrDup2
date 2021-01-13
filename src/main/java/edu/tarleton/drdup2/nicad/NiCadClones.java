package edu.tarleton.drdup2.nicad;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The implementation of the NiCad output format.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
@XmlRootElement(name = "clones")
@XmlType(name = "clones", 
        propOrder = {"systemInfo", "cloneInfo", "runInfo", "clones"}
)
@XmlAccessorType(XmlAccessType.PROPERTY)
public class NiCadClones {

    private SystemInfo systemInfo;
    private CloneInfo cloneInfo;
    private RunInfo runInfo;
    private List<NiCadClone> clones = new ArrayList<>();

    public NiCadClones() {
    }

    public NiCadClones(List<NiCadClone> clones) {
        this.clones = clones;
    }

    @XmlElement(name = "systeminfo")
    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    @XmlElement(name = "cloneinfo")
    public CloneInfo getCloneInfo() {
        return cloneInfo;
    }

    public void setCloneInfo(CloneInfo cloneInfo) {
        this.cloneInfo = cloneInfo;
    }

    @XmlElement(name = "runinfo")
    public RunInfo getRunInfo() {
        return runInfo;
    }

    public void setRunInfo(RunInfo runInfo) {
        this.runInfo = runInfo;
    }

    @XmlElement(name = "clone")
    public List<NiCadClone> getClones() {
        return clones;
    }

    public void setClones(List<NiCadClone> clones) {
        this.clones = clones;
    }
}
