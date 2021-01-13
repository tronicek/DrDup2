package edu.tarleton.drdup2.nicad;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * The implementation of the NiCad output format.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
@XmlType(name = "source", 
        propOrder = {"file", "startline", "startcolumn", "endline", "endcolumn"}
)
@XmlAccessorType(XmlAccessType.PROPERTY)
public class NiCadSource {

    private String file;
    private Integer startline;
    private Integer startcolumn;
    private Integer endline;
    private Integer endcolumn;
    private String sourceCode;

    public NiCadSource() {
    }

    public NiCadSource(String file, Integer startline, Integer startcolumn, Integer endline, Integer endcolumn) {
        this.file = file;
        this.startline = startline;
        this.startcolumn = startcolumn;
        this.endline = endline;
        this.endcolumn = endcolumn;
    }

    @XmlAttribute
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @XmlAttribute
    public Integer getStartline() {
        return startline;
    }

    public void setStartline(Integer startline) {
        this.startline = startline;
    }

    @XmlAttribute
    public Integer getStartcolumn() {
        return startcolumn;
    }

    public void setStartcolumn(Integer startcolumn) {
        this.startcolumn = startcolumn;
    }

    @XmlAttribute
    public Integer getEndline() {
        return endline;
    }

    public void setEndline(Integer endline) {
        this.endline = endline;
    }

    @XmlAttribute
    public Integer getEndcolumn() {
        return endcolumn;
    }

    public void setEndcolumn(Integer endcolumn) {
        this.endcolumn = endcolumn;
    }

    @XmlValue
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NiCadSource) {
            NiCadSource that = (NiCadSource) obj;
            return file.equals(that.file)
                    && startline.equals(that.startline)
                    && endline.equals(that.endline);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, startline, endline);
    }
}
