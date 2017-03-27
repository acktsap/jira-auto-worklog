package org.suresoft.sscroll.jiraWorklogClient.control;

import java.io.File;

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

	public static enum Tag {
		ROOT("information"),
		IP("ip"),
		PORT("port"),
		AUTHOR("author"),
		PASSWORD("password"),	// not used
		ISSUE_KEY("issuekey"),
		ID_LIST("idlist"),
		ID("id"),
		DATE("date"),	// not used
		TIME_SPENT("timespent"),
		COMMENT("comment");
		
		private String name;
		
		Tag(final String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
	private Document rootDocument;
	private Element rootElement;
	
	public XmlParser() {
		makeNewDocument(Tag.ROOT.getName());
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
	 * Get value from root element for the corresponding tag
	 * @param tag
	 * @return
	 */
	public String getValue(final Tag tag) {
		String value = "";
		if( rootDocument != null ) {
			if( tag == Tag.ID_LIST ) {
				Element nameListElement = (Element) rootElement.getElementsByTagName(Tag.ID_LIST.getName()).item(0);
				NodeList nameList = nameListElement.getElementsByTagName(Tag.ID.getName());

				for( int i = 0; i < nameList.getLength(); ++i ) {
					Node nameNode = nameList.item(i);
					if (nameNode.getNodeType() == Node.ELEMENT_NODE) {
						Element nameElement = (Element) nameNode;
						if( i != 0 ) {
							value += " ";
						}
						value += nameElement.getTextContent();
					}
				}
			} else {
				value = rootElement.getElementsByTagName(tag.getName()).item(0).getTextContent();
			}
		}
		return value;
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
	 * Make an element with tag and its value
	 * @param tag
	 * @param value
	 */
	public void setElementValue(final Tag tag, final String value) {
		if( rootDocument != null ) {
			Element childElement = rootDocument.createElement(tag.getName());
			if(tag == Tag.ID_LIST) {
				String[] names = value.split(" ");
				for( String name : names ) {
					Element nameElement = rootDocument.createElement(Tag.ID.getName());
					nameElement.appendChild(rootDocument.createTextNode(name));
					childElement.appendChild(nameElement);
				}
			} else {
				childElement.appendChild(rootDocument.createTextNode(value));
			}
			rootElement.appendChild(childElement);
		}
	}

}
