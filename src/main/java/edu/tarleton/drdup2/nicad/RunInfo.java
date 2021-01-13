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
@XmlType(name = "runinfo", propOrder = {"ncompares", "cputime"})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RunInfo {

    private long ncompares;
    private long cputime;

    @XmlAttribute
    public long getNcompares() {
        return ncompares;
    }

    public void setNcompares(long ncompares) {
        this.ncompares = ncompares;
    }

    @XmlAttribute
    public long getCputime() {
        return cputime;
    }

    public void setCputime(long cputime) {
        this.cputime = cputime;
    }
}
