//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.12 at 11:03:39 AM EST 
//


package gov.nysenate.openleg.xml.calendar;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}caldate"/>
 *         &lt;element ref="{}releasedate"/>
 *         &lt;element ref="{}releasetime"/>
 *         &lt;element ref="{}sections"/>
 *         &lt;element ref="{}sequence"/>
 *       &lt;/choice>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "caldate",
    "releasedate",
    "releasetime",
    "sections",
    "sequence"
})
@XmlRootElement(name = "supplemental")
public class XMLSupplemental {

    protected XMLCaldate caldate;
    protected XMLReleasedate releasedate;
    protected XMLReleasetime releasetime;
    protected XMLSections sections;
    protected XMLSequence sequence;
    @XmlAttribute(required = true)
    protected String id;

    /**
     * Gets the value of the caldate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLCaldate }
     *     
     */
    public XMLCaldate getCaldate() {
        return caldate;
    }

    /**
     * Sets the value of the caldate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLCaldate }
     *     
     */
    public void setCaldate(XMLCaldate value) {
        this.caldate = value;
    }

    /**
     * Gets the value of the releasedate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLReleasedate }
     *     
     */
    public XMLReleasedate getReleasedate() {
        return releasedate;
    }

    /**
     * Sets the value of the releasedate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLReleasedate }
     *     
     */
    public void setReleasedate(XMLReleasedate value) {
        this.releasedate = value;
    }

    /**
     * Gets the value of the releasetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLReleasetime }
     *     
     */
    public XMLReleasetime getReleasetime() {
        return releasetime;
    }

    /**
     * Sets the value of the releasetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLReleasetime }
     *     
     */
    public void setReleasetime(XMLReleasetime value) {
        this.releasetime = value;
    }

    /**
     * Gets the value of the sections property.
     * 
     * @return
     *     possible object is
     *     {@link XMLSections }
     *     
     */
    public XMLSections getSections() {
        return sections;
    }

    /**
     * Sets the value of the sections property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLSections }
     *     
     */
    public void setSections(XMLSections value) {
        this.sections = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link XMLSequence }
     *     
     */
    public XMLSequence getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLSequence }
     *     
     */
    public void setSequence(XMLSequence value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
