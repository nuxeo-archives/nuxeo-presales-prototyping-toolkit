/**
 * 
 */

package org.nuxeo.presales.prototyping.toolkit.operations.ftp;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.automation.core.collectors.BlobCollector;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

/**
 * @author karlharris
 */
@Operation(id = ExportBlob.ID, category=Constants.CAT_BLOB, label="Export Blob", description="Export the input Blob file. " +
														"Use URI format: <protocol>://<username>:<password>@hostname/<folder>/filename." +
														"The operation returns the input blob unchanged")
public class ExportBlob extends BlobExport{

    public static final String ID = "ExportBlob";

    @Param(name = "URI")
    String uri;
    
    @OperationMethod(collector = BlobCollector.class)
    public Blob run(Blob input) throws Exception {
    	
    	return transfer(input,uri);
      
    }
    
    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentModel input) throws Exception {
    	return transfer(input,uri);
    }

}
