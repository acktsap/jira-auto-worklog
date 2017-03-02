package org.suresoft.sscroll.jiraAutoLogging.control;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.suresoft.sscroll.jiraAutoLogging.control.XmlFileController.Element;

public class XmlFileController {

	public enum Element {
		IP("ip"),
		PORT("port"),
		AUTHOR("author"),
		PASSWORD("password"),
		ISSUE_KEY("issuekey"),
		NAME_LIST("namelist"),
		DATE("date"),
		TIME_SPENT("timespent"),
		COMMENT("comment");
		
		private String name;
		
		Element(final String name) {
			this.setName(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	private File file;
	
	public XmlFileController() {
		file = null;
	}
	
	public XmlFileController(final String fileName) {
		file = new File(fileName);
	}
	
	public void parse() {
		// TODO
		
	}

	public String getValue(final Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setElementValue(Element ip, String text) {
		// TODO Auto-generated method stub
		
	}
	
}
