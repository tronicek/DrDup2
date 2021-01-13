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
@XmlType(name = "cloneinfo", propOrder = {"npcs", "npairs"})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CloneInfo {

    private int npcs;
    private int npairs;

    @XmlAttribute
    public int getNpcs() {
        return npcs;
    }

    public void setNpcs(int npcs) {
        this.npcs = npcs;
    }

    @XmlAttribute
    public int getNpairs() {
        return npairs;
    }

    public void setNpairs(int npairs) {
        this.npairs = npairs;
    }
}
