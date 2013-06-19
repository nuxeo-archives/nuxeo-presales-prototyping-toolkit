package org.nuxeo.presales.prototyping.toolkit.operations.notification;


import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.StreamBlob;
import org.nuxeo.ecm.automation.client.model.StringBlob;
import org.nuxeo.ecm.automation.test.EmbeddedAutomationServerFeature;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.presales.prototyping.toolkit.operations.ftp.FTPExportII;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(EmbeddedAutomationServerFeature.class)
@Jetty(port = 18080)
@Deploy("org.nuxeo.presales.prototyping.toolkit.operation")
public class CanTransferFiles {

	protected @Inject
	HttpAutomationClient client;

	protected @Inject
	Session session;

	protected Set<TestBlob> blobList = new HashSet<TestBlob>();

	@Before
	public void init() throws Exception {
		blobList.add(getTextBlob());
		blobList.add(getJPEGBlob());
		blobList.add(getMOVBlob());

	}
	
	public class TestBlob{

		Map<String,Object> params;
		Blob blobHere;

		public TestBlob (Blob blob, Map<String,Object> params){
			blobHere = blob;
			this.params = params;
		}

		public Map<String,Object> getParams() {
			return params;
		}
		public Blob getBlobHere() {
			return blobHere;
		}
	}
	
	protected Map<String,Object> getParams(){
		Map<String, Object> ctx = new HashMap<String, Object>();		
		ctx.put("ServerURL", "localhost");
		ctx.put("Username", "FTPUnitTest");
		ctx.put("Password", "TestFTP");
		ctx.put("Path", "/Downloads/");
		ctx.put("Filename", "unittestFile");
		return ctx;
	}


	protected TestBlob getTextBlob(){
		Blob blob = (Blob) new StringBlob("FTP string blob test file contents");
		blob.setMimeType("text/plain");
		return new TestBlob(blob, getParams());
	}

	protected TestBlob getJPEGBlob() {
		String jpegFile = "/blobs/IMG_0216.JPG";
		return getBlob(jpegFile, "FTPJPEGTest", "image/jpeg");
	}

	protected TestBlob getMOVBlob() {
		String movFile = "/blobs/IMG_0217.MOV";
		return getBlob(movFile,"FTPMOVTest", "video/quicktime");
	}

	protected TestBlob getBlob(String fileSpec, String fileName, String mimeType) {

		InputStream in;
		TestBlob result = null;
		in = this.getClass().getResourceAsStream(fileName);
		Blob blob = (Blob) new StreamBlob(in, fileName, mimeType);
		result = new TestBlob(blob,getParams());
		return result;
	}

	public OperationRequest injectParams(OperationRequest req, Map<String,Object> blobParams) throws Exception {
		Set<String> param = blobParams.keySet();
		for (String x : param) {
			req.set(x, blobParams.get(x));
		}
		return req;

	}

	@Test
	public void canTransferFile() throws Exception {

		for (TestBlob blobToTest : blobList ){
			injectParams(session.newRequest(FTPExportII.ID).setInput(blobToTest.getBlobHere()),blobToTest.getParams())
			.setHeader(Constants.HEADER_NX_VOIDOP, "true").execute();
		}
	}
}
