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

import org.suresoft.sscroll.jiraWorklogClient.entity.User;
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
	 * Get value of the tag
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
	 * Returns the issue keys by List of String
	 * @return
	 */
	public List<String> getIssueKeys() {
		List<String> issueKeys = new ArrayList<String>();

		if ( rootDocument != null ) {
			Element issueKeyListElement = (Element) rootElement.getElementsByTagName(XmlTag.ISSUE_KEY_LIST.getName()).item(0);
			NodeList issueKeyNodeList = issueKeyListElement.getElementsByTagName(XmlTag.ISSUE_KEY.getName());

			for (int i = 0; i < issueKeyNodeList.getLength(); ++i) {
				Node issueKeyNode = issueKeyNodeList.item(i);
				
				if (issueKeyNode.getNodeType() == Node.ELEMENT_NODE) {
					Element issueKeyElement = (Element) issueKeyNode;
					issueKeys.add(issueKeyElement.getTextContent());
				}
			}
		}

		return issueKeys;
	}

	/**
	 * Returns the users by List of User
	 * @return
	 */
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		
		if (rootDocument != null) {
			Element userListElement = (Element) rootElement.getElementsByTagName(XmlTag.USER_LIST.getName()).item(0);
			NodeList userNodeList = userListElement.getElementsByTagName(XmlTag.USER.getName());

			for (int i = 0; i < userNodeList.getLength(); ++i) {
				Node userNode = userNodeList.item(i);
				
				if (userNode.getNodeType() == Node.ELEMENT_NODE) {
					Element userElement = (Element) userNode;
					
					User user = new User();
					user.setId(userElement.getAttribute(XmlAttribute.ID.getName()));
					user.setName(userElement.getAttribute(XmlAttribute.NAME.getName()));
					
					String selectedState = userElement.getAttribute(XmlAttribute.SELECTED.getName());
					boolean isSelected = Boolean.parseBoolean(selectedState);
					user.setSelected(isSelected);
					
					users.add(user);
				}
			}
		}

		return users;
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
	 * Make an element "issueKeyList" having children named "issueKey" 
	 * whose value is a String value in the issueKeys passed by
	 * @param issueKeys
	 */
	public void setIssueKeys(final List<String> issueKeys) {
		if ( rootElement != null && issueKeys != null ) {
			Element issueKeyListElement = rootDocument.createElement(XmlTag.ISSUE_KEY_LIST.getName());
			
			for (final String issue : issueKeys) {
				Element issueKeyElement = rootDocument.createElement(XmlTag.ISSUE_KEY.getName());
				issueKeyElement.appendChild(rootDocument.createTextNode(issue));
				
				issueKeyListElement.appendChild(issueKeyElement);
			}
				
			rootElement.appendChild(issueKeyListElement);
		}
	}

	/**
	 * Make an element "userList" having children named "user"
	 * whose attributes are in line with User information for each user
	 * @param users
	 */
	public void setUsers(final List<User> users) {
		if( rootElement != null && users != null ) {
			Element userListElement = rootDocument.createElement(XmlTag.USER_LIST.getName());
			
			for (final User user : users) {
				Element userElement = rootDocument.createElement(XmlTag.USER.getName());
				userElement.setAttribute(XmlAttribute.ID.getName(), user.getId());
				userElement.setAttribute(XmlAttribute.NAME.getName(), user.getName());
				userElement.setAttribute(XmlAttribute.SELECTED.getName(), Boolean.toString(user.isSelected()));
				
				userListElement.appendChild(userElement);
			}
				
			rootElement.appendChild(userListElement);
		}
	}

}
