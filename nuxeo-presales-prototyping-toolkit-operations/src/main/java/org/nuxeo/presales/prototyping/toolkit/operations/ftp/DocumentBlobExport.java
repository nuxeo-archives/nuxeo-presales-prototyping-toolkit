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
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

/**
 * @author karlharris
 */
@Operation(id=DocumentBlobExport.ID, category=Constants.CAT_DOCUMENT, label="Document Blob Export", description="Export the input Document Blob file. " +
														"Use URI format: <protocol>://<username>:<password>@hostname/<folder>/filename." +
														"The operation returns the input Document unchanged")
public class DocumentBlobExport extends BlobExport {

    public static final String ID = "DocumentBlobExport";

    @Param (name="URI") String uri;
    
    @OperationMethod(collector=DocumentModelCollector.class)
    public DocumentModel run(DocumentModel input) throws Exception {
      return transfer (input,uri); 
    }    

}
