/**
 * 
 */

package org.nuxeo.presales.prototyping.toolkit.operations.ftp;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;

/**
 * @author stephane lancoin, karl harris
 * 
 * This Operation takes a single Parameter. A URI of the form <protocol>://<username>:<password>@hostname/<folder>/filename.
 * Takes as input a single Document or a List of Documents each document must be of type File and contain a blob of bytes. The Operation
 * will uses the Apache VFS software to transfer the file using the <protocol> named in the URI.
 * Check http://commons.apache.org/proper/commons-vfs/filesystems.html for other protocols in this library.
 * 
 */


@Operation(id = BlobExport.ID, category = Constants.CAT_DOCUMENT, label = "BlobExport", description = "Export Blob from a Document use URI format:<protocol>://<username>:<password>@hostname/<folder>/filename " )
public class BlobExport {

	public static final String ID = "BlobExport";

	protected static FileSystemOptions defaultOpts = new FileSystemOptions();

	protected static FileSystemOptions ftpOpts = initFTP();

	protected Log log = LogFactory.getLog(BlobExport.class);

	protected @Context CoreSession session;

	protected @Param(name = "URI") String uriParams;

	@OperationMethod(collector = DocumentModelCollector.class)
	public DocumentModel run(DocumentModel input) throws Exception {
		BlobHolder blobHolder = input.getAdapter(BlobHolder.class);
		if (blobHolder == null) {
			throw new IllegalArgumentException(
					"wrong input, not a blob holder " + input.getPathAsString());
		}
		Blob blob = blobHolder.getBlob();
		
		FileSystemManager fsManager;
		fsManager = VFS.getManager();
		FileSystemOptions opts = findOpts(uriParams);

		FileObject blobFile = fsManager.resolveFile(uriParams, opts);
		if (!blobFile.exists()) {
			blobFile.createFile();
		}
		FileContent blobContent = blobFile.getContent();
		try {
			OutputStream os = blobContent.getOutputStream();
			try {
				blob.transferTo(os);
			} finally {
				os.close();
			}
		} finally {
			if (blobFile.isContentOpen()) {
				blobFile.close();
			}
		}

		return input;
	}

	protected FileSystemOptions findOpts(String uriParams)
			throws URISyntaxException, MalformedURLException {
		URI uri = new URI(uriParams);
		String proto = uri.toURL().getProtocol();
		if ("ftp".equals(proto)) {
			return ftpOpts;
		}
		return defaultOpts;
	}

	protected static FileSystemOptions initFTP() {
		FileSystemOptions opts = new FileSystemOptions();

		FtpFileSystemConfigBuilder configBuilder = FtpFileSystemConfigBuilder
				.getInstance();
		configBuilder.setUserDirIsRoot(opts, true);
		return opts;
	}

}
