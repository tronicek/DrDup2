package edu.tarleton.drdup2.nicad;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The implementation of the NiCad output format.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
@XmlType(name = "clone", propOrder = {"nlines", "distance", "similarity", "sources"})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class NiCadClone {

    private Integer nlines;
    private Integer distance;
    private Integer similarity;
    private List<NiCadSource> sources;

    public NiCadClone() {
    }

    public NiCadClone(Integer nlines, Integer similarity, List<NiCadSource> sources) {
        this.nlines = nlines;
        this.similarity = similarity;
        this.sources = sources;
    }

    @XmlAttribute
    public Integer getNlines() {
        return nlines;
    }

    public void setNlines(Integer nlines) {
        this.nlines = nlines;
    }
    
    @XmlAttribute
    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @XmlAttribute
    public Integer getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Integer similarity) {
        this.similarity = similarity;
    }

    @XmlElement(name = "source")
    public List<NiCadSource> getSources() {
        return sources;
    }

    public void setSources(List<NiCadSource> sources) {
        this.sources = sources;
    }
}
