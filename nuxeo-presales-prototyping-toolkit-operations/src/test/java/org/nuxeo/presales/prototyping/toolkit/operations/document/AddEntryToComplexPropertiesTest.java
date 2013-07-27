/**
 * 
 */
package org.nuxeo.presales.prototyping.toolkit.operations.document;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;

import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.ecm.automation.core.operations.document.CreateDocument;
import org.nuxeo.ecm.automation.core.operations.document.DeleteDocument;
import org.nuxeo.ecm.automation.core.operations.document.FetchDocument;
import org.nuxeo.ecm.automation.test.EmbeddedAutomationServerFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import com.google.inject.Inject;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;
import org.nuxeo.runtime.test.runner.LocalDeploy;


/**
 * @author fvadon
 *
 */
@RunWith(FeaturesRunner.class)
@Deploy({
	"org.nuxeo.ecm.platform.url.api",
	"org.nuxeo.ecm.platform.url.core",
	"org.nuxeo.ecm.platform.types.api",
	"org.nuxeo.ecm.platform.types.core",
	"org.nuxeo.ecm.platform.notification.core:OSGI-INF/NotificationService.xml",
	"org.nuxeo.ecm.automation.test",
	"org.nuxeo.presales.prototyping.toolkit.operation.test",
"org.nuxeo.presales.prototyping.toolkit.operation"})

@LocalDeploy({ 
"org.nuxeo.presales.prototyping.toolkit.operation.test:OSGI-INF/core-type-contrib.xml"})
@Features(EmbeddedAutomationServerFeature.class)
@Jetty(port = 18080)
@RepositoryConfig(cleanup = Granularity.METHOD)
public class AddEntryToComplexPropertiesTest {

	@Inject
	Session session;
	@Inject
	AutomationService service;

	@Inject
	CoreSession coreSession;

	//protected static String[] attachments = { "att1", "att2", "att3" };


	protected Document automationTestFolder;



	/*    @Inject
    HttpAutomationClient client;*/


	@Before
	public void setupTestFolder() throws Exception {
		Document root = (Document) session.newRequest(FetchDocument.ID).set(
				"value", "/").execute();
		assertNotNull(root);
		assertEquals("/", root.getPath());
		automationTestFolder = (Document) session.newRequest(CreateDocument.ID).setInput(
				root).set("type", "Folder").set("name",
						"automation-test-folder").execute();
		assertNotNull(automationTestFolder);
	}

	@After
	public void tearDownTestFolder() throws Exception {
		session.newRequest(DeleteDocument.ID).setInput(automationTestFolder).execute();
	}

	/**
	 * Use to setup complex documents for related tests
	 */
	public void setupComplexDocuments() throws Exception {
		Document root = (Document) session.newRequest(FetchDocument.ID).set(
				"value", "/").execute();
		createDocumentWithComplexProperties(root);
	}

	/**
	 * Create document with no complex properties
	 */
	public void createDocumentWithComplexProperties(Document root)
			throws Exception {
		// Fill the document properties
		Map<String, Object> creationProps = new HashMap<String, Object>();
		creationProps.put("ds:tableName", "MyTable");
		creationProps.put("dc:title", "testDoc");

		// Document creation
		session.newRequest(CreateDocument.ID).setInput(root).set("type",
				"DataSet").set("name", "testDoc").set("properties",
						new PropertyMap(creationProps).toString()).execute();
	} 

	@Test
	public void shouldAddNewFieldsFromJsonString()
			throws Exception {
		// Initialize repository for this test
		setupComplexDocuments();

		// the repository init handler sould have created a sample doc in the
		// repo
		// Check that we see it
		Document testDoc = (Document) session.newRequest(
				DocumentService.GetDocumentChild).setHeader(
						Constants.HEADER_NX_SCHEMAS, "*").setInput(new PathRef("/")).set(
								"name", "testDoc").execute();
		DocumentModel testDocModel = coreSession.getChild(coreSession.getRootDocument().getRef(),"testDoc");
		assertNotNull(testDoc);
		assertNotNull(testDocModel);

		// Check there is no value already.
		assertNotNull(testDoc.getProperties().get("ds:fields"));
		assertEquals(testDoc.getProperties().getList("ds:fields").size(),0);

		// Get new fields from json file to String
		File fieldsAsJsonFile = FileUtils.getResourceFileFromContext("creationFields.json");
		assertNotNull(fieldsAsJsonFile);
		String fieldsDataAsJSon = FileUtils.readFile(fieldsAsJsonFile);
		fieldsDataAsJSon = fieldsDataAsJSon.replaceAll("\n", "");
		fieldsDataAsJSon = fieldsDataAsJSon.replaceAll("\r", "");

		//Add first fields
		OperationContext ctx = new OperationContext(coreSession);
		ctx.setInput(testDocModel);
		OperationChain chain = new OperationChain("testChain");
		chain.add(AddEntryToComplexProperties.ID).set("xpath", "ds:fields").set("ComplexJsonProperties", fieldsDataAsJSon);

		service.run(ctx, chain);


		testDoc = (Document) session.newRequest(
				DocumentService.GetDocumentChild).setHeader(
						Constants.HEADER_NX_SCHEMAS, "*").setInput(new PathRef("/")).set(
								"name", "testDoc").execute();

		PropertyList dbFields = testDoc.getProperties().getList("ds:fields");
		assertEquals(5, dbFields.size());

		// Get new fields from json file to String
		fieldsAsJsonFile = FileUtils.getResourceFileFromContext("newField.json");
		assertNotNull(fieldsAsJsonFile);
		fieldsDataAsJSon = FileUtils.readFile(fieldsAsJsonFile);
		fieldsDataAsJSon = fieldsDataAsJSon.replaceAll("\n", "");
		fieldsDataAsJSon = fieldsDataAsJSon.replaceAll("\r", "");
		
		// ADD new fields
		ctx = new OperationContext(coreSession);
		ctx.setInput(testDocModel);
		chain = new OperationChain("testChain");
		chain.add(AddEntryToComplexProperties.ID).set("xpath", "ds:fields").set("ComplexJsonProperties", fieldsDataAsJSon);

		service.run(ctx, chain);
		testDoc = (Document) session.newRequest(
				DocumentService.GetDocumentChild).setHeader(
						Constants.HEADER_NX_SCHEMAS, "*").setInput(new PathRef("/")).set(
								"name", "testDoc").execute();

		assertEquals("testDoc", testDoc.getTitle());		
		dbFields = testDoc.getProperties().getList("ds:fields");
		assertEquals(7, dbFields.size());

	}		


}
