/**
 * 
 */

package org.nuxeo.presales.prototyping.toolkit.operations.ftp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.BlobCollector;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;

/**
 * @author karlharris
 */
@Operation(id = FTPExportII.ID, category = Constants.CAT_BLOB, label = "FTPExportII", description = "Export via FTP using Apache VFS")
public class FTPExportII {

	public static final String ID = "FTPExportII";

	protected Log log = LogFactory.getLog(FTPExportII.class);

	@Param(name = "ServerURL")
	String serverURL;
	@Param(name = "Username")
	String ftp_UserName;
	@Param(name = "Password")
	String ftp_Password;
	@Param(name = "Path")
	String ftp_Path;
	@Param(name = "Filename")
	String ftp_Filename;
	//@Param(name="Mime-type")
	//String mime_Type;

	@OperationMethod
	public Blob run(Blob input) throws IOException {

		log.info(" Transferring file to remote file system, filename = "
				+ ftp_Filename);
		String ftpUri = "ftp://" + ftp_UserName + ":" + ftp_Password + "@"
				+ serverURL + "/" + ftp_Path + "/" + ftp_Filename + ".txt";

		FileSystemOptions opts = new FileSystemOptions();
		FtpFileSystemConfigBuilder configBuilder = FtpFileSystemConfigBuilder
				.getInstance();
		configBuilder.setUserDirIsRoot(opts, true);

		FileSystemManager fsManager;
		fsManager = VFS.getManager();
		FileObject blobFile = fsManager.resolveFile(ftpUri, opts);
		if (!blobFile.exists()) {
			blobFile.createFile();
		}
		FileContent blobContent = blobFile.getContent();
		try {
			OutputStream os = blobContent.getOutputStream();
			try {
				input.transferTo(os);
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

}
