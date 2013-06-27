/**
 * 
 */

package org.nuxeo.presales.prototyping.toolkit.operations.ftp;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.DocumentModel;


/**
 * @author karlharris
 */
@Operation(id=DocBlobExport.ID, category=Constants.CAT_DOCUMENT, label="DocBlobExport", description="")
public class DocBlobExport extends BlobExport{

    public static final String ID = "DocBlobExport";
	protected @Param(name = "URI") String uriParams;

    @OperationMethod(collector=DocumentModelCollector.class)
    public DocumentModel run(DocumentModel input) throws Exception {
    	
    			return transfer(input, uriParams);
    			}

    }    

