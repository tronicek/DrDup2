package edu.tarleton.drdup2.nicad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * The implementation of the NiCad output format.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
@XmlType(name = "systeminfo", propOrder = {
    "processor", "system", "granularity", "threshold", "minlines", "maxlines"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SystemInfo {

    private String processor;
    private String system;
    private String granularity;
    private String threshold;
    private int minlines;
    private int maxlines;

    @XmlAttribute
    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    @XmlAttribute
    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @XmlAttribute
    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    @XmlAttribute
    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    @XmlAttribute
    public int getMinlines() {
        return minlines;
    }

    public void setMinlines(int minlines) {
        this.minlines = minlines;
    }

    @XmlAttribute
    public int getMaxlines() {
        return maxlines;
    }

    public void setMaxlines(int maxlines) {
        this.maxlines = maxlines;
    }
}
