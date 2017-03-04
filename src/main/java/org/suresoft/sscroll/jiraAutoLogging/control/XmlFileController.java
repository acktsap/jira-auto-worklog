package org.suresoft.sscroll.jiraAutoLogging.control;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlFileController {

	public enum Tag {
		IP("ip"),
		PORT("port"),
		AUTHOR("author"),
		PASSWORD("password"),
		ISSUE_KEY("issuekey"),
		NAME_LIST("namelist"),
		NAME("name"),
		DATE("date"),
		TIME_SPENT("timespent"),
		COMMENT("comment");
		
		private String name;
		
		Tag(final String name) {
			this.setName(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	private String fileName;
	private File file;
	private Element rootElement;
	
	public XmlFileController() {
		this.fileName = "";
	}
	
	public XmlFileController(final String fileName) {
		this.setFileName(fileName);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public void parse()  {
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(getFile());
			document.getDocumentElement().normalize(); // optional, but recommended
			rootElement = document.getDocumentElement();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public String getValue(final Tag tag) {
		String value = "";
		if( tag == Tag.NAME_LIST ) {
			Element nameListElement = (Element) rootElement.getElementsByTagName(Tag.NAME_LIST.getName()).item(0);
			NodeList nameList = nameListElement.getElementsByTagName(Tag.NAME.getName());

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
		return value;
	}

	public void setElementValue(final Tag tag, final String value) {
		// TODO Auto-generated method stub
		
	}

	public void load() {
		load(getFileName());
	}
	
	public void load(final String fileName) {
		setFile(new File(fileName));
	}
	
}
