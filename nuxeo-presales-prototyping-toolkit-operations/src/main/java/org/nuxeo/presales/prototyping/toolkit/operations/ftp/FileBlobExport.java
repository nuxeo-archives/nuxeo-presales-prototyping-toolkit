/**
 * 
 */

package org.nuxeo.presales.prototyping.toolkit.operations.ftp;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.BlobCollector;
import org.nuxeo.ecm.core.api.Blob;


/*
 * @author karlharris
 */
@Operation(id=FileBlobExport.ID, category=Constants.CAT_BLOB, label="FileBlobExport", description="")
public class FileBlobExport extends BlobExport {

    public static final String ID = "FileBlobExport";
	protected @Param(name = "URI") String uriParams;
	
    @OperationMethod(collector=BlobCollector.class)
    public Blob run(Blob input) throws Exception {
    	
      return transfer(input, uriParams); 
    }    

}
