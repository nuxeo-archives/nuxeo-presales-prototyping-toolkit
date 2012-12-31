/**
 * 
 */

package org.nuxeo.presales.prototyping.toolkit.video.operations;

import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.BlobCollector;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.presales.prototyping.toolkit.video.mediainfo.MediaInfoHelper;

/**
 * @author fvadon
 */
@Operation(id=GetInfoFromMediaInfo.ID, category=Constants.CAT_SERVICES, label="Get Info From MediaInfo", description="Command line call to media info, the result is a map of maps put in the context variable resultContextVariable." +
		"Returns the input blob. An example would be result= "+
"{General={Format=MPEG-4, Complete name=test.mp4}, Audio={Format profile=Layer 2, Format=MPEG Audio, Delay relative to video=83ms, ID=0}, Video={Format profile=Baseline@L2.1, Format=AVC, Format/Info=Advanced Video Codec, ID=1, Height=288 pixels, Width=512 pixels}}")
public class GetInfoFromMediaInfo {

	public static final String ID = "GetInfoFromMediaInfo";

	@Context
	protected OperationContext ctx;

	@Param(name = "resultContextVariable")
	protected String resultContextVariable;

	@OperationMethod(collector=BlobCollector.class)
	public Blob run(Blob input) throws ClientException {

		if (input == null) {
			return null;
		}

		ctx.put(resultContextVariable, MediaInfoHelper.getProcessedMediaInfo(input));

		return input;

	}    

}

