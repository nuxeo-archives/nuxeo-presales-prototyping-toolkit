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

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
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
import org.nuxeo.presales.prototyping.toolkit.operations.ftp.ExportBlob;
import org.nuxeo.presales.prototyping.toolkit.operations.ftp.DocumentBlobExport;
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

	protected static final String host = "localhost";
	protected static final String user = "username";
	protected static final String password = "password";
	
	protected static final String FILETEXTNAME = "TestBlobTextFile";
	protected static final String FILETEXTPATH = "/default-domain";
	protected static final String FILETEXTCONTENTID = "text-file";
	protected static final String FILETEXTID = ExportBlob.ID;
	protected static final String FILETEXTURI ="ftp://"+user+":"+password+"@"+host+"/Downloads/unittestTextFile.txt";
	
	protected static final String FILEIMAGENAME = "TestBlobImageFile";
	protected static final String FILEIMAGEPATH ="/default-domain";
	protected static final String FILEIMAGECONTENTID = "image-file";
	protected static final String FILEIMAGEID = ExportBlob.ID;
	protected static final String FILEIMAGEURI ="ftp://"+user+":"+password+"@"+host+"/Downloads/unittestImageFile.jpg";
	
	protected static final String FILEVIDEONAME = "TestBlobVideoFile";
	protected static final String FILEVIDEOPATH ="/default-domain";
	protected static final String FILEVIDEOCONTENTID = "video-file";
	protected static final String FILEVIDEOID = ExportBlob.ID;
	protected static final String FILEVIDEOURI =	"ftp://"+user+":"+password+"@"+host+"/Downloads/unittestVideoFile.mov";
	

	protected static final String DOCTEXTID = DocumentBlobExport.ID;
	protected static final String DOCTEXTURI =	"ftp://"+user+":"+password+"@"+host+"/Downloads/unittestTextDoc.txt";
	protected static final String DOCIMAGEURI =	"ftp://"+user+":"+password+"@"+host+"/Downloads/unittestImageDoc.jpg";
	protected static final String DOCVIDEOURI =	"ftp://"+user+":"+password+"@"+host+"/Downloads/unittestVideoDoc.mov";
	
	protected static final String BLOBS_IMG_0216_JPG = "blobs/IMG_0216.JPG";

	protected static final String BLOBS_IMG_0217_MOV = "blobs/IMG_0217.MOV";

	public static class MyRepositoryInit extends DefaultRepositoryInit {

		@Override
		public void populate(CoreSession session) throws ClientException {
			super.populate(session);

			//File Test Text
			
			DocumentModel fileText = session.createDocumentModel(FILETEXTPATH, FILETEXTCONTENTID, "File");
			fileText.setPropertyValue("file:content", new org.nuxeo.ecm.core.api.impl.blob.StringBlob("Nice, it's working!!!"));
			fileText.setPropertyValue("file:filename", FILETEXTNAME);
			fileText = session.createDocument(fileText);
			
			//File Image(JPEG) Test
			
			DocumentModel fileImg = session.createDocumentModel(FILEIMAGEPATH, FILEIMAGECONTENTID, "File");
			fileImg.setPropertyValue("file:filename", FILEIMAGENAME);
			InputStream jpgBlob = CanTransferFiles.class.getClassLoader().getResourceAsStream(BLOBS_IMG_0216_JPG);
			Assert.assertTrue(jpgBlob != null);
			try {
				fileImg.setPropertyValue("file:content", new FileBlob(jpgBlob));
			} catch (IOException e1) {

			}
			fileImg = session.createDocument(fileImg);
			
			//File Video(MOV) Test

			DocumentModel fileVideo = session.createDocumentModel(FILEVIDEOPATH, FILEVIDEOCONTENTID, "File");
			fileVideo.setPropertyValue("file:filename", FILEVIDEONAME);

			InputStream fileVideoBlob = CanTransferFiles.class.getClassLoader().getResourceAsStream(BLOBS_IMG_0217_MOV);
			Assert.assertTrue(fileVideoBlob != null);
			try {
				fileVideo.setPropertyValue("file:content", new FileBlob(fileVideoBlob));
			} catch (IOException e) {

			}
			fileVideo = session.createDocument(fileVideo);
			
		}
	}

	//protected @Inject HttpAutomationClient client;

	protected @Inject Session clientSession;

	//protected @Inject CoreSession coreSession;

	//protected Documents docList = new Documents();


/*	protected String getListFileParams(){
		String result =  "ftp://username:password@localhost/Downloads/*";
		return result;
	}*/
/*	protected String getTextFileParams(){
		String targetPath = guessTargetPath();
		String result = "file:".concat(targetPath);
		result = result.concat("TestTextFile.txt");
		return result;
	}*/
	
/*	protected String getListDocParams(){
		String result =  "ftp://username:password@localhost/Downloads/*";
		return result;
	}*/
	
/*	protected String getTextDocParams(){
		String targetPath = guessTargetPath();
		String result = "file:".concat(targetPath);
		result = result.concat("TestTextDoc.txt");
		return result;
	}*/

/*	protected String guessTargetPath() {
		String myclasspath = CanTransferFiles.class.getCanonicalName().replace('.', '/').concat(".class");
		String mypath = CanTransferFiles.class.getClassLoader().getResource(myclasspath).getFile();
		return mypath.substring(0, mypath.length()-myclasspath.length()).concat("../");
	}*/


	protected void transfer (String path, String id, String uri) throws Exception {
		PathRef p1 = new PathRef(path);
		Assert.assertTrue(p1 !=null);
		clientSession.newRequest(id).setInput(p1).set("URI", uri )
		.setHeader(Constants.HEADER_NX_VOIDOP, "true")
		.execute();
	}

	
	@Test
	public void canTransferTextFile() throws Exception {
		transfer(FILETEXTPATH+"/"+FILETEXTCONTENTID,FILETEXTID,FILETEXTURI);
	}
	@Test
	public void canTransferIMAGEFile() throws Exception {
		transfer(FILEIMAGEPATH+"/"+FILEIMAGECONTENTID,FILEIMAGEID,FILEIMAGEURI);
	}
	@Test
	public void canTransferVIDEOFile() throws Exception {
		transfer(FILEVIDEOPATH+"/"+FILEVIDEOCONTENTID,FILEVIDEOID,FILEVIDEOURI);
	}

	@Test
	public void canTransferTextDoc() throws Exception {
		transfer(FILETEXTPATH+"/"+FILETEXTCONTENTID,DOCTEXTID,DOCTEXTURI);
	}
	@Test
	public void canTransferIMAGEDoc() throws Exception {
		transfer(FILEIMAGEPATH+"/"+FILEIMAGECONTENTID,DOCTEXTID,DOCIMAGEURI);
	}
	@Test
	public void canTransferVIDEODoc() throws Exception {
		transfer(FILEVIDEOPATH+"/"+FILEVIDEOCONTENTID,DOCTEXTID,DOCVIDEOURI);
	}
 /*
  * @Test
	@Ignore
	public void canTransferDocList() throws Exception {
		Documents docs = (Documents) clientSession.newRequest("Document.Query")
				.set("query", "SELECT * FROM Document WHERE ecm:primaryType = 'File'")
				.execute();
		Assert.assertTrue(new Integer(docs.size()).toString(),docs.size() != 0);

		DocRefs refs = new DocRefs();

		for (Document x : docs){
			refs.add(new DocRef(x.getId()));
		}

		clientSession.newRequest(ExportBlob.ID)
		.setInput(refs)
		.set("URI", getListParams() )
		.setHeader(Constants.HEADER_NX_VOIDOP, "true")
		.execute();

	}
	*/
	

}
