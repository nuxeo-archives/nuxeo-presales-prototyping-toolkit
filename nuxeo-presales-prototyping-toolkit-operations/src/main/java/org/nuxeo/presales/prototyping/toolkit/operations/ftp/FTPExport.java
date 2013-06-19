/**
 * 
 */

package org.nuxeo.presales.prototyping.toolkit.operations.ftp;



import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
//import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;

import org.nuxeo.ecm.core.api.Blob;


/**
 * @author karlharris
 */
@Operation(id=FTPExport.ID, category=Constants.CAT_BLOB, label="Export file via ftp", 
					description="Use Apache VFS Library to Export this file via FTP")

public class FTPExport {


	@Param(name="ServerURL")
	String serverURL;
	@Param(name="Username")
	String ftp_UserName;
	@Param(name="Password")
	String ftp_Password;
	@Param(name="Path")
	String ftp_Path;
	@Param(name="Filename")
	String ftp_Filename;
	//@Param(name="Mime-type")
	//-String mime_Type;
	
	
    public static final String ID = "FTPExport";

    @OperationMethod
    public Blob run(Blob input) {
   
	      String ftpUri = "ftp://"+ftp_UserName+":"+ ftp_Password   +"@"+serverURL+"ftp_Path"+"/"+ftp_Filename;
			try {
	    		FileSystemManager fsManager = VFS.getManager();
	    		FileObject blobFile = fsManager.resolveFile(ftpUri);
	    		FileObject output = fsManager.createFileSystem(blobFile);
	    		input.transferTo(output.getContent().getOutputStream());

			}/*catch (FileSystemException e1) {
				e1.printStackTrace();
			}*/ catch (IOException e) {
				e.printStackTrace();
			}
      return input; 
    }    

}
