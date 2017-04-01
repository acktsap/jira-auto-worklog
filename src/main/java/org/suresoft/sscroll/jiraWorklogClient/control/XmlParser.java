package org.suresoft.sscroll.jiraWorklogClient.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {

	private Document rootDocument;
	private Element rootElement;
	
	public XmlParser() {
		makeNewDocument(XmlTag.ROOT.getName());
	}
	
	public XmlParser(final String fileName) {
		parse(fileName);
	}
	
	/**
	 * parse xml file and set rootDocument, rootElement for the file
	 * @param fileName
	 */
	public void parse(final String fileName)  {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			rootDocument = documentBuilder.parse(new File(fileName));
			rootDocument.getDocumentElement().normalize(); // optional, but recommended
			rootElement = rootDocument.getDocumentElement();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {	// catch SAXException, IOException
			rootDocument = null;
			rootElement = null;
			System.out.println("No file");
		} 
	}

	/**
	 * Get value whose tag is "tag"
	 * @param tag
	 * @return
	 */
	public String getValue(final XmlTag tag) {
		String value = "";
		if( tag != null && rootDocument != null ) {
			NodeList elements = rootElement.getElementsByTagName(tag.getName());
			value = elements.item(0).getTextContent();
		}
		return value;
	}
	
	/**
	 * Get list of value whose root tag is "tag" 
	 * @param tag
	 * @return list of value
	 */
	public List<String> getValueList(final XmlTag tag) {
		ArrayList<String> valueList = new ArrayList<String>();
		
		if( tag != null && rootDocument != null ) {
			if( tag.hasChild() ) {
				Element nameListElement = (Element) rootElement.getElementsByTagName(tag.getName()).item(0);
				NodeList nameList = nameListElement.getElementsByTagName(tag.getChildName());

				for( int i = 0; i < nameList.getLength(); ++i ) {
					Node nameNode = nameList.item(i);
					if (nameNode.getNodeType() == Node.ELEMENT_NODE) {
						Element nameElement = (Element) nameNode;
						String textContent = nameElement.getTextContent();
						valueList.add(textContent);
					}
				}
			} 
		}
		return valueList;
	}
	
	/**
	 * Make new Document object whose root tag is rootTag
	 * @param rootTag
	 */
	public void makeNewDocument(final String rootTag) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			rootDocument = documentBuilder.newDocument();
			rootElement = rootDocument.createElement(rootTag);
			rootDocument.appendChild(rootElement);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save current rootDocument to xml file named "fileName"
	 * @param fileName
	 */
	public void save(final String fileName) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			// set intent with 2
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
			DOMSource domSource = new DOMSource(rootDocument);
			StreamResult result = new StreamResult(new File(fileName));
//			StreamResult result = new StreamResult(System.out);	// for testing
			transformer.transform(domSource, result);

		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Make an element whose tag and value is "tag", "value" respectively.
	 * @param tag
	 * @param value
	 */
	public void setValue(final XmlTag tag, final String value) {
		if( tag != null && value != null && rootDocument != null ) {
			Element childElement = rootDocument.createElement(tag.getName());
			childElement.appendChild(rootDocument.createTextNode(value));
			rootElement.appendChild(childElement);
		}
	}
	
	
	/**
	 * Make an elements whose root tag is the "tag" and value is an element of "values".
	 * @param tag
	 * @param values
	 */
	public void setValueList(final XmlTag tag, final List<String> values) {
		if( tag != null && values != null && rootDocument != null ) {
			Element childElement = rootDocument.createElement(tag.getName());
			if( tag.hasChild() ) {
				for( final String value : values ) {
					Element nameElement = rootDocument.createElement(tag.getChildName());
					nameElement.appendChild(rootDocument.createTextNode(value));
					childElement.appendChild(nameElement);
				}
			}
			rootElement.appendChild(childElement);
		}
	}

}
