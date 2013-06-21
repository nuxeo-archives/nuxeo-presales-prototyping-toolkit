package org.nuxeo.presales.prototyping.toolkit.operations.notification;
/**
 * @author Stephane Lancone, Karl Harris
 *
 * The test cases for this Operation tests protocols of type FILE and type FTP. Blobs tested and transferred are a small text blob,
 * a jpeg image and a Quicktime (mov) video blob. To run this test you will need an FTP server AND File access on the localhost machine.
 * using an account with username = username and password = password
 *  (yes, the actual username and password are the same as the labels). There are a total of 4 tests available.
 *  
 *  1. Transfer a small text file to the <Eclipse Root>/target/TestTextFile.txt  folder on the local host.
 *  2. Tranfer a jpeg image to an ftp server using  ftp://username:password@localhost/Downloads/unittestImage.jpeg URI.
 *  3. Tranfer a mov(Quicktime) image to an ftp server using ftp://username:password@localhost/Downloads/unittestVideo.jpeg URI.
 *  4. Transfer a dynamically generated list of all 3 files above using ftp server using ftp://username:password@localhost/Downloads/*  URI.
 *  
 *  Note: The jpeg and mov files are referenced by a Maven dependency and stored in a .zip file. Maven will expand the jpeg and mov file and
 *  place them in a <Eclipse Root>/target/blobs folder. The small text file is generated when the test is initialized.

 */

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.DocRefs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.test.EmbeddedAutomationServerFeature;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.presales.prototyping.toolkit.operations.ftp.BlobExport;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({EmbeddedAutomationServerFeature.class})
@Jetty(port = 18080)
@Deploy("org.nuxeo.presales.prototyping.toolkit.operation")
@RepositoryConfig(init=CanTransferFiles.MyRepositoryInit.class)
public class CanTransferFiles {

	protected static final String BLOBS_IMG_0216_JPG = "blobs/IMG_0216.JPG";

	protected static final String BLOBS_IMG_0217_MOV = "blobs/IMG_0217.MOV";

	public static class MyRepositoryInit extends DefaultRepositoryInit {

		@Override
		public void populate(CoreSession session) throws ClientException {
			super.populate(session);

			DocumentModel text = session.createDocumentModel("/default-domain", "text-file", "File");
			text.setPropertyValue("file:content", new org.nuxeo.ecm.core.api.impl.blob.StringBlob("Nice, it's working!!!"));
			text.setPropertyValue("file:filename", "TestBlobText");
			text = session.createDocument(text);

			DocumentModel img = session.createDocumentModel("/default-domain", "img-file", "File");
			img.setPropertyValue("file:filename", "TestBlobImage");

			InputStream jpgBlob = CanTransferFiles.class.getClassLoader().getResourceAsStream(BLOBS_IMG_0216_JPG);
			Assert.assertTrue(jpgBlob != null);
			try {
				img.setPropertyValue("file:content", new FileBlob(jpgBlob));
			} catch (IOException e1) {

			}
			img = session.createDocument(img);

			DocumentModel video = session.createDocumentModel("/default-domain", "video-file", "File");
			video.setPropertyValue("file:filename", "TestBlobVideo");

			InputStream videoBlob = CanTransferFiles.class.getClassLoader().getResourceAsStream(BLOBS_IMG_0217_MOV);
			Assert.assertTrue(videoBlob != null);
			try {
				video.setPropertyValue("file:content", new FileBlob(videoBlob));
			} catch (IOException e) {

			}
			video = session.createDocument(video);
		}
	}

	protected @Inject
	HttpAutomationClient client;

	protected @Inject
	Session clientSession;

	protected @Inject CoreSession coreSession;

	protected Documents docList = new Documents();


	protected static class TestBlob{

		protected final String params;
		protected final Blob blobHere;
		protected final String file;

		protected TestBlob (Blob blob, String params,String fileSpec){
			this.blobHere = blob;
			this.params = params;
			this.file = fileSpec;
		}
	}

	protected String getVideoParams(){
		String result =  "ftp://username:password@localhost/Downloads/unittestVideo.mov";
		return result;
	}
	protected String getJPEGParams(){
		String result =  "ftp://username:password@localhost/Downloads/unittestImage.jpg";
		return result;
	}
	protected String getListParams(){
		String result =  "ftp://username:password@localhost/Downloads/*";
		return result;
	}
	protected String getFileParams(){
		String targetPath = guessTargetPath();
		String result = "file:".concat(targetPath);
		result = result.concat("TestTextFile.txt");
		return result;
	}


	protected String guessTargetPath() {
		String myclasspath = CanTransferFiles.class.getCanonicalName().replace('.', '/').concat(".class");
		String mypath = CanTransferFiles.class.getClassLoader().getResource(myclasspath).getFile();
		return mypath.substring(0, mypath.length()-myclasspath.length()).concat("../");
	}


	@Test
	public void canTransferDocFile() throws Exception {

		PathRef p1 = new PathRef("/default-domain/text-file");
		Assert.assertTrue(p1 !=null);
		clientSession.newRequest(BlobExport.ID).setInput(p1).set("URI", getFileParams() ).setHeader(Constants.HEADER_NX_VOIDOP, "true").execute();
	}

	@Test
	public void canTransferDocJPEG() throws Exception {
		PathRef p1 = new PathRef("/default-domain/img-file");
		Assert.assertTrue(p1 !=null);
		clientSession.newRequest(BlobExport.ID).setInput(p1).set("URI", getJPEGParams()).setHeader(Constants.HEADER_NX_VOIDOP, "true").execute();
	}
	@Test
	public void canTransferDocMOV() throws Exception {
		PathRef p1 = new PathRef("/default-domain/video-file");
		Assert.assertTrue(p1 !=null);
		clientSession.newRequest(BlobExport.ID).setInput(p1).set("URI", getVideoParams() ).setHeader(Constants.HEADER_NX_VOIDOP, "true").execute();
	}

	@Test
	public void canTransferDocList() throws Exception {
		Documents docs = (Documents) clientSession.newRequest("Document.Query")
				.set("query", "SELECT * FROM Document WHERE ecm:primaryType = 'File'")
				.execute();
		Assert.assertTrue(new Integer(docs.size()).toString(),docs.size() != 0);

		DocRefs refs = new DocRefs();

		for (Document x : docs){
			refs.add(new DocRef(x.getId()));
		}

		clientSession.newRequest(BlobExport.ID)
		.setInput(refs)
		.set("URI", getListParams() )
		.setHeader(Constants.HEADER_NX_VOIDOP, "true")
		.execute();

	}

}
